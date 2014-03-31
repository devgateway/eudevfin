package org.devgateway.eudevfin.reports.ui.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.devgateway.eudevfin.reports.ui.scripts.Dashboards;

/**
 * @author idobre
 * @since 3/11/14
 */
public class DataTableDashboard extends WebMarkupContainer {
    private Boolean bPaginate;
    private Boolean bFilter;
    private Boolean bInfo;
    private Boolean bSort;
    private Boolean bLengthChange;
    private int iDisplayLength;
    private Boolean bSortableColumn;

    public DataTableDashboard(String id) {
        super(id);

        setOutputMarkupId(true);

        add(new AttributeModifier("class", Model.of("display")));

        // set the default Data Tables parameters
        bPaginate = false;
        bFilter = false;
        bInfo = false;
        bSort = false;
        bLengthChange = false;
        iDisplayLength = 10;
        bSortableColumn = false;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        renderDemoCSS(response);
        renderBasicJS(response);

        StringBuilder js = new StringBuilder();
        js.append("$(document).ready( function() {\n");
        js.append("	$('#" + getMarkupId() + "').dataTable( {\n");
        js.append("		\"bJQueryUI\": true,\n");
        js.append("		\"bPaginate\": " + bPaginate + ",\n");
        js.append("		\"bFilter\": " + bFilter + ",\n");
        js.append("		\"bInfo\": " + bInfo + ",\n");
        js.append("		\"bSort\": " + bSort + ",\n");
        js.append("		\"bLengthChange\": " + bLengthChange + ",\n");
        js.append("		\"iDisplayLength\": " + iDisplayLength + ",\n");
        js.append("		\"sPaginationType\": \"full_numbers\",\n");
        js.append("     \"aoColumnDefs\": [\n");
        if (bSortableColumn) {
            js.append("         { \"bSortable\": true, \"aTargets\": [] } \n");
        }
        js.append("] } );\n");
        js.append("} );");

        response.render(JavaScriptHeaderItem.forScript(js, getId() + "_datatables"));
    }

    private void renderDemoCSS(IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(new CssResourceReference(Dashboards.class,
                "DataTables-1.9.4/media/css/demo_table_jui.css")));
    }

    private void renderBasicJS(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class,
                "DataTables-1.9.4/media/js/jquery.dataTables.min.js")));
    }

    public void setbPaginate(Boolean bPaginate) {
        this.bPaginate = bPaginate;
    }

    public void setbFilter(Boolean bFilter) {
        this.bFilter = bFilter;
    }

    public void setbInfo(Boolean bInfo) {
        this.bInfo = bInfo;
    }

    public void setbSort(Boolean bSort) {
        this.bSort = bSort;
    }

    public void setbLengthChange(Boolean bLengthChange) {
        this.bLengthChange = bLengthChange;
    }

    public void setiDisplayLength(int iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }

    public void setbSortableColumn(Boolean bSortableColumn) {
        this.bSortableColumn = bSortableColumn;
    }
}
