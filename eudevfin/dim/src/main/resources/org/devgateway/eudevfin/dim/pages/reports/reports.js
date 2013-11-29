// aplication variable
var app = {}

$(document).ready(function () {
    // add more colors for Highcharts
    Highcharts.setOptions({
        colors: ["#4572A7", "#AA4643", "#89A54E", "#80699B", "#3D96AE", "#DB843D", "#92A8CD", "#A47D7C", "#B5CA92", "#DDDF00", "#ED561B", "#6AF9C4"]
    });

    Highcharts.getOptions().colors = $.map(Highcharts.getOptions().colors, function (color) {
        return {
            radialGradient: { cx: 0.5, cy: 0.3, r: 0.7 },
            stops: [
                [0, color],
                [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
            ]
        };
    });
});

// Model that define a chart
(function () {
    'use strict';
    
    var ChartModel = Backbone.Model.extend ({
        defaults: function () {
            return {
                name: "",
                type: "queryComponent",
                listeners:[],
                parameters: [],
                resultvar: "",
                executeAtStart: true,
                queryDefinition: {
                    dataAccessId:  "",
                    path: '/some/path'
                },
                preExecution: function () {},
                postExecution: function () {}
            };
        },

        initialize: function() {
            //console.log("Create new Chart");
        }

    });

    app.ChartModel = ChartModel;
}(app));

// Model that define a chart pie definition
(function () {
    'use strict';
    
    var ChartPieDefinitionModel = Backbone.Model.extend ({
        defaults: function () {
            return {
                chart: {
                    animation: {
                        duration: 1000
                    },
                    borderColor: '#EBBA95',
                    borderRadius: 20,
                    borderWidth: 2,
                    height: 380,
                    //width: 960,                   // don't specify the widht for responsive design
                    marginBottom: null,
                    marginLeft: null,
                    //marginRight: 400,             // don't specify the marginRight for responsive design
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

                        return '<b>' + this.point.name + '</b><br />' + value;  // + ' - ' + sprintf('%.2f', this.percentage) + '%';
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
                            duration: 1000
                        },
                        dataLabels: {
                            enabled: false,
                            color: '#000000',
                            connectorColor: '#000000',
                            formatter: function () {
                                return '<b>'+ this.point.name +'</b>: '+ sprintf('%.2f', this.percentage) +' %';
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
                        duration: 1000
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



// Model that define a chart column definition
(function () {
    'use strict';
    
    var ChartColumnDefinitionModel = Backbone.Model.extend ({
        defaults: function () {
            return {
                chart: {
                    animation: {
                        duration: 1000
                    },
                    borderColor: '#EBBA95',
                    borderRadius: 20,
                    borderWidth: 2,
                    height: 380,
                    //width: 960,                   // don't specify the widht for responsive design
                    marginBottom: null,
                    //marginLeft: 150,
                    //marginRight: 100,
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
                        text: 'Contracts Value'
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
                colors: ['#ff0000', '#ff6600', '#ffcc00'],
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
                            duration: 1000
                        },
                        dataLabels: {
                            enabled: false,
                            color: '#000000',
                            connectorColor: '#000000',
                            formatter: function () {
                                return '<b>'+ this.point.name +'</b>: '+ sprintf('%.2f', this.percentage) +' %';
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
                        duration: 1000
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

var pathToCdaFile = '/some/path.cda';
var testTableDefinition,
    testTable,
    pieChart,
    columnChart,
    pieChartDefinition,
    columnsChartDefinition,
    pieChartQuery,
    pieQueryResult,
    columnQueryResult,
    columnChartQuery;

testTableDefinition = {
    colHeaders: ["Col 1", "Col 2", "Amount"],
    colTypes: ['numeric', 'string', 'numeric'],
    colFormats: ['%d', '%s', '%d'],
    colWidths: ['30%', '40', '30%'],
    colSortable: [true, true, true],
    // sortBy: [[2, 'desc']],	for now the sort on CDA is not working 
    tableStyle: "themeroller",
    paginationType: "full_numbers",
    paginateServerside: false,
    paginate: true,
    filter: true,
    info: true,
    sort: true,
    lengthChange: false,
    displayLength: 5,
    // query properties
    path: pathToCdaFile,                 // I don't think we will need this property
    dataAccessId: "simpleSQLQuery"
};

testTable = {
    name: "testTable",
    type: "tableComponent",
    listeners:[],
    parameters: [],
    chartDefinition: testTableDefinition,
    htmlObject: "test-table",
    executeAtStart: true,
    preChange: function (value) {
        // console.log('>>> preChange');

        return value;
    },
    postFetch: function (values) {
        // console.log('>>> postFetch');

        return values;
    },
    preExecution: function () {
        // console.log('>>> preExecution');

        return undefined;
    },
    postExecution: function () {
        // console.log('>>> postExecution');
    }
};

pieChartDefinition = new app.ChartPieDefinitionModel();
pieChartDefinition.get('chart').renderTo = 'pie-chart';
pieChartDefinition.get('plotOptions').series.events.click = function (event) {
                    setTimeout(function() {
                        console.log('pie chart clicked!');
                    }, 500);
                };

columnsChartDefinition = new app.ChartColumnDefinitionModel();
columnsChartDefinition.get('chart').renderTo = 'column-chart';
columnsChartDefinition.get('series')[0].pointWidth = 115;
columnsChartDefinition.get('xAxis').labels.rotation = -15;
columnsChartDefinition.get('plotOptions').series.events.click = function (event) {
                    setTimeout(function() {
                        console.log('column chart clicked!');
                    }, 200);
                };

pieChartQuery = new app.ChartModel ({
    name: "pieChartQuery",
    parameters: [],
    resultvar: "pieQueryResult",
    queryDefinition: {
        dataAccessId: "pieChartQuery", 
        path: '/some/path'
    },
    executeAtStart: true,
    preExecution: function () {
        // do nothing
    },
    postExecution: function () {
        var resultSeries = [];
        for(var i = 0; i < pieQueryResult.length && i < 10; i++) {
            // filter the results
            pieQueryResult[i].splice(0, 1);
            // we need to do this because Highcharts needs 'int' instead of 'string' :(
            // pieQueryResult[i][1] = parseInt(pieQueryResult[i][1], 10);
            pieQueryResult[i][1] = Math.floor(Math.random() * 1000);

            resultSeries.push(pieQueryResult[i]);
        }

        pieChartDefinition.get('series')[0].data = resultSeries;
        pieChart = new Highcharts.Chart(pieChartDefinition.toJSON());
    }
});

columnChartQuery = new app.ChartModel ({
    name: "columnChartQuery",
    parameters: [],
    resultvar: "columnQueryResult",
    queryDefinition: {
        dataAccessId: "columnChartQuery", 
        path: '/some/path'
    },
    executeAtStart: true,
    preExecution: function () {
        // do nothing
    },
    postExecution: function () {
        var resultCategories = [],
            resultSeries     = [],
            colors           = Highcharts.getOptions().colors,
            len              = Highcharts.getOptions().colors.length,
            i;

        for (i = 0; i < columnQueryResult.length && i < 10; i++) {
            if (parseInt(columnQueryResult[i][2], 10) > 0) {
                resultCategories.push(columnQueryResult[i][1]);
                resultSeries.push({
                    name: columnQueryResult[i][1], 
                    y: Math.floor(Math.random() * 1000),
                    // y: parseInt(columnQueryResult[i][2], 10), 
                    color: colors[i % len]      // access colors array in a circular manner
                });
            }
        }

        if (resultCategories.length > 0) {
            columnsChartDefinition.get('series')[0].data = resultSeries;
            columnsChartDefinition.get('xAxis').categories = resultCategories;
            columnChart = new Highcharts.Chart(columnsChartDefinition.toJSON());
        }
    }
});

// get the charts as JSON objects
pieChartQuery = pieChartQuery.toJSON();
columnChartQuery = columnChartQuery.toJSON();


// The components to be loaded into the dashboard within the [] separated by ,
var components = [testTable, pieChartQuery, columnChartQuery];

// The initial dashboard load function definition
var load = function () {
    Dashboards.init(components);
};

// The initial dashboard load function execution
load();