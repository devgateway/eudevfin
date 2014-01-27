// aplication variable
var app = app || {};

// Model that define a chart column definition
(function () {
    'use strict';

    var ChartColumnDefinitionModel = Backbone.Model.extend({
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
                    type: 'column',
                    renderTo: ''
                },
                credits: {
                    enabled: false
                },
                exporting: {
                    enabled: true
                },
                xAxis: {
                    labels: {
                        rotation: -45,
                        align: 'right',
                        style: {
                            fontSize: '13px',
                            fontFamily: 'Verdana, sans-serif'
                        }
                    }
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
                    enabled: false
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

                    formatter: function () {
                        var value = '€' + sprintf('%d', this.y).replace(/,/g, " ");
                        return '<b>' + this.point.name + '</b> - ' + value;
                    },

                    percentageDecimals: 2,
                    shared: false,
                    useHTML: true
                },
                plotOptions: {
                    column: {
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
                        minPointLength: 10,
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
                    type: 'column',
                    name: '',
                    pointWidth: 85,
                    data: []
                }]
            };
        },

        initialize: function () {
            //console.log("Create new Chart Column Definition");
        }

    });

    app.ChartColumnDefinitionModel = ChartColumnDefinitionModel;
}(app));
