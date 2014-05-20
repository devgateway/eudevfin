package org.devgateway.eudevfin.reports.ui.components;

import com.googlecode.wickedcharts.highcharts.options.*;
import com.googlecode.wickedcharts.highcharts.options.color.HexColor;
import com.googlecode.wickedcharts.highcharts.options.functions.DefaultFormatter;
import com.googlecode.wickedcharts.highcharts.options.series.Point;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;
import org.apache.log4j.Logger;
import org.devgateway.eudevfin.reports.core.domain.QueryResult;
import org.devgateway.eudevfin.reports.core.service.QueryService;

import java.util.List;

/**
 * @author idobre
 * @since 3/12/14
 */
public class PieChart extends RunMdxQuery {
    private static final Logger logger = Logger.getLogger(PieChart.class);

    private Options options;
    protected String chartId;
    protected QueryResult result;

    public PieChart (QueryService CdaService, String chartId, String dataAccessId) {
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
    public List<Point> getResultSeries () {
        return null;
    }

    private void setDefaultOptions () {
        options.setTitle(new Title(""));

        options.setChartOptions(new ChartOptions()
                .setAnimation(Boolean.TRUE)
                .setBorderColor(new HexColor("#DEEDF7"))
                .setBorderRadius(20)
                .setBorderWidth(2)
                .setHeight(250)
                .setMarginLeft(null)
                .setMarginRight(null)
                .setMarginTop(null)
                .setMarginBottom(null)
                .setPlotShadow(Boolean.FALSE)
                .setType(SeriesType.PIE));

        options.setCreditOptions(new CreditOptions().setEnabled(Boolean.FALSE));

        options.setExporting(new ExportingOptions().setEnabled(Boolean.TRUE));

        options.setLegend(new Legend()
                .setAlign(HorizontalAlignment.RIGHT)
                .setVerticalAlign(VerticalAlignment.TOP)
                .setBackgroundColor(new HexColor("#FCFFC5"))
                .setBorderColor(new HexColor("#C98657"))
                .setEnabled(Boolean.TRUE)
                .setFloating(Boolean.FALSE)
                .setItemStyle(new CssStyle()
                        .setProperty("lineHeight", "14px")
                        .setProperty("width", "200"))
                .setLayout(LegendLayout.VERTICAL)
                .setX(0)
                .setY(50));

        options.setTooltip(new Tooltip()
                .setBorderWidth(1)
                .setStyle(new CssStyle()
                        .setProperty("padding", "10"))
                .setPercentageDecimals(2)
                .setFormatter(new DefaultFormatter().setFunction("var value = sprintf('%.2f', this.y).replace(/,/g, \" \");\n" +
                        "return '<b>' + this.point.name + '</b><br />' + value;"))
                .setShared(Boolean.FALSE)
                .setUseHTML(Boolean.TRUE));

        options.setPlotOptions(new PlotOptionsChoice()
                .setPie(new PlotOptions()
                        .setAllowPointSelect(Boolean.TRUE)
                        .setCursor(Cursor.POINTER)
                        .setAnimation(Boolean.TRUE)
                        .setDataLabels(new DataLabels()
                                .setEnabled(Boolean.TRUE)
                                .setFormatter(new DefaultFormatter().setFunction("var value = sprintf('%.2f', this.y).replace(/,/g, \" \");\n" +
                                        "return value;"))
                                .setColor(new HexColor("#000000"))
                                .setConnectorColor(new HexColor("#000000")))
                        .setShowInLegend(Boolean.TRUE)));
    }

    public Chart getChart() {
        return new Chart(chartId, options);
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }
}
