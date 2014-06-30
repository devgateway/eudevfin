var myBarColors = ["#3D96AE", "#AA4643"];

var displayBarChart = function (parametersJson) {
    'use strict';

    checkBarResults(parametersJson.result, parametersJson.numberOfSeries);

    var numberOfRows = parametersJson.result.resultset.length;
    var height = 300 + numberOfRows * 35;

    // display the bar chart
    nv.addGraph(function () {
        var chart = nv.models.multiBarHorizontalChart()
            .x(function (data) {
                return data[0];
            })
            .y(function (data) {
                return data[1];
            })
            .tooltipContent(function (key, x, y, e, graph) {
                return '<h3 class="smaller">' + key + ' - ' + x + '</h3>' + '<p>' +  y + '</p>';
            })
            .height(height)             // set the height
            .margin({left: 100})
            .showValues(true)           // Show bar value next to each bar.
            .tooltips(true)             // Show tooltips on hover.
            .showLegend(true)
            .transitionDuration(500)
            .showControls(false);       // Allow user to switch between "Grouped" and "Stacked" mode.

        chart.yAxis
            .axisLabel('Amount')
            .tickFormat(d3.format(',.2f'));

        d3.select("#" + parametersJson.id + " svg")
            .datum(formatBarResultSet(parametersJson.result.resultset, parametersJson.numberOfSeries,
                parametersJson.Series1, parametersJson.Series2))
            .transition().duration(500)
            .call(chart);

        // update the height of the svg
        $("#" + parametersJson.id + " svg").height(height);

        nv.utils.windowResize(chart.update);

        return chart;
    });
}

/**
 * format the results for the nvd3 bar chart
 */
var formatBarResultSet = function (result, numberOfSeries, Series1, Series2) {
    var i,
        data;

    if(numberOfSeries === 1) {
        data = [{
            "key": Series1,
            "color": "#3D96AE",
            "values": []
            }
        ];
    } else {
        if(numberOfSeries === 2) {
            data = [{
                "key": Series1,
                "color": "#3D96AE",
                "values": []
                },
                {
                    "key": Series2,
                    "color": "#AA4643",
                    "values": []
                }
            ];
        }
    }

    for (i = 0; i < result.length; i++) {
        // add data for the first series
        data[0].values.push([result[i][0], parseFloat(result[i][1], 10)]);

        if(numberOfSeries == 2) {
            data[1].values.push([result[i][0], parseFloat(result[i][2], 10)]);
        }
    }

    return data;
}

var checkBarResults = function (result, numberOfSeries) {
    var i;

    if(numberOfSeries === 1) {
        for (i = 0; i < result.resultset.length; i++) {
            if (result.metadata[1].colName.toLowerCase() === "FIRST YEAR".toLowerCase()) {
                if (result.resultset[i].length > 1 && result.resultset[i][1] === null) {
                    result.resultset[i][1] = 0;
                }
            }
        }
    } else {
        if (numberOfSeries === 2) {
            for (i = 0; i < result.resultset.length; i++) {
                // check if we have data for both years
                if (result.metadata[1].colName.toLowerCase() === "First Year".toLowerCase()) {
                    if (result.resultset[i].length > 1 && result.resultset[i][1] === null) {
                        result.resultset[i][1] = 0;
                    }
                    if (result.resultset[i].length > 2 && result.resultset[i][2] === null) {
                        result.resultset[i][2] = 0;
                    }
                } else {
                    // we don't have the first year data
                    result.resultset[i].splice(1, 0, 0);
                }
            }
        }
    }
}
