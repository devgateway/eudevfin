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
                    categories: ['ODA equity investment', 'ODA Grant', 'ODA Loan', 'ODA Grant-like']
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
                    name: 'Organization 1',
                    data: [5, 3, 4, 7]
                }, {
                    name: 'Organization 1',
                    data: [2, 2, 3, 2]
                }, {
                    name: 'Organization 1',
                    data: [3, 4, 4, 2]
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
