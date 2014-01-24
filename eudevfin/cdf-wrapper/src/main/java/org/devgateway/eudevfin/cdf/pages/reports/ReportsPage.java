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
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.dashboard.Dashboards;
import org.devgateway.eudevfin.dashboard.components.ColumnsChart;
import org.devgateway.eudevfin.dashboard.components.Filter;
import org.devgateway.eudevfin.dashboard.components.PieChart;
import org.devgateway.eudevfin.dashboard.components.StackedBarChart;
import org.devgateway.eudevfin.dashboard.components.Table;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath(value = "/reports")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ReportsPage extends HeaderFooter {

    public ReportsPage() {
        addComponents();
    }

    private void addComponents() {
		// wicket ID, CDA ID, dashboard parameter
        Filter sectorFilter = new Filter("sectorFilter", "sectorList", "dashboards.sector.filter", "sectorListParameter");
        add(sectorFilter);
        
        Filter orgFilter = new Filter("orgFilter", "organizationList", "dashboards.org.filter", "organizationListParameter");
	    add(orgFilter);

	    Filter biMultilateralFilter = new Filter("biMultilateralFilter", "biMultilateralList", "dashboards.biMultilateral.filter", "biMultilateralListParameter");
	    add(biMultilateralFilter);

	    Table testTable = new Table("testTable", "typeOfFinance", "dashboards.typeOfFinance");
        testTable.parameters().addFilter(sectorFilter);
        testTable.parameters().addFilter(orgFilter);
        add(testTable);

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
    }
}
