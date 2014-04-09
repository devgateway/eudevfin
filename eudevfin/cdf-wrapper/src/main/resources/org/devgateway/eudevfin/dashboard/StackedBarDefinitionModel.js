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
		                duration: 1000
	                },
	                borderColor: '#DEEDF7',
	                borderRadius: 20,
	                borderWidth: 2,
	                height: 350,
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
                        enabled: false,
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
                tooltip: {
                    backgroundColor: {
                        linearGradient: [0, 0, 0, 60],
                        stops: [
                            [0, '#FFFFFF'],
                            [1, '#E0E0E0']
                        ]
                    },
                    borderWidth: 1,

                    formatter: function () {
                        var value = '$ ' + sprintf('%d', this.y).replace(/,/g, " ");

                        return value;
                    },

                    percentageDecimals: 2,
                    shared: false,
                    useHTML: true
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
