/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.reports;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.devgateway.eudevfin.dim.core.Constants;
import org.devgateway.eudevfin.dim.core.pages.HeaderFooter;
import org.devgateway.eudevfin.dim.pages.reports.components.DataTable;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath(value = "/reports")
@AuthorizeInstantiation(Constants.ROLE_USER)
public class ReportsPage extends HeaderFooter {

    public ReportsPage() {

        addComponents();
    }

    private void addComponents() {

//        Filter sectorFilter = new Filter("sectorFilter", "sectorList", "sectorListParameter");
//        add(sectorFilter);
//        Filter orgFilter = new Filter("orgFilter", "organizationList","organizationListParameter");
//        add(orgFilter);

        DataTable testTable = new DataTable("testTable", "dashboards.financialTransaction");
//        testTable.parameters().addFilter(sectorFilter);
//        testTable.parameters().addFilter(orgFilter);

        add(testTable);
        add(new DataTable("testTable2", "dashboards.financialTransaction"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forUrl("/js/cdfplugin.js"));


        response.render(JavaScriptHeaderItem.forUrl("/js/Highcharts-3.0.7/js/highcharts.js"));
        response.render(JavaScriptHeaderItem.forUrl("/js/Highcharts-3.0.7/js/modules/exporting.js"));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(ReportsPage.class, "FilterModel.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(ReportsPage.class, "TableModel.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(ReportsPage.class, "TableDefinitionModel.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(ReportsPage.class, "ChartModel.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(ReportsPage.class, "ChartPieDefinitionModel.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(ReportsPage.class, "ChartColumnDefinitionModel.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(ReportsPage.class, "StackedBarDefinitionModel.js")));

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(ReportsPage.class, "reports.js")));

    }
}
