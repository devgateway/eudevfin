/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

// aplication variable
var app = app || {};

app.downloadPDF = function () {
    /*
    * find all svg elements in $container
    * $container is the jQuery object of the div that we need to convert to image/pdf.
    * This div may contain highcharts along with other child divs, etc
    */
    var container = $('#printable-container');

    // add the page title in case it exists
    if ($('.transaction-title').length > 0) {
        var pageTitle = $('.transaction-title').parent();
        container.prepend(pageTitle);
    }

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
    $('#print-page').click(function() {
        window.print();
    });

    $('#download-page').click(function() {
        app.downloadPDF();
    });
});


