/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
var myColors = ["#4572A7", "#AA4643", "#89A54E", "#80699B", "#3D96AE", "#DB843D", "#92A8CD", "#A47D7C", "#B5CA92", "#DDDF00", "#ED561B", "#6AF9C4"];

var displayPieChart = function (parametersJson) {
    'use strict';

    d3.scale.myColors = function() {
        return d3.scale.ordinal().range(myColors);
    };

    // display the pie chart
    nv.addGraph(function () {
        var chart = nv.models.pieChart()
            .x(function (data) {
                return data[0];
            })
            .y(function (data) {
                return data[1];
            })
            .tooltipContent(function (key, x, y, e, graph) {
                return '<h3 class="smaller">' + key + '</h3>' + '<p>' +  x + '</p>'
            })
            .showLabels(true)       // Display pie labels
            .showLegend(true)       // Display the legend
            .labelThreshold(.01)    // Configure the minimum slice size for labels to show up
            .labelType("percent")   // Configure what type of data to show in the label. Can be "key", "value" or "percent"
            .tooltips(true)         // Show tooltips on hover.
            .color(d3.scale.myColors().range());

        d3.select("#" + parametersJson.id + " svg")
            .datum(formatPieResultSet(parametersJson.result.resultset, parametersJson.useMillion))
            .transition().duration(500)
            .call(chart);

        nv.utils.windowResize(chart.update);

        return chart;
    });
}

/**
 * format the results for the nvd3 pie chart
 */
var formatPieResultSet = function (result, useMillion) {
    var i;

    for (i = 0; i < result.length; i++) {
        if (useMillion !== undefined && useMillion === true) {
            result[i][1] = parseFloat(result[i][1], 10) / 1000000;
        } else {
            result[i][1] = parseFloat(result[i][1], 10);
        }
    }

    return result;
}
