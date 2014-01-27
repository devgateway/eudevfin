// aplication variable
var app = app || {};

// Model that define a chart pie definition
(function () {
    'use strict';

    var ChartPieDefinitionModel = Backbone.Model.extend({
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
                    marginLeft: null,
                    marginTop: null,
                    plotShadow: false,
                    type: 'pie',
                    renderTo: ''
                },
                credits: {
                    enabled: false
                },
                exporting: {
                    enabled: true
                },
                legend: {
                    align: 'right',
                    verticalAlign: 'top',
                    backgroundColor: '#FCFFC5',
                    borderColor: '#C98657',
                    enabled: true,
                    floating: false,
                    padding: 3,
                    itemMarginTop: 5,
                    itemMarginBottom: 5,
                    itemStyle: {
                        lineHeight: '14px',
                        width: 450
                    },
                    labelFormatter: function () {
                        return this.name;
                    },
                    layout: 'vertical',
                    navigation: {
                        activeColor: '#3E576F',
                        animation: true,
                        arrowSize: 12,
                        inactiveColor: '#CCC',
                        style: {
                            fontWeight: 'bold',
                            color: '#333',
                            fontSize: '12px'
                        }
                    },
                    x: 0,
                    y: 50
                },
                title: {
                    text: ''
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
                    style: {
                        padding: 10
                    },

                    formatter: function () {
                        var value = '€' + sprintf('%d', this.y).replace(/,/g, " ");

                        return '<b>' + this.point.name + '</b><br />' + value; // + ' - ' + sprintf('%.2f', this.percentage) + '%';
                    },

                    percentageDecimals: 2,
                    shared: false,
                    useHTML: true
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        animation: {
                            duration: 10
                        },
                        dataLabels: {
                            enabled: false,
                            color: '#000000',
                            connectorColor: '#000000',
                            formatter: function () {
                                return '<b>' + this.point.name + '</b>: ' + sprintf('%.2f', this.percentage) + ' %';
                            }
                        },
                        showInLegend: true
                    },
                    series: {
                        cursor: 'pointer',
                        events: {
                            click: function () {
                                // implementation of the click event
                            }
                        }
                    }
                },
                series: [{
                    animation: {
                        duration: 10
                    },
                    type: 'pie',
                    name: '',
                    data: []
                }]
            };
        },

        initialize: function () {
            //console.log("Create new Chart Pie Definition");
        }

    });

    app.ChartPieDefinitionModel = ChartPieDefinitionModel;
}(app));
