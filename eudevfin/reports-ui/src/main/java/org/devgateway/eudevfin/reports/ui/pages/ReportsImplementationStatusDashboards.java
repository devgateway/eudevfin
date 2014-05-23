package org.devgateway.eudevfin.reports.ui.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.reports.core.service.QueryService;
import org.devgateway.eudevfin.reports.ui.components.Table;
import org.devgateway.eudevfin.reports.ui.scripts.Dashboards;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author idobre
 * @since 4/16/14
 */
@MountPath(value = "/reportsimplementationstatusdashboards")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ReportsImplementationStatusDashboards extends HeaderFooter {
    private static final Logger logger = Logger.getLogger(ReportsImplementationStatusDashboards.class);

    private String countryCurrency = "$";

    private int tableYear;
    // variables that holds the parameters received from filter
    private String currencyParam;
    private String expenditureParam;
    private String startingYearParam;
    private String completitionYearParam;
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
        if(!parameters.get(ReportsConstants.COMPLETITIONYEAR_PARAM).equals(StringValue.valueOf((String) null))) {
            completitionYearParam = parameters.get(ReportsConstants.COMPLETITIONYEAR_PARAM).toString();
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

        logger.error(">>>>> expenditureParam: " + expenditureParam);
        logger.error(">>>>> startingYearParam: " + startingYearParam);
        logger.error(">>>>> completitionYearParam: " + completitionYearParam);
        logger.error(">>>>> valueParam: " + valueParam);
        logger.error(">>>>> budgetCodesParam: " + budgetCodesParam);
        logger.error(">>>>> coFinancingParam: " + coFinancingParam);

        addComponents();
    }

    private void addComponents() {
        Label title = new Label("title", new StringResourceModel("reportsimplementationstatusdashboards.title", this, null, null));
        add(title);

        addImplementationStatusTable();
        addImplementationStatusChart();
    }

    private void addImplementationStatusTable () {
        Label title = new Label("implementationTableTitle", "Net Disbursement by country - " + (tableYear - 1) + "-" + tableYear +
                " - " + countryCurrency + " - full amount");
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
                table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            }
        } else {
            table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
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

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "Dashboards.utilities.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "highcharts-no-data-to-display.js")));

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "canvg-1.3/rgbcolor.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "canvg-1.3/StackBlur.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "canvg-1.3/canvg.js")));

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "html2canvas.js")));

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "jspdf.min.js")));

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "reports.js")));
    }
}
