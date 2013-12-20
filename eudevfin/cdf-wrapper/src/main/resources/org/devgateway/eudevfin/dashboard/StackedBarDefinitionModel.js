// aplication variable
var app = app || {};

// Model that define a stacked bar definition
(function () {
    'use strict';

    var StackedBarDefinitionModel = Backbone.Model.extend({
        defaults: function () {
            return {
                chart: {
                    type: 'bar'
                },
                title: {
                    text: 'Stacked bar chart'
                },
                xAxis: {
                    categories: ['Apples', 'Oranges', 'Pears', 'Grapes', 'Bananas']
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: 'Total fruit consumption'
                    }
                },
                legend: {
                    backgroundColor: '#FFFFFF',
                    reversed: true
                },
                plotOptions: {
                    series: {
                        stacking: 'normal'
                    }
                },
                series: [{
                    name: 'John',
                    data: [5, 3, 4, 7, 2]
                }, {
                    name: 'Jane',
                    data: [2, 2, 3, 2, 1]
                }, {
                    name: 'Joe',
                    data: [3, 4, 4, 2, 5]
                }]
            };
        },

        // y-axis
        //  {
        // top: 300,
        // lineWidth: 0,
        // min: 0,
        // max: 0,
        // gridLineWidth: 0,
        // offset: 0,
        // height: 50,
        // labels: {
        //     enabled: false
        // },
        // title: {
        //     text: 'my title',
        //     align: 'low'
        // }


        // },

        initialize: function () {
            //console.log("Create new Stacked Bar Definition");
        }

    });

    app.StackedBarDefinitionModel = StackedBarDefinitionModel;
}(app));
