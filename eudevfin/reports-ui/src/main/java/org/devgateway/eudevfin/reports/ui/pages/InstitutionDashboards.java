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
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.reports.core.service.QueryService;
import org.devgateway.eudevfin.reports.ui.components.BarChartNVD3;
import org.devgateway.eudevfin.reports.ui.components.PieChartNVD3;
import org.devgateway.eudevfin.reports.ui.components.Table;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author idobre
 * @since 6/2/14
 */

@MountPath(value = "/institutiondashboards")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class InstitutionDashboards extends ReportsDashboards {
    private static final Logger logger = Logger.getLogger(InstitutionDashboards.class);

    private String countryCurrency = "$";

    private int tableYear;
    // variables that holds the parameters received from filter
    private String currencyParam;
    private String institutionParam;

    @SpringBean
    protected QueryService CdaService;

    @SpringBean
    private FinancialTransactionService financialTransactionService;

    public InstitutionDashboards(final PageParameters parameters) {
        // get the reporting year
        tableYear = Calendar.getInstance().get(Calendar.YEAR) - 1;

        // process the parameters received from the filters
        if(!parameters.get(ReportsConstants.ISNATIONALCURRENCY_PARAM).equals(StringValue.valueOf((String) null))) {
            currencyParam = parameters.get(ReportsConstants.ISNATIONALCURRENCY_PARAM).toString();
            if (currencyParam.equals("true")) {
                countryCurrency = ReportsDashboardsUtils.getCurrency();
            }
        } else {
            countryCurrency = ReportsDashboardsUtils.getCurrency();
        }
        if(!parameters.get(ReportsConstants.INSTITUTION_PARAM).equals(StringValue.valueOf((String) null))) {
            institutionParam = parameters.get(ReportsConstants.INSTITUTION_PARAM).toString();
        }

        String subTitle = new StringResourceModel("page.subtitle", this, null, null).getObject();
        Label country = new Label("institution", (institutionParam != null ? subTitle + " - " + institutionParam : ""));
        add(country);

        addComponents();
    }

    private void addComponents() {
        addTable();
        addPieChart();
        addBarChart();
        addTableList();
        addTypeBiMultilateralChart();
    }

    protected void addTable () {
        Map parameters = new HashMap();
        parameters.put("year", tableYear);
        parameters.put("currency", countryCurrency);
        String titleText = ReportsDashboardsUtils.fillPattern(
                new StringResourceModel("InstitutionDashboards.titleByCountrySector", this, null, null).getObject(), parameters);
        Label title = new Label("institutionTableTitle", titleText);
        add(title);

        Table table = new Table(CdaService, "institutionTable", "institutionTableRows", "institutionDashboardsTable") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();

                return ReportsDashboardsUtils.processTableRowsWithTotalOneYear(this.rows, this.result, this.rowId, true,
                        currencyParam, ReportsConstants.isCountry, true);
            }
        };

        // add MDX queries parameters
        table.setParam("paramFIRST_YEAR", Integer.toString(tableYear));
        table.setParam("paramInstitution", (institutionParam != null ? institutionParam : ""));
        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            }
        } else {
            table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
        }

        Label firstYear = new Label("firstYear", tableYear);
        table.getTable().add(firstYear);

        add(table.getTable());
        table.addTableRows();
    }

    private void addPieChart() {
        Map parameters = new HashMap();
        parameters.put("year", tableYear);
        parameters.put("currency", countryCurrency);
        String titleText = ReportsDashboardsUtils.fillPattern(
                new StringResourceModel("InstitutionDashboards.titleBySector", this, null, null).getObject(), parameters);
        Label title = new Label("institutionPieChartTitle", titleText);
        add(title);

        PieChartNVD3 pieChartNVD3 = new PieChartNVD3(CdaService, "institutionPieChart", "institutionDashboardsPieChart");

        // add MDX queries parameters
        pieChartNVD3.setParam("paramFIRST_YEAR", Integer.toString(tableYear));
        pieChartNVD3.setParam("paramInstitution", (institutionParam != null ? institutionParam : ""));
        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                pieChartNVD3.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            }
        } else {
            pieChartNVD3.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
        }

        add(pieChartNVD3);
    }

    private void addBarChart() {
        Map parameters = new HashMap();
        parameters.put("year", tableYear);
        parameters.put("currency", countryCurrency);
        String titleText = ReportsDashboardsUtils.fillPattern(
                new StringResourceModel("InstitutionDashboards.titleByCountry", this, null, null).getObject(), parameters);
        Label title = new Label("institutionBarChartTitle", titleText);
        add(title);

        BarChartNVD3 barChartNVD3 = new BarChartNVD3(CdaService, "institutionBarChart", "institutionDashboardsBarChart");

        // add MDX queries parameters
        barChartNVD3.setParam("paramFIRST_YEAR", Integer.toString(tableYear));
        barChartNVD3.setParam("paramInstitution", (institutionParam != null ? institutionParam : ""));
        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                barChartNVD3.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            }
        } else {
            barChartNVD3.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
        }

        barChartNVD3.setNumberOfSeries(1);
        barChartNVD3.setSeries1("Year " + (tableYear));

        add(barChartNVD3);
    }

    protected void addTableList () {
        Map parameters = new HashMap();
        parameters.put("year", tableYear);
        parameters.put("currency", countryCurrency);
        String titleText = ReportsDashboardsUtils.fillPattern(
                new StringResourceModel("InstitutionDashboards.title", this, null, null).getObject(), parameters);
        Label title = new Label("institutionTableListTitle", titleText);
        add(title);

        Table table = new Table(CdaService, "institutionTableList", "institutionTableListRows", "institutionDashboardsTableList") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();

                return ReportsDashboardsUtils.processTableRowsTransactions(financialTransactionService,
                        this.rows, this.result, this.rowId, currencyParam, ReportsConstants.isInstitution);
            }
        };

        // add MDX queries parameters
        table.setParam("paramFIRST_YEAR", Integer.toString(tableYear));
        table.setParam("paramInstitution", (institutionParam != null ? institutionParam : ""));
        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
                table.setParam("paramcurrencyDisbursement", ReportsConstants.MDX_NAT_EXTENDED_CURRENCY);
            }
        } else {
            table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            table.setParam("paramcurrencyDisbursement", ReportsConstants.MDX_NAT_EXTENDED_CURRENCY);
        }

        Label firstYear = new Label("firstYear", tableYear + " Disbursement");
        table.getTable().add(firstYear);

        add(table.getTable());
        table.addTableRows();
    }

    protected void addTypeBiMultilateralChart () {
        Label title = new Label("biMultilateralTitle", new StringResourceModel("ReportsInstitutionTypeOfAidDashboards.biMultilateralChart", this, null, null));
        add(title);

        PieChartNVD3 pieChartNVD3 = new PieChartNVD3(CdaService, "biMultilateralChart", "institutionDashboardsBiMultilateralChart");

        // add MDX queries parameters
        pieChartNVD3.setParam("paramFIRST_YEAR", Integer.toString(tableYear));
        pieChartNVD3.setParam("paramInstitution", (institutionParam != null ? institutionParam : ""));
        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                pieChartNVD3.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            }
        } else {
            pieChartNVD3.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
        }

        add(pieChartNVD3);
    }
}