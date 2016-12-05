/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.reports.ui.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.financial.util.FinancialTransactionUtil;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.reports.core.domain.Metadatum;
import org.devgateway.eudevfin.reports.core.service.QueryService;
import org.devgateway.eudevfin.reports.ui.components.PieChartNVD3;
import org.devgateway.eudevfin.reports.ui.components.StackedBarChartNVD3;
import org.devgateway.eudevfin.reports.ui.components.Table;
import org.joda.money.CurrencyUnit;
import org.springframework.context.i18n.LocaleContextHolder;
import org.wicketstuff.annotation.mount.MountPath;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author idobre
 * @since 3/11/14
 */

@MountPath(value = "/reports")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ReportsPage extends ReportsDashboards {
    private static final Logger logger = Logger.getLogger(ReportsPage.class);

    private final int MILLION = 1000000;
    private int tableYear;
    private String countryCurrency;

    @SpringBean
    QueryService CdaService;

    public ReportsPage() {
        tableYear = Calendar.getInstance().get(Calendar.YEAR) - 1;

        addComponents();
    }

    private void addComponents () {
        addReportingCountry();
        getCurrency();
        addNetODATable();
        addTopTenRecipientsTable();
        addTopTenMemoShareTAble();
        addOdaByRegionChart();
        addOdaByIncomeGroupChart();
        addOdaBySectorChart();
    }

    private void addReportingCountry () {
        String donorName = "";
        Organization organizationForCurrentUser = AuthUtils.getOrganizationForCurrentUser();

        if (organizationForCurrentUser != null) {
            donorName = organizationForCurrentUser.getDonorName();
        }

        Label reportingCountry = new Label("reportingCountry", donorName);
        add(reportingCountry);

        Map parameters = new HashMap();
        parameters.put("0", (tableYear - 1));
        parameters.put("1", tableYear);
        String legendText = ReportsDashboardsUtils.fillPattern(
                new StringResourceModel("ReportsPage.legend", this, null, null).getObject(), parameters);
        Label legend = new Label("legend", legendText);
        add(legend);
    }

    private void getCurrency () {
        countryCurrency = "";
        CurrencyUnit currencyForCountryIso = FinancialTransactionUtil
                .getCurrencyForCountryIso(AuthUtils
                        .getIsoCountryForCurrentUser());
        if (currencyForCountryIso != null) {
            countryCurrency = currencyForCountryIso.getCode();
        }
    }

    private void addNetODATable () {
        Label title = new Label("netODADashboardTitle", new StringResourceModel("ReportsPage.netODA", this, null, null));
        add(title);

        Table table = new Table(CdaService, "netODADashboard", "netODARows", "netODATable") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                final int NUMBER_OF_YEARS = 2;
                final String FIRST_YEAR = "" + (tableYear - 1);

                this.rows = new ArrayList<>();
                this.result = this.runQuery();
                List <List<String>> resultSet = this.result.getResultset();
                List <Metadatum>metadata = result.getMetadata();

                if(resultSet.get(0).size() != 1) {
                    // check programmatically that all columns are populated.
                    // there is no other elegant solution
                    // (we don't need to update the 'colIndex' property from metadata since we only use the results)
                    int len = resultSet.size();

                    if (metadata.size() > 1 && !metadata.get(1).getColName().equals(FIRST_YEAR)) {
                        // add new Metadatum item for missing year
                        Metadatum newItem = new Metadatum();
                        newItem.setColIndex(1);
                        newItem.setColType("Numeric");
                        newItem.setColName(FIRST_YEAR);

                        metadata.add(1, newItem);

                        // add the new dimension values for missing year
                        for (int i = 0; i < len; i++) {
                            resultSet.get(i).add(1, null);
                        }
                    }

                    if (metadata.size() > 2 && !metadata.get(2).getColName().equals("" + (Integer.parseInt(FIRST_YEAR) + 1))) {
                        // add new Metadatum item for missing year
                        Metadatum newItem = new Metadatum();
                        newItem.setColIndex(2);
                        newItem.setColType("Numeric");
                        newItem.setColName("" + (Integer.parseInt(FIRST_YEAR) + 1));

                        metadata.add(2, newItem);

                        // add the new dimension values for missing year
                        for (int i = 0; i < len; i++) {
                            resultSet.get(i).add(2, null);
                        }
                    }

                    // add '2013 / 2012' type of column
                    for (int i = 0; i < len - 2; i++) {
                        // add (last_year - 1 / last_year) column
                        // but first check if we have valid values
                        if (resultSet.get(i).get(NUMBER_OF_YEARS - 1) == null) {
                            resultSet.get(i).add(null);
                        } else {
                            if (resultSet.get(i).size()<=NUMBER_OF_YEARS || resultSet.get(i).get(NUMBER_OF_YEARS) == null) {
                                resultSet.get(i).add(null);
                            } else {
                                Double result = Double.parseDouble(resultSet.get(i).get(NUMBER_OF_YEARS - 1)) /
                                        Double.parseDouble(resultSet.get(i).get(NUMBER_OF_YEARS)) * 100;
                                Locale locale = LocaleContextHolder.getLocale();
                                DecimalFormat twoDForm = (DecimalFormat) NumberFormat.getNumberInstance(locale);
                                twoDForm.applyPattern("#.##");
                                resultSet.get(i).add(Float.valueOf(twoDForm.format(result)) + "%");
                            }
                        }
                    }

                    // add null values for '2013 / 2012' column on 'ODA/GNI' and 'Bilateral share' rows
                    for (int i = len - 2; i < len; i++) {
                        resultSet.get(i).add(null);
                    }

                    // display the results in millions
                    for (int i = 0; i < len - 2; i++) {
                        for (int j = 1; j <= NUMBER_OF_YEARS; j++) {
                            String item = resultSet.get(i).get(j);
                            if (item != null) {
                                resultSet.get(i).set(j, "" + Float.parseFloat(item) / ReportsPage.this.MILLION);
                            }
                        }
                    }

                    // format the amounts as #,###.##
                    // and other values like 'Bilateral share'
                    for (int i = 0; i < len; i++) {
                        for (int j = 1; j <= NUMBER_OF_YEARS; j++) {
                            String item = resultSet.get(i).get(j);

                            if (item != null) {
                                // len - 2 row is the 'ODA/GNI' row and we need to add the percentages
                                if (i == len - 2) {
                                    item = ReportsDashboardsUtils.AmountFormat(Float.parseFloat(resultSet.get(i).get(j)));
                                    item += "%";
                                } else {
                                    item = ReportsDashboardsUtils.AmountFormat(Float.parseFloat(resultSet.get(i).get(j)));
                                }

                                // len - 1 row is the 'Bilateral share' row and we need to add the percentages
                                if (i == len - 1) {
                                    item += "%";
                                }
                                resultSet.get(i).set(j, item);
                            }
                        }
                    }

                    for (List<String> item : resultSet) {
                        this.rows.add(item.toArray(new String[item.size()]));
                    }
                }

                ListView<String[]> tableRows = new ListView<String[]>(this.rowId, rows) {
                    @Override
                    protected void populateItem(ListItem<String[]> item) {
                        String[] row = item.getModelObject();

                        item.add(new Label("col0", row[0]));
                        item.add(new Label("col1", row[1]));
                        item.add(new Label("col2", row[2]));
                        item.add(new Label("col3", row[3]));
                    }
                };

                return tableRows;
            }
        };

        table.setParam("paramYEAR", Integer.toString(tableYear));
        table.setParam("paramYEAR1", Integer.toString(tableYear - 1));
        table.setParam("paramREPORT_CURRENCY_CODE", "National Currency (" + countryCurrency + " m)");

        Label tableYear1 = new Label("tableYear1", tableYear - 1);
        table.getTable().add(tableYear1);
        Label tableYear2 = new Label("tableYear2", tableYear);
        table.getTable().add(tableYear2);
        Label tableYear3 = new Label("tableYear3", "Change <br/> " +  tableYear + " / " + (tableYear - 1));
        tableYear3.setEscapeModelStrings(Boolean.FALSE);
        table.getTable().add(tableYear3);

        add(table.getTable());
        table.addTableRows();
    }

    private void addTopTenRecipientsTable () {
        Label title = new Label("topTenRecipientsTitle", new StringResourceModel("ReportsPage.topTenRecipients", this, null, null));
        add(title);

        Table table = new Table(CdaService, "topTenRecipients", "topTenRows", "topTenRecipients") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();
                List <List<String>> resultSet = this.result.getResultset();

                // format the amounts as #,###.###
                for(int i = 0; i < resultSet.size(); i++) {
                    // add numbers in front of the countries names
                    String item = resultSet.get(i).get(0);
                    if (item != null) {
                        item = i + 1 + " " + item;
                        resultSet.get(i).set(0, item);
                    }

                    // format the amounts
                    item = resultSet.get(i).get(1);

                    if (item != null) {
                        item = ReportsDashboardsUtils.AmountFormat(Float.parseFloat(resultSet.get(i).get(1)) / ReportsPage.this.MILLION);
                        resultSet.get(i).set(1, item);
                    }
                }

                for (List<String> item : resultSet) {
                    this.rows.add(item.toArray(new String[item.size()]));
                }

                ListView<String[]> tableRows = new ListView<String[]>(this.rowId, rows) {
                    @Override
                    protected void populateItem(ListItem<String[]> item) {
                        String[] row = item.getModelObject();

                        item.add(new Label("col0", row[0]));
                        item.add(new Label("col1", row[1]));
                    }
                };

                return tableRows;
            }
        };

        table.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        table.setParam("paramSECOND_YEAR", Integer.toString(tableYear));

        add(table.getTable());
        table.addTableRows();
    }

    private void addTopTenMemoShareTAble () {
        Label title = new Label("topTenMemoShareTitle", new StringResourceModel("ReportsPage.topTenMemoShare", this, null, null));
        add(title);

        Table table = new Table(CdaService, "topTenMemoShare", "topTenMemoRows", "topTenMemoShare") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();

                for (List<String> item : this.result.getResultset()) {
                    if (item.size() > 0) {
                        String[] newItem = new String[2];
                        newItem[0] = "Top 5 recipients";
                        newItem[1] = ReportsDashboardsUtils.twoDecimalFormat(item.get(0)) + "%";

                        rows.add(newItem);
                    }

                    if (item.size() > 1) {
                        String[] newItem = new String[2];
                        newItem[0] = "Top 10 recipients";
                        newItem[1] = ReportsDashboardsUtils.twoDecimalFormat(item.get(1)) + "%";

                        rows.add(newItem);
                    }

                    if (item.size() > 2) {
                        String[] newItem = new String[2];
                        newItem[0] = "Top 20 recipients";
                        newItem[1] = ReportsDashboardsUtils.twoDecimalFormat(item.get(2)) + "%";

                        rows.add(newItem);
                    }
                }

                ListView<String[]> tableRows = new ListView<String[]>(this.rowId, rows) {
                    @Override
                    protected void populateItem(ListItem<String[]> item) {
                        String[] row = item.getModelObject();

                        item.add(new Label("col0", row[0]));
                        item.add(new Label("col1", row[1]));
                    }
                };

                return tableRows;
            }
        };

        table.setParam("paramYEAR", Integer.toString(tableYear));

        add(table.getTable());
        table.addTableRows();
    }

    private void addOdaByIncomeGroupChart () {
        Label title = new Label("odaByIncomeGroupTitle", new StringResourceModel("ReportsPage.odaByIncomeGroupChart", this, null, null));
        add(title);

        PieChartNVD3 pieChartNVD3 = new PieChartNVD3(CdaService, "odaByIncomeGroupChart", "odaByIncomeGroupChart");

        // add MDX queries parameters
        pieChartNVD3.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        pieChartNVD3.setParam("paramSECOND_YEAR", Integer.toString(tableYear));

        pieChartNVD3.setUseMillion(true);

        add(pieChartNVD3);
    }

    private void addOdaByRegionChart () {
        Label title = new Label("odaByRegionTitle", new StringResourceModel("ReportsPage.odaByRegionChart", this, null, null));
        add(title);

        PieChartNVD3 pieChartNVD3 = new PieChartNVD3(CdaService, "odaByRegionChart", "odaByRegionChart");

        // add MDX queries parameters
        pieChartNVD3.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        pieChartNVD3.setParam("paramSECOND_YEAR", Integer.toString(tableYear));

        pieChartNVD3.setUseMillion(true);

        add(pieChartNVD3);
    }

    private void addOdaBySectorChart () {
        Label title = new Label("odaBySectorTitle", new StringResourceModel("ReportsPage.odaBySectorChart", this, null, null));
        add(title);

        StackedBarChartNVD3 stackedBarChartNVD3 = new StackedBarChartNVD3(CdaService, "odaBySectorChart", "odaBySectorChart");

        // add MDX queries parameters
        stackedBarChartNVD3.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        stackedBarChartNVD3.setParam("paramSECOND_YEAR", Integer.toString(tableYear));

        add(stackedBarChartNVD3);
    }
}
