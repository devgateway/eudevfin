package org.devgateway.eudevfin.reports.ui.components;

import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.theme.Theme;
import com.googlecode.wickedcharts.wicket6.JavaScriptResourceRegistry;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.devgateway.eudevfin.reports.ui.scripts.Dashboards;

/**
 * Use this class in order to load the Highcharts library from 'local' resources
 * and not use the files from the Web
 *
 * @author idobre
 * @since 5/21/14
 */

public class LocalChart extends Chart {
    private static final String DEFAULT_HIGHCHARTS = "Highcharts-4.0.1/js/highcharts.js";
    private static final String DEFAULT_HIGHCHARTS_MORE = "Highcharts-4.0.1/js/highcharts-more.js";
    private static final String DEFAULT_HIGHCHARTS_EXPORTING = "Highcharts-4.0.1/js/modules/exporting.js";

    public LocalChart(String id, Options options) {
        super(id, options);
        init();
    }

    public LocalChart(String id, Options options, Theme theme) {
        super(id, options, theme);
        init();
    }

    private void init () {
        JavaScriptResourceRegistry.getInstance().setHighchartsReference(new JavaScriptResourceReference(Dashboards.class,
                DEFAULT_HIGHCHARTS));
        JavaScriptResourceRegistry.getInstance().setHighchartsMoreReference(new JavaScriptResourceReference(Dashboards.class,
                DEFAULT_HIGHCHARTS_MORE));
        JavaScriptResourceRegistry.getInstance().setHighchartsExportingReference(new JavaScriptResourceReference(Dashboards.class,
                DEFAULT_HIGHCHARTS_EXPORTING));
    }
}
