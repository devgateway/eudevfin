package org.devgateway.eudevfin.reports.ui.pages;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.devgateway.eudevfin.reports.ui.scripts.Dashboards;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.devgateway.eudevfin.ui.common.scripts.CommonScriptsReference;

/**
 * @author idobre
 * @since 6/2/14
 */
public class ReportsDashboards extends HeaderFooter {
    protected static final Boolean USE_NVD3 = true;

    public ReportsDashboards () {

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        if (!USE_NVD3) {
            response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "highcharts-fixes.js")));
        }

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "Dashboards.utilities.js")));

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(CommonScriptsReference.class, "canvg-1.3/rgbcolor.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(CommonScriptsReference.class, "canvg-1.3/StackBlur.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(CommonScriptsReference.class, "canvg-1.3/canvg.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(CommonScriptsReference.class, "html2canvas.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(CommonScriptsReference.class, "jspdf.min.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(CommonScriptsReference.class, "utils.js")));
    }
}
