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
            doc.save('Download file.pdf');

            // After your image is generated revert the temporary changes
            container.find('.screenShotTempCanvas').remove();
            container.find('svg').show();
        },
        allowTaint: true,
        useCORS: true
    });
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


