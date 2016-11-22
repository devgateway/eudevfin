/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

// aplication variable
var app = app || {};

app.downloadPDF = function () {
    /*
    * find all svg elements in $container
    * $container is the jQuery object of the div that we need to convert to image/pdf.
    * This div may contain highcharts/nvd3 along with other child divs, etc
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

        canvas.setAttribute('width', this.getBoundingClientRect().width);
        canvas.setAttribute('height', this.getBoundingClientRect().height);

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
    
    app.exportTwo();   
}

app.exportTwo = function () {
    var container = $('#printable-container');
    var width = container.width();
    var height = container.height();
    
    var canvasToImage = function(canvas){
        var img = new Image();
        var dataURL = canvas.toDataURL('image/png');
        img.src = dataURL;
        return img;
    };
    var canvasShiftImage = function(oldCanvas,shiftAmt){
        shiftAmt = parseInt(shiftAmt) || 0;
        if(!shiftAmt){ return oldCanvas; }
        
        var newCanvas = document.createElement('canvas');
        newCanvas.height = oldCanvas.height - shiftAmt;
        newCanvas.width = oldCanvas.width;
        var ctx = newCanvas.getContext('2d');
        
        var img = canvasToImage(oldCanvas);
        ctx.drawImage(img,0, shiftAmt, img.width, img.height, 0, 0, img.width, img.height);
        
        return newCanvas;
    };
    
    var canvasToImageSuccess = function(canvas){
        var pdf = new jsPDF('l','px'),
            pdfInternals = pdf.internal,
            pdfPageSize = pdfInternals.pageSize,
            pdfScaleFactor = pdfInternals.scaleFactor,
            pdfPageWidth = pdfPageSize.width,
            pdfPageHeight = pdfPageSize.height,
            totalPdfHeight = 0,
            htmlPageHeight = canvas.height,
            htmlScaleFactor = canvas.width / (pdfPageWidth * pdfScaleFactor),
            safetyNet = 0;
        
        while(totalPdfHeight < htmlPageHeight && safetyNet < 15){
            var newCanvas = canvasShiftImage(canvas, totalPdfHeight);
            pdf.addImage(newCanvas, 'png', 0, 0, pdfPageWidth, 0, null, 'NONE');
            
            totalPdfHeight += (pdfPageHeight * pdfScaleFactor * htmlScaleFactor);
            
            if(totalPdfHeight < htmlPageHeight){
                pdf.addPage();
            }
            safetyNet++;
        }
        
        pdf.save('Download.pdf');
    };

    html2canvas(container, {
        width: width,
        height: height,
        onrendered: function(canvas){
            canvasToImageSuccess(canvas);
            container.find('.screenShotTempCanvas').remove();
            container.find('svg').show();
        }
    });
}

$(document).ready(function () {
    $('#print-page').click(function() {
        window.print();
    });

    $('#download-page').click(function() {
        $('html, body').scrollTop(0);
        app.downloadPDF();
    });
});


