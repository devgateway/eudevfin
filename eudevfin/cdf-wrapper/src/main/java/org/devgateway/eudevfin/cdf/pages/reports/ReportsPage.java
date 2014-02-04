/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.cdf.pages.reports;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.dashboard.Dashboards;
import org.devgateway.eudevfin.dashboard.components.ColumnsChart;
import org.devgateway.eudevfin.dashboard.components.PieChart;
import org.devgateway.eudevfin.dashboard.components.StackedBarChart;
import org.devgateway.eudevfin.dashboard.components.Table;
import org.devgateway.eudevfin.dashboard.components.TableParameters;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.Arrays;

@MountPath(value = "/reports")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ReportsPage extends HeaderFooter {
	private static final int TABLE_YEAR = 2013;

    public ReportsPage() {
        addComponents();
    }

    private void addComponents() {
	    Table netODADashboard = new Table("netODADashboard", "netODATable", "dashboards.netODA");
	    // js function that will be called to initialize the table dashboard
	    netODADashboard.setInitFunction("addNetODATable");

	    TableParameters netODAParameters = netODADashboard.getParameters();

	    // for now we use a hardcoded value for year in order to display the last 3 years data for 'Net ODA' table
	    netODAParameters.addParameter("YEAR", Integer.toString(TABLE_YEAR));
	    netODAParameters.addParameter("YEAR1", Integer.toString(TABLE_YEAR - 1));
	    netODAParameters.addParameter("YEAR2", Integer.toString(TABLE_YEAR - 2));

	    netODAParameters.getChartDefinition().setColHeaders(Arrays.asList("Net ODA", Integer.toString(TABLE_YEAR - 2),
			    Integer.toString(TABLE_YEAR - 1),
			    Integer.toString(TABLE_YEAR ),
			    Integer.toString(TABLE_YEAR - 1) + "/ " + Integer.toString(TABLE_YEAR)));
	    netODAParameters.getChartDefinition().setColTypes(Arrays.asList("string", "numeric", "numeric", "numeric", "percentFormat"));
	    netODAParameters.getChartDefinition().setColFormats(Arrays.asList("%s", "%.0f", "%.0f", "%.0f", "%.2f"));
	    netODAParameters.getChartDefinition().setColWidths(Arrays.asList("20%", "20%", "20%", "20%", "20%"));
	    netODAParameters.getChartDefinition().setSort(Boolean.FALSE);
	    netODAParameters.getChartDefinition().setPaginate(Boolean.FALSE);
	    netODAParameters.getChartDefinition().setInfo(Boolean.FALSE);
	    netODAParameters.getChartDefinition().setFilter(Boolean.FALSE);
	    netODAParameters.getChartDefinition().setColSortable(Arrays.asList(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE));

        add(netODADashboard);

	    Table topTenRecipients = new Table("topTenRecipients", "topTenRecipients", "dashboards.topTenRecipients");
	    topTenRecipients.setInitFunction("addTopTenRecipients");

	    TableParameters topTenRecipientsParameters = topTenRecipients.getParameters();

	    topTenRecipientsParameters.getChartDefinition().setColHeaders(Arrays.asList("Countries", "Amount"));
	    topTenRecipientsParameters.getChartDefinition().setColTypes(Arrays.asList("string", "numeric"));
	    topTenRecipientsParameters.getChartDefinition().setColFormats(Arrays.asList("%s", "%.0f"));
	    topTenRecipientsParameters.getChartDefinition().setColWidths(Arrays.asList("60%", "40%"));
	    topTenRecipientsParameters.getChartDefinition().setSort(Boolean.TRUE);
	    topTenRecipientsParameters.getChartDefinition().setPaginate(Boolean.FALSE);
	    topTenRecipientsParameters.getChartDefinition().setInfo(Boolean.FALSE);
	    topTenRecipientsParameters.getChartDefinition().setFilter(Boolean.FALSE);
	    topTenRecipientsParameters.getChartDefinition().setColSortable(Arrays.asList(Boolean.TRUE, Boolean.TRUE));

	    add(topTenRecipients);



	    Table topTenMemoShare = new Table("topTenMemoShare", "topTenMemoShare", "dashboards.topTenMemoShare");
	    topTenMemoShare.setInitFunction("addTopTenMemoShare");

	    TableParameters topTenMemoShareParameters = topTenMemoShare.getParameters();

	    topTenMemoShareParameters.getChartDefinition().setColHeaders(Arrays.asList("Top", "Percent"));
	    topTenMemoShareParameters.getChartDefinition().setColTypes(Arrays.asList("string", "percentFormat"));
	    topTenMemoShareParameters.getChartDefinition().setColFormats(Arrays.asList("%s", "%.0f"));
	    topTenMemoShareParameters.getChartDefinition().setColWidths(Arrays.asList("60%", "40%"));
	    topTenMemoShareParameters.getChartDefinition().setSort(Boolean.FALSE);
	    topTenMemoShareParameters.getChartDefinition().setPaginate(Boolean.FALSE);
	    topTenMemoShareParameters.getChartDefinition().setInfo(Boolean.FALSE);
	    topTenMemoShareParameters.getChartDefinition().setFilter(Boolean.FALSE);
	    topTenMemoShareParameters.getChartDefinition().setColSortable(Arrays.asList(Boolean.FALSE, Boolean.FALSE));

	    add(topTenMemoShare);

        PieChart pieChart = new PieChart("pieChart", "typeOfAid", "dashboards.typeOfAid");
        add(pieChart);

        ColumnsChart columnsChart = new ColumnsChart("columnsChart", "typeOfFlow", "dashboards.typeOfFlow");
        add(columnsChart);

        StackedBarChart stackedBarChart = new StackedBarChart("stackedBarChart", "typeOfSectorsByFlow", "dashboards.typeOfSectorsByFlow");
        add(stackedBarChart);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

	    // load CDF plugin
        response.render(JavaScriptHeaderItem.forUrl("/js/cdfplugin.min.js"));

        // highcharts
        response.render(JavaScriptHeaderItem.forUrl("/js/Highcharts-3.0.7/js/highcharts.js"));
        response.render(JavaScriptHeaderItem.forUrl("/js/Highcharts-3.0.7/js/modules/exporting.js"));
        
        // dashboard models
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "FilterModel.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "TableModel.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "TableDefinitionModel.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "ChartModel.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "ChartPieDefinitionModel.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "ChartColumnDefinitionModel.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "StackedBarDefinitionModel.js")));

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "reports.js")));
	    // The initial dashboard load function execution
	    response.render(OnDomReadyHeaderItem.forScript("app.load();"));
    }
}
