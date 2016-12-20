/* global offset */

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
    var height = 350;
    var numberOfRows = parametersJson.result.resultset.length;
    if (numberOfRows > 4) {
        height += numberOfRows * 50;
    }

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
            .height(height)  
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
        
        chart.legend.dispatch.on('legendClick', function(d, i) {
            nv.utils.windowResize(chart.update);
            setTimeout(function() {
                console.log("Legend Clicked");
                onLegendClick(parametersJson, height);
            }, 100);
        });
        
        onLegendClick(parametersJson, height);

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

var insertLinebreaks = function (d) {
    if (d !== null && d !== undefined) {
        var el = d3.select(this);
        var parent = d3.select(this.parentNode);
        var strExploded = explode(d[0], 80);
        var lines = strExploded.split('\n');
        el.text('');
        for (var i = 0; i < lines.length; i++) {
            var tspan = el.append('tspan').text(lines[i]);
            if (i > 0) {
                tspan.attr('x', 8).attr('dy', '18');
            }
        }
        
        var t = d3.transform(parent.attr("transform"));
        
        parent.attr("transform", "translate(0, " + (t.translate[1] + offset) + ")"); 
        
        var newOffset = (lines.length-1) * 18;
        
        if (newOffset !== 0) {
            offset += newOffset;
        }
    }
}

var onLegendClick = function(parametersJson, height) {
    offset = 0;
    var legendSize = d3.select("#" + parametersJson.id + " svg")
                .selectAll('g.nv-legendWrap g.nvd3.nv-legend g text').size();
        
    if (legendSize > 4) {
        
        // Firefox 1.0+
        var isFirefox = typeof InstallTrigger !== 'undefined';
        
        if (isFirefox) {
            d3.select("#" + parametersJson.id + " svg").style("height", height + "px");
        } else {
            d3.select("#" + parametersJson.id + " svg").style("height", height);
        }

        // split the 
        d3.select("#" + parametersJson.id + " svg")
                .selectAll('g.nv-legendWrap g.nvd3.nv-legend g text').each(insertLinebreaks);

        // change the x coordinate to 0 due to the fact that had negative value and was hidden
        d3.select("#" + parametersJson.id + " svg").select('g.nv-legendWrap g.nvd3.nv-legend g')
                .attr("transform", "translate(0, 5)");
    }
}
