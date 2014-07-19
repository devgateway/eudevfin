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
import org.devgateway.eudevfin.reports.core.service.QueryService;
import org.devgateway.eudevfin.reports.ui.components.BarChartNVD3;
import org.devgateway.eudevfin.reports.ui.components.Table;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author idobre
 * @since 4/16/14
 */
@MountPath(value = "/reportscountryinstitutiondashboards")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ReportsCountryInstitutionDashboards extends ReportsDashboards {
    private static final Logger logger = Logger.getLogger(ReportsCountryInstitutionDashboards.class);

    /*
     * variables used to dynamically create the MDX queries
     */
    // variables used for 'institution table'
    private String institutionTableRowSet = "CrossJoin([Extending Agency].[Name].Members, [Country].[Name].Members)";
    private String institutionTableRecipient = "CrossJoin([Extending Agency].[Name].Members, [Country].[Name].[__RECIPIENT__])";
    private String institutionTableInstitutionRecipient = "CrossJoin([Extending Agency].[Name].[__INSTITUTION__], [Country].[Name].__RECIPIENT__)";
    private String institutionTableGeography = "CrossJoin([Extending Agency].[Name].Members, Filter({{[Country].[Name].Members}}, " +
            "(Exists(Ancestor([Country].CurrentMember, [Country].[Geography]), {[Country].[__GEOGRAPHY__]}).Count  > 0)))";
    private String institutionTableInstitutionGeography = "CrossJoin([Extending Agency].[Name].[__INSTITUTION__], Filter({{[Country].[Name].Members}}, " +
            "(Exists(Ancestor([Country].CurrentMember, [Country].[Geography]), {[Country].[__GEOGRAPHY__]}).Count  > 0)))";

    private String institutionChartRowSet = "{Hierarchize({[Extending Agency].[Name].Members})}";
    private String institutionChartInstitution = "{Hierarchize({[Extending Agency].[Name].[__INSTITUTION__]})}";

    private String countryCurrency = "$";

    private int tableYear;
    // variables that holds the parameters received from filter
    private String currencyParam;
    private String geographyParam;
    private String recipientParam;
    private String institutionParam;
    private String yearParam;
    private String coFinancingParam;

    @SpringBean
    QueryService CdaService;

    public ReportsCountryInstitutionDashboards(final PageParameters parameters) {
        // get a default year if it's not specified
        tableYear = Calendar.getInstance().get(Calendar.YEAR) - 1;

        // process the parameters received from the filters
        if(!parameters.get(ReportsConstants.ISNATIONALCURRENCY_PARAM).equals(StringValue.valueOf((String) null))) {
            currencyParam = parameters.get(ReportsConstants.ISNATIONALCURRENCY_PARAM).toString();
            if (currencyParam.equals("true")) {
                countryCurrency = ReportsDashboardsUtils.getCurrency();
            }
        }
        // process the parameters received from the filters
        if(!parameters.get(ReportsConstants.GEOGRAPHY_PARAM).equals(StringValue.valueOf((String) null))) {
            geographyParam = parameters.get(ReportsConstants.GEOGRAPHY_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.RECIPIENT_PARAM).equals(StringValue.valueOf((String) null))) {
            recipientParam = parameters.get(ReportsConstants.RECIPIENT_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.INSTITUTION_PARAM).equals(StringValue.valueOf((String) null))) {
            institutionParam = parameters.get(ReportsConstants.INSTITUTION_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.YEAR_PARAM).equals(StringValue.valueOf((String) null))) {
            yearParam = parameters.get(ReportsConstants.YEAR_PARAM).toString();
            tableYear = Integer.parseInt(yearParam);
        }
        if(!parameters.get(ReportsConstants.COFINANCING_PARAM).equals(StringValue.valueOf((String) null))) {
            coFinancingParam = parameters.get(ReportsConstants.COFINANCING_PARAM).toString();
        }

        /*
         * create the MDX queries based on the filters
         */
        if (recipientParam != null) {
            institutionTableRecipient = institutionTableRecipient.replaceAll("__RECIPIENT__", recipientParam);

            if (institutionParam != null) {
                institutionTableInstitutionRecipient = institutionTableInstitutionRecipient.replaceAll("__INSTITUTION__", institutionParam);
                institutionTableInstitutionRecipient = institutionTableInstitutionRecipient.replaceAll("__RECIPIENT__", "[" + recipientParam + "]");

                institutionTableRowSet = institutionTableInstitutionRecipient;
            } else {
                institutionTableRowSet = institutionTableRecipient;
            }
        } else {
            if (geographyParam != null) {
                institutionTableGeography = institutionTableGeography.replaceAll("__GEOGRAPHY__", geographyParam);

                if (institutionParam != null) {
                    institutionTableInstitutionGeography = institutionTableInstitutionGeography.replaceAll("__INSTITUTION__", institutionParam);
                    institutionTableInstitutionGeography = institutionTableInstitutionGeography.replaceAll("__GEOGRAPHY__", geographyParam);

                    institutionTableRowSet = institutionTableInstitutionGeography;
                } else {
                    institutionTableRowSet = institutionTableGeography;
                }
            } else {
                if (institutionParam != null) {
                    institutionTableInstitutionRecipient = institutionTableInstitutionRecipient.replaceAll("__INSTITUTION__", institutionParam);
                    institutionTableInstitutionRecipient = institutionTableInstitutionRecipient.replaceAll("__RECIPIENT__", "Members");

                    institutionTableRowSet = institutionTableInstitutionRecipient;

                    institutionChartInstitution = institutionChartInstitution.replaceAll("__INSTITUTION__", institutionParam);
                    institutionChartRowSet = institutionChartInstitution;

                }
            }
        }

        addComponents();
    }

    private void addComponents() {
        addInstitutionTable();
        addInstitutionChart();
    }

    private void addInstitutionTable () {
        Map parameters = new HashMap();
        parameters.put("0", (tableYear - 1));
        parameters.put("1", tableYear);
        parameters.put("currency", countryCurrency);
        String titleText = ReportsDashboardsUtils.fillPattern(
                new StringResourceModel("ReportsCountryInstitutionDashboards.title", this, null, null).getObject(), parameters);
        Label title = new Label("institutionTableTitle", titleText);
        add(title);

        Table table = new Table(CdaService, "institutionTable", "institutionTableRows", "customDashboardsInstitutionTable") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();

                return ReportsDashboardsUtils.processTableRowsWithTotal(this.rows, this.result, this.rowId, true,
                        currencyParam, ReportsConstants.isCountry, true);
            }
        };

        // add MDX queries parameters
        table.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        table.setParam("paramSECOND_YEAR", Integer.toString(tableYear));
        if(coFinancingParam != null && coFinancingParam.equals("true")) {
            table.setParam("paramCOFINANCED", "[1]");
        }
        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            }
        }

        table.setParam("paraminstitutionTableRowSet", institutionTableRowSet);

        Label firstYear = new Label("firstYear", tableYear - 1);
        table.getTable().add(firstYear);
        Label secondYear = new Label("secondYear", tableYear);
        table.getTable().add(secondYear);

        add(table.getTable());
        table.addTableRows();

        Label currencyFirstYear = new Label("currencyFirstYear", countryCurrency);
        table.getTable().add(currencyFirstYear);
        Label currencySecondYear = new Label("currencySecondYear", countryCurrency);
        table.getTable().add(currencySecondYear);
    }

    private void addInstitutionChart () {
        Map parameters = new HashMap();
        parameters.put("0", (tableYear - 1));
        parameters.put("1", tableYear);
        parameters.put("currency", countryCurrency);
        String titleText = ReportsDashboardsUtils.fillPattern(
                new StringResourceModel("ReportsCountryInstitutionDashboards.title", this, null, null).getObject(), parameters);
        Label title = new Label("institutionChartTitle", titleText);
        add(title);

        BarChartNVD3 barChartNVD3 = new BarChartNVD3(CdaService, "institutionChart", "customDashboardsInstitutionChart");

        // add MDX queries parameters
        barChartNVD3.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        barChartNVD3.setParam("paramSECOND_YEAR", Integer.toString(tableYear));
        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                barChartNVD3.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            }
        }
        if (geographyParam != null) {
            barChartNVD3.setParam("paramCOUNTRIES", "[" + geographyParam + "]");
        } else {
            if (recipientParam != null) {
                barChartNVD3.setParam("paramCOUNTRIES", "[Name].[" + recipientParam + "]");
            }
        }
        if (coFinancingParam != null && coFinancingParam.equals("true")) {
            barChartNVD3.setParam("paramCOFINANCED", "[1]");
        }

        barChartNVD3.setParam("paraminstitutionChartRowSet", institutionChartRowSet);

        barChartNVD3.setNumberOfSeries(2);
        barChartNVD3.setSeries1("Year " + (tableYear - 1));
        barChartNVD3.setSeries2("Year " + (tableYear));

        add(barChartNVD3);
    }
}
