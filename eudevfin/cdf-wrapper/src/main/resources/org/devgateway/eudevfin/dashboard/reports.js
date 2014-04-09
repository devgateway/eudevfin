/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

// aplication variable
var app = app || {};

app.downloadPDFOdaAtGlance = function () {
    /*
     * find all svg elements in $container
     * $container is the jQuery object of the div that we need to convert to image/pdf.
     * This div may contain highcharts along with other child divs, etc
     */
    var container = $('#reports-page');
    var svgElements = container.find('svg');

    // replace all svgs with a temp canvas
    svgElements.each(function () {
        this.className = 'tempHide';
        var canvas, xml;

        canvas = document.createElement('canvas');
        canvas.className = 'screenShotTempCanvas';
        // convert SVG into a XML string
        xml = (new XMLSerializer()).serializeToString(this);

        // Removing the name space as IE throws an error
        xml = xml.replace(/xmlns=\"http:\/\/www\.w3\.org\/2000\/svg\"/, '');

        // draw the SVG onto a canvas
        canvg(canvas, xml);
        $(canvas).insertAfter(this);

        // hide the SVG element
        $(this).hide();
    });

    html2canvas(container, {
        onrendered: function(canvas) {
            // document.body.appendChild(canvas);
            var doc = new jsPDF();

            var pdfWidth = 220;
            var pdfHeight = pdfWidth * (container.height() / container.width());
            doc.addImage(canvas.toDataURL('image/jpeg'), 'JPEG', 0, 20, pdfWidth, pdfHeight);
            doc.save('Oda at Glance.pdf');

            // After your image is generated revert the temporary changes
            container.find('.screenShotTempCanvas').remove();
            container.find('svg').show();
        },
        allowTaint: true,
        useCORS: true
    });
}

// function to check if an array has all the elements equal to a value
app.checkArrayValue = function (array, value){
	var i;

	if(array.length > 0) {
		for(i = 0; i < array.length; i++) {
			if (array[i] !== value) {
				return false;
			}
		}
	}

	return true;
}

// format the Amount column from tables (remove the comma)
app.tableValueFormat = function (v, st) {
	if (v >= 1000) {
		return sprintf(st.colFormat, v / 1000).replace(/,/g, " ") + " 000";
	} else {
		if (v !== 0) {
			return v;
		} else {
			return '';
		}
	}
}

app.noData = function () {
	$('.no-data').show(1000);
}

$(document).ready(function () {
    // add more colors for Highcharts
    Highcharts.setOptions({
        colors: ["#4572A7", "#AA4643", "#89A54E", "#80699B", "#3D96AE", "#DB843D", "#92A8CD", "#A47D7C", "#B5CA92", "#DDDF00", "#ED561B", "#6AF9C4"]
    });

    Highcharts.getOptions().colors = $.map(Highcharts.getOptions().colors, function (color) {
        return {
            radialGradient: {
                cx: 0.5,
                cy: 0.3,
                r: 0.7
            },
            stops: [
                [0, color],
                [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
            ]
        };
    });

    $('#print-page').click(function() {
        window.print();
    });

    $('#download-page').click(function() {
        app.downloadPDFOdaAtGlance();
    });
});

/**
 * Create a global getSVG method that takes an array of charts as an argument
 */
Highcharts.getSVG = function(charts) {
    var svgArr = [],
        top = 0,
        width = 0;

    $.each(charts, function(i, chart) {
        var svg = chart.getSVG();
        svg = svg.replace('<svg', '<g transform="translate(0,' + top + ')" ');
        svg = svg.replace('</svg>', '</g>');

        top += chart.chartHeight;
        width = Math.max(width, chart.chartWidth);

        svgArr.push(svg);
    });

    return '<svg height="'+ top +'" width="' + width + '" version="1.1" xmlns="http://www.w3.org/2000/svg">' + svgArr.join('') + '</svg>';
};

/**
 * Create a global exportCharts method that takes an array of charts as an argument,
 * and exporting options as the second argument
 */
Highcharts.exportCharts = function(charts, options) {
    var form
        svg = Highcharts.getSVG(charts);

    // merge the options
    options = Highcharts.merge(Highcharts.getOptions().exporting, options);

    // create the form
    form = Highcharts.createElement('form', {
        method: 'post',
        action: options.url
    }, {
        display: 'none'
    }, document.body);

    // add the values
    Highcharts.each(['filename', 'type', 'width', 'svg'], function(name) {
        Highcharts.createElement('input', {
            type: 'hidden',
            name: name,
            value: {
                filename: options.filename || 'chart',
                type: options.type,
                width: options.width,
                svg: svg
            }[name]
        }, null, form);
    });
    
    // submit
    form.submit();

    // clean up
    form.parentNode.removeChild(form);
};

// The components to be loaded into the dashboard within the [] separated by ,
app.components = [];

app.addComponent = function (component) {
	this.components.push(component);
}

// The initial dashboard load function definition
app.load = function () {
	// don't display too many logs
	Dashboards.setLogLifecycle(false);
    Dashboards.init(this.components);
};

