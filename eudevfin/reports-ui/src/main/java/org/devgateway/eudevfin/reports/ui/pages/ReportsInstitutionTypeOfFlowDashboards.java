package org.devgateway.eudevfin.reports.ui.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author idobre
 * @since 4/16/14
 */
@MountPath(value = "/reportsinstitutiontypeofflowdashboards")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ReportsInstitutionTypeOfFlowDashboards extends HeaderFooter {
    private static final Logger logger = Logger.getLogger(ReportsInstitutionTypeOfFlowDashboards.class);

    private final int MILLION = 1000000;

    /*
     * variables used to dynamically create the MDX queries
     */
    // variables used for 'type of aid table'
    private String typeOfAidTableRowSet = "CrossJoin([Extending Agency].[Name].Members, [Type of Aid].[Name].Members)";

    private int tableYear;
    // variables that holds the parameters received from filter
    private String agencyParam;
    private String typeOfFlowParam;
    private String institutionParam;
    private String yearParam;
    private String humanitarianAidgParam;

    @SpringBean
    QueryService CdaService;

    public ReportsInstitutionTypeOfFlowDashboards(final PageParameters parameters) {
        // get a default year if it's not specified
        tableYear = Calendar.getInstance().get(Calendar.YEAR) - 1;

        // process the parameters received from the filters
        if(!parameters.get(ReportsConstants.AGENCY_PARAM).equals(StringValue.valueOf((String) null))) {
            agencyParam = parameters.get(ReportsConstants.AGENCY_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.TYPEOFFLOW_PARAM).equals(StringValue.valueOf((String) null))) {
            typeOfFlowParam = parameters.get(ReportsConstants.TYPEOFFLOW_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.INSTITUTION_PARAM).equals(StringValue.valueOf((String) null))) {
            institutionParam = parameters.get(ReportsConstants.INSTITUTION_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.YEAR_PARAM).equals(StringValue.valueOf((String) null))) {
            yearParam = parameters.get(ReportsConstants.YEAR_PARAM).toString();
            tableYear = Integer.parseInt(yearParam);
        }
        if(!parameters.get(ReportsConstants.HUMANITARIANAID_PARAM).equals(StringValue.valueOf((String) null))) {
            humanitarianAidgParam = parameters.get(ReportsConstants.HUMANITARIANAID_PARAM).toString();
        }

        addComponents();
    }

    private void addComponents() {
        Label title = new Label("title", new StringResourceModel("reportscountrytypeofaiddashboards.title", this, null, null));
        add(title);

        addTypeOfAidTable();
        addTypeOfAidChart();
    }

    private void addTypeOfAidTable () {
        Label title = new Label("typeOfAidTableTitle", new StringResourceModel("reportscountrytypeofaiddashboards.typeOfAidTable", this, null, null));
        add(title);

        Table table = new Table(CdaService, "typeOfAidTable", "typeOfAidTableRows", "customDashboardsTypeOfAidTable") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();

                List <List<String>> resultSet = result.getResultset();

                if(resultSet.size() != 0) {
                    // format the amounts as #,###.##
                    // and other values like percentages
                    DecimalFormat df = new DecimalFormat("#,###.##");
                    for (int i = 0; i < resultSet.size(); i++) {
                        if (resultSet.get(i).size() > 2 && resultSet.get(i).get(2) != null) {
                            String item = df.format(Float.parseFloat(resultSet.get(i).get(2))); // amounts - national currency (first year)
                            resultSet.get(i).set(2, item);
                        }

                        if (resultSet.get(i).size() > 3 && resultSet.get(i).get(3) != null) {
                            String item = df.format(Float.parseFloat(resultSet.get(i).get(3))); // amounts (first year)
                            resultSet.get(i).set(3, item);
                        }
                    }

                    // 'group by' institutions for countries
                    for (int i = resultSet.size() - 1; i > 0; i--) {
                        if(resultSet.get(i).get(0).equals(resultSet.get(i - 1).get(0))) {
                            resultSet.get(i).set(0, null);
                        }
                    }

                    for (List<String> item : resultSet) {
                        rows.add(item.toArray(new String[item.size()]));
                    }
                }

                ListView<String[]> tableRows = new ListView<String[]>(rowId, rows) {
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

        // add MDX queries parameters
        table.setParam("paramFIRST_YEAR", Integer.toString(tableYear ));

        table.setParam("paramtypeOfAidTableRowSet", typeOfAidTableRowSet);

        Label firstYear = new Label("firstYear", tableYear);
        table.getTable().add(firstYear);

        add(table.getTable());
        table.addTableRows();

        Label nationalCurrencyFirst = new Label("nationalCurrencyFirst", new StringResourceModel("reportscountrytypeofaiddashboards.nationalCurrency", this, null, null));
        table.getTable().add(nationalCurrencyFirst);
    }

    private void addTypeOfAidChart () {

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
