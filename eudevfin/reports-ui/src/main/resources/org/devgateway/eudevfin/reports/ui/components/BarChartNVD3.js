/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
var myBarColors = ["#3D96AE", "#AA4643"];

var displayBarChart = function (parametersJson) {
    'use strict';

    checkBarResults(parametersJson.result, parametersJson.numberOfSeries);

    var numberOfRows = parametersJson.result.resultset.length;
    var height = 300 + numberOfRows * 40;

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
            .margin({left: 120})
            .showValues(true)           // Show bar value next to each bar.
            .tooltips(true)             // Show tooltips on hover.
            .showLegend(true)
            .transitionDuration(500)
            .showControls(false);       // Allow user to switch between "Grouped" and "Stacked" mode.

        chart.yAxis
            .axisLabel('Amount')
            .tickFormat(d3.format(',.2f'));

        //chart.xAxis.tickFormat(function(d) {
        //    //return "new string";
        //});

        d3.select("#" + parametersJson.id + " svg")
            .datum(formatBarResultSet(parametersJson.result.resultset, parametersJson.numberOfSeries,
                parametersJson.Series1, parametersJson.Series2))
            .transition().duration(500)
            .call(chart);

        // update the height of the svg
        $("#" + parametersJson.id + " svg").height(height);

        nv.utils.windowResize(chart.update);

        // used this in order to include newlines in labels in NVD3 charts
        d3.select("#" + parametersJson.id + " svg").
            selectAll('g.nv-x.nv-axis g text').each(insertLinebreaks);

        return chart;
    });
}

/**
 * function that splits a text in rows of max length and with word preservation
 */
var explode = function (text, max) {
    var max = max || 50;

    // clean the text
    // text = text.replace(/  +/g, " ").replace(/^ /, "").replace(/ $/, "");

    if (text === undefined) {
        return "";
    }

    if (text.length <= max) {
        return text;
    }

    // get the first part of the text
    var exploded = text.substring(0, max);

    // get the next part of the text
    text = text.substring(max);

    // if next part doesn't start with a space
    if (text.charAt(0) !== ' ') {
        // while the first part doesn't end with a space && the first part still has at least one char
        while (exploded.charAt(exploded.length - 1) !== " " && exploded.length > 0) {
            text = exploded.charAt(exploded.length - 1) + text;
            exploded = exploded.substring(0, exploded.length - 1);
        }
        // if the first part has been emptied (case of a text bigger than max without any space)
        if (exploded.length == 0) {
            // re-explode the text without caring about spaces
            exploded = text.substring(0, max);
            text = text.substring(max);
            // if the first part isn't empty
        } else {
            // remove the last char of the first part, because it's a space
            exploded = exploded.substring(0, exploded.length - 1);
        }
        // if the next part starts with a space
    } else {
        // remove the first char of the next part
        text = text.substring(1);
    }

    // return the first part and the exploded next part, concatenated by \n
    return exploded + "\n" + explode(text, max);
}

/**
 * function that insert line breaks in NVD3 label chartss
 */
var insertLinebreaks = function (d) {
    if (d !== null && d !== undefined) {
        var el = d3.select(this);
        var lines = explode(d, 20).split('\n');
        el.text('');

        for (var i = 0; i < lines.length; i++) {
            var tspan = el.append('tspan').text(lines[i]);
            if (i > 0) {
                tspan.attr('x', -5).attr('dy', '15');
            }
        }
    }
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
