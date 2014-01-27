// aplication variable
var app = app || {};

// Model that define a stacked bar definition
(function () {
    'use strict';

    var StackedBarDefinitionModel = Backbone.Model.extend({
        defaults: function () {
            return {
                chart: {
	                animation: {
		                duration: 10
	                },
	                borderColor: '#DEEDF7',
	                borderRadius: 20,
	                borderWidth: 2,
	                height: 380,
	                marginBottom: null,
	                marginTop: null,
	                plotShadow: false,
                    type: 'bar',
	                renderTo: ''
                },
	            credits: {
		            enabled: false
	            },
	            exporting: {
		            enabled: true
	            },
                title: {
                    text: ''
                },
                xAxis: {
	                labels: {
		                rotation: -15,
		                align: 'right',
		                style: {
			                fontSize: '13px',
			                fontFamily: 'Verdana, sans-serif'
		                }
	                },
                    categories: []
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: 'Amount'
                    },
	                labels: {
		                formatter: function () {
			                return Highcharts.numberFormat(this.value, 0, '.', ' ');
		                }
	                }
                },
                legend: {
                    backgroundColor: '#FFFFFF',
                    reversed: true
                },
                plotOptions: {
                    series: {
                        stacking: 'normal',
	                    //minPointLength: 10,
	                    cursor: 'pointer',
	                    events: {
		                    click: function () {
			                    // implementation of the click event
		                    }
	                    }
                    }
                },
                series: []
            };
        },

        initialize: function () {
            //console.log("Create new Stacked Bar Definition");
        }

    });

    app.StackedBarDefinitionModel = StackedBarDefinitionModel;
}(app));
