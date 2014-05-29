package org.devgateway.eudevfin.reports.ui.components;

import com.googlecode.wickedcharts.highcharts.options.*;
import com.googlecode.wickedcharts.highcharts.options.color.HexColor;
import com.googlecode.wickedcharts.highcharts.options.functions.DefaultFormatter;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;
import org.apache.log4j.Logger;
import org.devgateway.eudevfin.reports.core.domain.QueryResult;
import org.devgateway.eudevfin.reports.core.service.QueryService;

import java.util.List;
import java.util.Map;

/**
 * @author idobre
 * @since 3/12/14
 */
public class StackedBarChart extends RunMdxQuery {
    private static final Logger logger = Logger.getLogger(StackedBarChart.class);

    private Options options;
    protected String chartId;
    protected QueryResult result;

    public StackedBarChart (QueryService CdaService, String chartId, String dataAccessId) {
        super(CdaService);

        this.chartId = chartId;

        options = new Options();
        setDefaultOptions();

        this.setParam("dataAccessId", dataAccessId);
    }

    /**
     * Method that process the MDX result (it's similar with postExecution function from CDF plugin)
     * this method should be Override by each object to configure the desired output
     */
    public Map<String, Float> getResultSeries () {
        return null;
    }

    public List<List<Float>> getResultSeriesAsList () {
        return null;
    }

    private void setDefaultOptions () {
        options.setTitle(new Title(""));

        options.setSubtitle(new Title(""));

        options.setChartOptions(new ChartOptions()
                .setAnimation(Boolean.TRUE)
                .setBorderColor(new HexColor("#DEEDF7"))
                .setBorderRadius(20)
                .setBorderWidth(2)
                .setHeight(300)
                .setMarginLeft(null)
                .setMarginRight(null)
                .setMarginTop(null)
                .setMarginBottom(null)
                .setPlotShadow(Boolean.FALSE)
                .setType(SeriesType.BAR));

        options.setCreditOptions(new CreditOptions().setEnabled(Boolean.FALSE));

        options.setExporting(new ExportingOptions().setEnabled(Boolean.TRUE));

        options.setxAxis(new Axis()
                .setLabels(new Labels()
                        .setRotation(-15)
                        .setAlign(HorizontalAlignment.RIGHT)
                        .setStyle(new CssStyle()
                                .setProperty("fontSize", "13px")
                                .setProperty("fontFamily", "Verdana, sans-serif"))));

        options.setyAxis(new Axis()
                .setMin(0)
                .setTitle(new Title("Amount")));

        options.setTooltip(new Tooltip()
                .setBorderWidth(1)
                .setPercentageDecimals(2)
                .setFormatter(new DefaultFormatter().setFunction("var value = '$ million ' + sprintf('%.2f', this.y).replace(/,/g, \" \");\n" +
                        "return this.point.category + '<br />' + '<b>' + this.point.series.name + '</b>: ' + value;"))
                .setShared(Boolean.FALSE)
                .setUseHTML(Boolean.TRUE));

        options.setLegend(new Legend()
                .setBackgroundColor(new HexColor("#FFFFFF"))
                .setReversed(Boolean.TRUE));

        options.setPlotOptions(new PlotOptionsChoice()
                .setSeries(new PlotOptions()
                        .setStacking(Stacking.NORMAL)
                        .setMinPointLength(5)
                        .setCursor(Cursor.POINTER)));
    }

    public Chart getChart () {
        return new LocalChart(chartId, options);
    }

    public Options getOptions () {
        return options;
    }

    public void setOptions (Options options) {
        this.options = options;
    }
}
