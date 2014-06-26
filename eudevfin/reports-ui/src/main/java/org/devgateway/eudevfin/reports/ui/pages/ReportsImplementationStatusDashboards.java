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

/**
 * @author idobre
 * @since 4/16/14
 */
@MountPath(value = "/reportsimplementationstatusdashboards")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ReportsImplementationStatusDashboards extends ReportsDashboards {
    private static final Logger logger = Logger.getLogger(ReportsImplementationStatusDashboards.class);

    private String countryCurrency = "$";

    private int tableYear;
    // variables that holds the parameters received from filter
    private String currencyParam;
    private String expenditureParam;
    private String startingYearParam;
    private String completionYearParam;
    private String valueParam;
    private String budgetCodesParam;
    private String coFinancingParam;

    @SpringBean
    QueryService CdaService;

    public ReportsImplementationStatusDashboards(final PageParameters parameters) {
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
        if(!parameters.get(ReportsConstants.EXPENDITURE_PARAM).equals(StringValue.valueOf((String) null))) {
            expenditureParam = parameters.get(ReportsConstants.EXPENDITURE_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.STARTINGYEAR_PARAM).equals(StringValue.valueOf((String) null))) {
            startingYearParam = parameters.get(ReportsConstants.STARTINGYEAR_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.COMPLETIONYEAR_PARAM).equals(StringValue.valueOf((String) null))) {
            completionYearParam = parameters.get(ReportsConstants.COMPLETIONYEAR_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.VALUE_PARAM).equals(StringValue.valueOf((String) null))) {
            valueParam = parameters.get(ReportsConstants.VALUE_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.BUDGETCODES_PARAM).equals(StringValue.valueOf((String) null))) {
            budgetCodesParam = parameters.get(ReportsConstants.BUDGETCODES_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.COFINANCING_PARAM).equals(StringValue.valueOf((String) null))) {
            coFinancingParam = parameters.get(ReportsConstants.COFINANCING_PARAM).toString();
        }

        addComponents();
    }

    private void addComponents() {
        addImplementationStatusTable();
        addImplementationStatusChart();
    }

    private void addImplementationStatusTable () {
        String expenditure = "";
        if (expenditureParam != null && expenditureParam.equals("true")) {
            expenditure = new StringResourceModel("commitment", this, null, null).getObject();
        } else {
            expenditure = new StringResourceModel("disbursement", this, null, null).getObject();
        }
        Label title = new Label("implementationTableTitle", "Implementation Status - " + expenditure+ " - " +
                (tableYear - 1) + "-" + tableYear + " - " + countryCurrency + " - full amount");
        add(title);

        Table table = new Table(CdaService, "implementationTable", "implementationTableRows", "customDashboardsImplementationStatusTable") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();

                return ReportsDashboardsUtils.processTableRowsWithoutMainCategory(this.rows, this.result, this.rowId);
            }
        };

        // add MDX queries parameters
        table.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        table.setParam("paramSECOND_YEAR", Integer.toString(tableYear));

        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                if (expenditureParam != null && expenditureParam.equals("true")) {
                    table.setParam("paramcurrency", ReportsConstants.MDX_NAT_COMMITMENT_CURRENCY);
                } else {
                    table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
                }
            } else {
                if (expenditureParam != null && expenditureParam.equals("true")) {
                    table.setParam("paramcurrency", ReportsConstants.MDX_USD_COMMITMENT_CURRENCY);
                }
            }
        } else {
            if (expenditureParam != null && expenditureParam.equals("true")) {
                table.setParam("paramcurrency", ReportsConstants.MDX_NAT_COMMITMENT_CURRENCY);
            } else {
                table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            }
        }
        if (valueParam != null) {
            if (valueParam.equals("true")) {
                table.setParam("paramvalueParam", "> 500000");
            } else {
                if (valueParam.equals("false")) {
                    table.setParam("paramvalueParam", "< 500000");
                }
            }
        }
        if(coFinancingParam != null && coFinancingParam.equals("true")) {
            table.setParam("paramCOFINANCED", "[1]");
        }
        if(startingYearParam != null) {
            table.setParam("paramstartingYear", startingYearParam);
        }
        if(completionYearParam != null) {
            table.setParam("paramcompletionYear", completionYearParam);
        }

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

    private void addImplementationStatusChart () {
        String expenditure = "";
        if (expenditureParam != null && expenditureParam.equals("true")) {
            expenditure = new StringResourceModel("commitment", this, null, null).getObject();
        } else {
            expenditure = new StringResourceModel("disbursement", this, null, null).getObject();
        }
        Label title = new Label("implementationChartTitle", "Implementation Status - " + expenditure+ " - " +
                (tableYear - 1) + "-" + tableYear + " - " + countryCurrency + " - full amount");
        add(title);

        BarChartNVD3 barChartNVD3 = new BarChartNVD3(CdaService, "implementationChart", "customDashboardsImplementationStatusChart");

        // add MDX queries parameters
        barChartNVD3.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        barChartNVD3.setParam("paramSECOND_YEAR", Integer.toString(tableYear));
        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                if (expenditureParam != null && expenditureParam.equals("true")) {
                    barChartNVD3.setParam("paramcurrency", ReportsConstants.MDX_NAT_COMMITMENT_CURRENCY);
                } else {
                    barChartNVD3.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
                }
            } else {
                if (expenditureParam != null && expenditureParam.equals("true")) {
                    barChartNVD3.setParam("paramcurrency", ReportsConstants.MDX_USD_COMMITMENT_CURRENCY);
                }
            }
        } else {
            if (expenditureParam != null && expenditureParam.equals("true")) {
                barChartNVD3.setParam("paramcurrency", ReportsConstants.MDX_NAT_COMMITMENT_CURRENCY);
            } else {
                barChartNVD3.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            }
        }
        if (valueParam != null) {
            if (valueParam.equals("true")) {
                barChartNVD3.setParam("paramvalueParam", "> 500000");
            } else {
                if (valueParam.equals("false")) {
                    barChartNVD3.setParam("paramvalueParam", "< 500000");
                }
            }
        }
        if (coFinancingParam != null && coFinancingParam.equals("true")) {
            barChartNVD3.setParam("paramCOFINANCED", "[1]");
        }
        if (startingYearParam != null) {
            barChartNVD3.setParam("paramstartingYear", startingYearParam);
        }
        if (completionYearParam != null) {
            barChartNVD3.setParam("paramcompletionYear", completionYearParam);
        }

        barChartNVD3.setNumberOfSeries(2);
        barChartNVD3.setSeries1("Year " + (tableYear - 1));
        barChartNVD3.setSeries2("Year " + (tableYear));

        add(barChartNVD3);
    }
}
