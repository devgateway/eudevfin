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
 * @since 3/27/14
 */
@MountPath(value = "/reportscountrysectordashboards")
@AuthorizeInstantiation({AuthConstants.Roles.ROLE_USER,AuthConstants.Roles.ROLE_VIEWER})
public class ReportsCountrySectorDashboards extends ReportsDashboards {
    private static final Logger logger = Logger.getLogger(ReportsCountrySectorDashboards.class);

    /*
     * variables used to dynamically create the MDX queries
     */
    // variables used for 'country table'
    private String countryTableRowSet = "{Hierarchize({[Country].[Geography].Members, [Country].[Name].Members, [Country].[TOTAL]})}";
    private String countryTableGeography = "{Hierarchize({{[Country].[__GEOGRAPHY__]}, Filter({{[Country].[Name].Members}}, " +
            "(Exists(Ancestor([Country].CurrentMember, [Country].[Geography]), {[Country].[__GEOGRAPHY__]}).Count  > 0))}), [Country].[TOTAL]}";
    private String countryTableRecipient = "{Hierarchize({{[Country].[Name].[__RECIPIENT__]}}), [Country].[TOTAL]}";
    private String countryTableGeographyRecipient = "{Hierarchize({{[Country].[__GEOGRAPHY__]}, [Country].[Name].[__RECIPIENT__], [Country].[TOTAL]})}";
    private String countryTableTotal = "MEMBER [Country].[Total] AS IIF(SUM([Country].[Name].Members __CURRENCY__) > 0, SUM([Country].[Name].Members __CURRENCY__), Null)";
    private String countryTableTotalGeography = "MEMBER [Country].[Total] AS IIF(SUM([Country].__GEOGRAPHY__ __CURRENCY__) > 0, SUM([Country].__GEOGRAPHY__ __CURRENCY__), Null)";

    // variables used for 'country chart'
    private String countryChartRowSet = "{[Country].[Name].Members}";
    private String countryChartGeography = "{Filter({{[Country].[Name].Members}}, (Exists(Ancestor([Country].CurrentMember, [Country].[Geography]), {[Country].[__GEOGRAPHY__]}).Count  > 0))}";
    private String countryChartRecipient = "{[Country].[Name].[__RECIPIENT__]}";

    // variables used for 'sector table'
    private String sectorTableRowSet = "{Hierarchize({[SectorHierarchy].[ParentName].Members, [SectorHierarchy].[Name].Members, [SectorHierarchy].[TOTAL]})}";
    private String sectorTableSector = "{Hierarchize({[SectorHierarchy].[Name].[__SECTOR__], [SectorHierarchy].[TOTAL]})}";
    private String sectorTableParentSector = "{Hierarchize({{[SectorHierarchy].[__PARENT_SECTOR__]}, Filter({{[SectorHierarchy].[Name].Members}}, " +
            "(Exists(Ancestor([SectorHierarchy].CurrentMember, [SectorHierarchy].[ParentName]), {[SectorHierarchy].[__PARENT_SECTOR__]}).Count  > 0))}), [SectorHierarchy].[TOTAL]}";

    private String sectorTableTotal = "MEMBER[SectorHierarchy].[Total] AS IIF(SUM([SectorHierarchy].[Name].Members __CURRENCY__) > 0, SUM([SectorHierarchy].[Name].Members __CURRENCY__), Null)";
    private String sectorTableTotalSector = "MEMBER[SectorHierarchy].[Total] AS IIF(SUM([SectorHierarchy].[Name].[__SECTOR__] __CURRENCY__) > 0, SUM([SectorHierarchy].[Name].[__SECTOR__] __CURRENCY__), Null)";
    private String sectorTableTotalParentSector = "MEMBER [SectorHierarchy].[Total] AS IIF(SUM([SectorHierarchy].[__PARENT_SECTOR__] __CURRENCY__) > 0, SUM([SectorHierarchy].[__PARENT_SECTOR__] __CURRENCY__), Null)";

    // variables used for 'sector chart'
    private String sectorChartRowSet = "{[SectorHierarchy].[Name].Members}";
    private String sectorChartSector = "{[SectorHierarchy].[Name].[__SECTOR__]}";
    private String sectorChartParentSector = "{Filter({{[SectorHierarchy].[Name].Members}}, (Exists(Ancestor([SectorHierarchy].CurrentMember, [SectorHierarchy].[ParentName]), {[SectorHierarchy].[__PARENT_SECTOR__]}).Count  > 0))}";

    private String countryCurrency = "$";

    private int tableYear;
    // variables that holds the parameters received from filter
    private String currencyParam;
    private String geographyParam;
    private String recipientParam;
    private String sectorParam;
    private String isRootSectorParam;
    private String yearParam;
    private String coFinancingParam;
    private String CPAOnlyParam;

    @SpringBean
    protected QueryService CdaService;

    public ReportsCountrySectorDashboards(final PageParameters parameters) {
        // get a default year if it's not specified
        tableYear = Calendar.getInstance().get(Calendar.YEAR) - 1;

        // process the parameters received from the filters
        if(!parameters.get(ReportsConstants.ISNATIONALCURRENCY_PARAM).equals(StringValue.valueOf((String) null))) {
            currencyParam = parameters.get(ReportsConstants.ISNATIONALCURRENCY_PARAM).toString();
            if (currencyParam.equals("true")) {
                countryCurrency = ReportsDashboardsUtils.getCurrency();
            }
        }
        if(!parameters.get(ReportsConstants.GEOGRAPHY_PARAM).equals(StringValue.valueOf((String) null))) {
            geographyParam = parameters.get(ReportsConstants.GEOGRAPHY_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.RECIPIENT_PARAM).equals(StringValue.valueOf((String) null))) {
            recipientParam = parameters.get(ReportsConstants.RECIPIENT_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.SECTOR_PARAM).equals(StringValue.valueOf((String) null))) {
            sectorParam = parameters.get(ReportsConstants.SECTOR_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.ISROOTSECTOR_PARAM).equals(StringValue.valueOf((String) null))) {
            isRootSectorParam = parameters.get(ReportsConstants.ISROOTSECTOR_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.YEAR_PARAM).equals(StringValue.valueOf((String) null))) {
            yearParam = parameters.get(ReportsConstants.YEAR_PARAM).toString();
            tableYear = Integer.parseInt(yearParam);
        }
        if(!parameters.get(ReportsConstants.COFINANCING_PARAM).equals(StringValue.valueOf((String) null))) {
            coFinancingParam = parameters.get(ReportsConstants.COFINANCING_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.CPAONLY_PARAM).equals(StringValue.valueOf((String) null))) {
            CPAOnlyParam = parameters.get(ReportsConstants.CPAONLY_PARAM).toString();
        }

        /*
         * create the MDX queries based on the filters
         */
        if (geographyParam != null && recipientParam != null) {
            // calculate 'country table' row set
            countryTableGeographyRecipient = countryTableGeographyRecipient.replaceAll("__GEOGRAPHY__", geographyParam);
            countryTableGeographyRecipient = countryTableGeographyRecipient.replaceAll("__RECIPIENT__", recipientParam);
            countryTableRowSet = countryTableGeographyRecipient;

            // calculate 'country table' Total value
            // use the region to calculate the total amount
            countryTableTotalGeography = countryTableTotalGeography.replaceAll("__GEOGRAPHY__", "[" + geographyParam + "]");
            countryTableTotal = countryTableTotalGeography;

            // calculate 'country chart' row set
            countryChartRecipient = countryChartRecipient.replaceAll("__RECIPIENT__", recipientParam);
            countryChartRowSet = countryChartRecipient;
        } else {
            if (geographyParam != null) {
                // calculate 'country table' row set
                countryTableGeography = countryTableGeography.replaceAll("__GEOGRAPHY__", geographyParam);
                countryTableRowSet = countryTableGeography;

                // calculate 'country table' Total value
                countryTableTotalGeography = countryTableTotalGeography.replaceAll("__GEOGRAPHY__", "[" + geographyParam + "]");
                countryTableTotal = countryTableTotalGeography;

                // calculate 'country chart' row set
                countryChartGeography = countryChartGeography.replaceAll("__GEOGRAPHY__", geographyParam);
                countryChartRowSet = countryChartGeography;
            } else {
                if (recipientParam != null) {
                    // calculate 'country table' row set
                    countryTableRecipient = countryTableRecipient.replaceAll("__RECIPIENT__", recipientParam);
                    countryTableRowSet = countryTableRecipient;

                    // calculate 'country table' Total value
                    countryTableTotalGeography = countryTableTotalGeography.replaceAll("__GEOGRAPHY__", "[Name].[" + recipientParam + "]");
                    countryTableTotal = countryTableTotalGeography;

                    // calculate 'country chart' row set
                    countryChartRecipient = countryChartRecipient.replaceAll("__RECIPIENT__", recipientParam);
                    countryChartRowSet = countryChartRecipient;
                }
            }
        }

        if(sectorParam != null) {
            // calculate 'sector table' row set
            if (isRootSectorParam != null && isRootSectorParam.equals("true")) {
                sectorTableParentSector = sectorTableParentSector.replaceAll("__PARENT_SECTOR__", sectorParam);
                sectorTableRowSet = sectorTableParentSector;

                sectorTableTotalParentSector = sectorTableTotalParentSector.replaceAll("__PARENT_SECTOR__", sectorParam);
                sectorTableTotal = sectorTableTotalParentSector;

                sectorChartParentSector = sectorChartParentSector.replaceAll("__PARENT_SECTOR__", sectorParam);
                sectorChartRowSet = sectorChartParentSector;
            } else {
                sectorTableSector = sectorTableSector.replaceAll("__SECTOR__", sectorParam);
                sectorTableRowSet = sectorTableSector;

                // calculate 'sector table' Total value
                sectorTableTotalSector = sectorTableTotalSector.replaceAll("__SECTOR__", sectorParam);
                sectorTableTotal = sectorTableTotalSector;

                // calculate 'sector chart' row set
                sectorChartSector = sectorChartSector.replaceAll("__SECTOR__", sectorParam);
                sectorChartRowSet = sectorChartSector;
            }
        }

        addComponents();
    }

    private void addComponents() {
        addCountryTable();
        addCountryChart();
        addSectorTable();
        addSectorChart();
    }

    private void addCountryTable () {
        Map parameters = new HashMap();
        parameters.put("0", (tableYear - 1));
        parameters.put("1", tableYear);
        parameters.put("currency", countryCurrency);
        String titleText = ReportsDashboardsUtils.fillPattern(
                new StringResourceModel("ReportsCountrySectorDashboards.titleByCountry", this, null, null).getObject(), parameters);
        Label title = new Label("countryTableTitle", titleText);
        add(title);

        Table table = new Table(CdaService, "countryTable", "countryTableRows", "customDashboardsCountryTable") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();

                return ReportsDashboardsUtils.processTableRows(this.rows, this.result, this.rowId,
                        currencyParam, ReportsConstants.isCountry);
            }
        };

        // add MDX queries parameters
        table.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        table.setParam("paramSECOND_YEAR", Integer.toString(tableYear));

        // we use to methods to calculate the total, one for total line, and one for calculating the percentages
        table.setParam("paramcountryTableTotalLine", countryTableTotal.replaceAll("__CURRENCY__", "").replaceAll("Total", "TOTAL"));

        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
                countryTableTotal = countryTableTotal.replaceAll("__CURRENCY__", ", [Measures].[" + ReportsConstants.MDX_NAT_CURRENCY + "]");
            } else {
                countryTableTotal = countryTableTotal.replaceAll("__CURRENCY__", ", [Measures].[" + ReportsConstants.MDX_USD_CURRENCY + "]");
            }
        } else {
            table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            countryTableTotal = countryTableTotal.replaceAll("__CURRENCY__", ", [Measures].[" + ReportsConstants.MDX_NAT_CURRENCY + "]");
        }

        if (sectorParam != null) {
            table.setParam("paramSECTOR", "[Name].[" + sectorParam + "]");
        }
        if(coFinancingParam != null && coFinancingParam.equals("true")) {
            table.setParam("paramCOFINANCED", "[1]");
        }
        if(CPAOnlyParam != null && CPAOnlyParam.equals("true")) {
            table.setParam("paramCPA", "[1]");
        }
        table.setParam("paramcountryTableRowSet", countryTableRowSet);
        table.setParam("paramcountryTableTotal", countryTableTotal);

        table.setdisableStripeClasses(Boolean.TRUE);

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

    private void addCountryChart () {
        Map parameters = new HashMap();
        parameters.put("0", (tableYear - 1));
        parameters.put("1", tableYear);
        parameters.put("currency", countryCurrency);
        String titleText = ReportsDashboardsUtils.fillPattern(
                new StringResourceModel("ReportsCountrySectorDashboards.titleByCountry", this, null, null).getObject(), parameters);
        Label title = new Label("countryChartTitle", titleText);
        add(title);

        BarChartNVD3 barChartNVD3 = new BarChartNVD3(CdaService, "countryChart", "customDashboardsCountryChart");

        // add MDX queries parameters
        barChartNVD3.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        barChartNVD3.setParam("paramSECOND_YEAR", Integer.toString(tableYear));
        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                barChartNVD3.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            }
        } else {
            barChartNVD3.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
        }

        if (sectorParam != null) {
            barChartNVD3.setParam("paramSECTOR", "[Name].[" + sectorParam + "]");
        }
        if (coFinancingParam != null && coFinancingParam.equals("true")) {
            barChartNVD3.setParam("paramCOFINANCED", "[1]");
        }
        if (CPAOnlyParam != null && CPAOnlyParam.equals("true")) {
            barChartNVD3.setParam("paramCPA", "[1]");
        }
        barChartNVD3.setParam("paramcountryChartRowSet", countryChartRowSet);

        barChartNVD3.setNumberOfSeries(2);
        barChartNVD3.setSeries1("Year " + (tableYear - 1));
        barChartNVD3.setSeries2("Year " + (tableYear));

        add(barChartNVD3);
    }

    private void addSectorTable () {
        Map parameters = new HashMap();
        parameters.put("0", (tableYear - 1));
        parameters.put("1", tableYear);
        parameters.put("currency", countryCurrency);
        String titleText = ReportsDashboardsUtils.fillPattern(
                new StringResourceModel("ReportsCountrySectorDashboards.titleBySector", this, null, null).getObject(), parameters);
        Label title = new Label("sectorTableTitle", titleText);
        add(title);

        Table table = new Table(CdaService, "sectorTable", "sectorTableRows", "customDashboardsSectorTable") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();

                return ReportsDashboardsUtils.processTableRows(this.rows, this.result, this.rowId,
                        currencyParam, ReportsConstants.isSector);
            }
        };

        // add MDX queries parameters
        table.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        table.setParam("paramSECOND_YEAR", Integer.toString(tableYear));
        table.setParam("paramsectorTableTotalLine", sectorTableTotal.replaceAll("__CURRENCY__", "").replaceAll("Total", "TOTAL"));
        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
                sectorTableTotal = sectorTableTotal.replaceAll("__CURRENCY__", ", [Measures].[" + ReportsConstants.MDX_NAT_CURRENCY + "]");
            } else {
                sectorTableTotal = sectorTableTotal.replaceAll("__CURRENCY__", ", [Measures].[" + ReportsConstants.MDX_USD_CURRENCY + "]");
            }
        } else {
            table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            sectorTableTotal = sectorTableTotal.replaceAll("__CURRENCY__", ", [Measures].[" + ReportsConstants.MDX_NAT_CURRENCY + "]");
        }
        if (geographyParam != null && recipientParam != null) {
            table.setParam("paramCOUNTRIES", "[" + geographyParam + "].[" + recipientParam + "]");
        } else {
            if (geographyParam != null) {
                table.setParam("paramCOUNTRIES", "[" + geographyParam + "]");
            } else {
                if (recipientParam != null) {
                    table.setParam("paramCOUNTRIES", "[Name].[" + recipientParam + "]");
                }
            }
        }
        if(coFinancingParam != null && coFinancingParam.equals("true")) {
            table.setParam("paramCOFINANCED", "[1]");
        }
        if(CPAOnlyParam != null && CPAOnlyParam.equals("true")) {
            table.setParam("paramCPA", "[1]");
        }
        table.setParam("paramsectorTableRowSet", sectorTableRowSet);
        table.setParam("paramsectorTableTotal", sectorTableTotal);

        table.setdisableStripeClasses(Boolean.TRUE);

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

    private void addSectorChart () {
        Map parameters = new HashMap();
        parameters.put("0", (tableYear - 1));
        parameters.put("1", tableYear);
        parameters.put("currency", countryCurrency);
        String titleText = ReportsDashboardsUtils.fillPattern(
                new StringResourceModel("ReportsCountrySectorDashboards.titleBySector", this, null, null).getObject(), parameters);
        Label title = new Label("sectorChartTitle", titleText);
        add(title);

        BarChartNVD3 barChartNVD3 = new BarChartNVD3(CdaService, "sectorChart", "customDashboardsSectorChart");

        // add MDX queries parameters
        barChartNVD3.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        barChartNVD3.setParam("paramSECOND_YEAR", Integer.toString(tableYear));
        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                barChartNVD3.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            }
        } else {
            barChartNVD3.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
        }
        if (geographyParam != null && recipientParam != null) {
            barChartNVD3.setParam("paramCOUNTRIES", "[" + geographyParam + "].[" + recipientParam + "]");
        } else {
            if (geographyParam != null) {
                barChartNVD3.setParam("paramCOUNTRIES", "[" + geographyParam + "]");
            } else {
                if (recipientParam != null) {
                    barChartNVD3.setParam("paramCOUNTRIES", "[Name].[" + recipientParam + "]");
                }
            }
        }
        if (coFinancingParam != null && coFinancingParam.equals("true")) {
            barChartNVD3.setParam("paramCOFINANCED", "[1]");
        }
        if (CPAOnlyParam != null && CPAOnlyParam.equals("true")) {
            barChartNVD3.setParam("paramCPA", "[1]");
        }
        barChartNVD3.setParam("paramsectorChartRowSet", sectorChartRowSet);

        barChartNVD3.setNumberOfSeries(2);
        barChartNVD3.setSeries1("Year " + (tableYear - 1));
        barChartNVD3.setSeries2("Year " + (tableYear));

        add(barChartNVD3);
    }
}
