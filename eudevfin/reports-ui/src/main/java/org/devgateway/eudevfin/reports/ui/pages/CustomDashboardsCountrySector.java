package org.devgateway.eudevfin.reports.ui.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.reports.core.service.QueryService;
import org.devgateway.eudevfin.reports.ui.components.Table;
import org.devgateway.eudevfin.reports.ui.scripts.Dashboards;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

import javax.sql.DataSource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 3/27/14
 */
@MountPath(value = "/dashboardscountrysector")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class CustomDashboardsCountrySector extends HeaderFooter {
    private static final Logger logger = Logger.getLogger(CustomDashboardsCountrySector.class);

    @SpringBean
    QueryService CdaService;

    @SpringBean
    private DataSource cdaDataSource;

    public CustomDashboardsCountrySector() {
        addComponents();
    }

    private void addComponents() {
        addCountryTable();
    }

    private void addCountryTable () {
        Label title = new Label("countryTableTitle", new StringResourceModel("customdashboards.countryTable", this, null, null));
        add(title);

        Table table = new Table(CdaService, "countryTable", "countryTableRows", "customdashboardscountry") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();
                List <List<String>> resultSet = this.result.getResultset();

                logger.info(">>>>>>>>>>>>>>>>>>>>>>>");
                logger.info(resultSet);

                // format the amounts as #,###
                DecimalFormat df = new DecimalFormat("#,###");
                for(int i = 0; i < resultSet.size(); i++) {
                    if (resultSet.get(i).size() > 1 && resultSet.get(i).get(1) != null) {
                        String item = df.format(Float.parseFloat(resultSet.get(i).get(1)));
                        resultSet.get(i).set(1, item);
                    }

                    if (resultSet.get(i).size() > 2 && resultSet.get(i).get(2) != null) {
                        String item = df.format(Float.parseFloat(resultSet.get(i).get(2)));
                        resultSet.get(i).set(2, item);
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
                        item.add(new Label("col2", row[2]));
                    }
                };

                return tableRows;
            }
        };

        table.setbSort(true);

        add(table.getTable());
        table.addTableRows();
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
