// aplication variable
var app = app || {};

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
