// aplication variable
var app = app || {};

// Model that define a filter
(function () {
    'use strict';

    var FilterModel = Backbone.Model.extend({
        defaults: function () {
            return {
                name: "",
                type: "select",
                parameters: [],
                parameter: "",
                valueAsId: true,
                htmlObject: "",
                queryDefinition: {
                    dataAccessId: "",
                    path: '/some/path'
                },
                executeAtStart: true,
                preExecution: function () {},
                postExecution: function () {}
            };
        },

        initialize: function () {
            //console.log("Create new Filter");
        }
    });

    app.FilterModel = FilterModel;
}(app));
