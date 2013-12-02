// aplication variable
var app = app || {};

// Model that define a table
(function () {
      'use strict';
      
      var TableModel = Backbone.Model.extend ({
         defaults: function () {
            return {
                name: "",
                type: "tableComponent",
                listeners:[],
                parameters: [],
                chartDefinition: null,
                htmlObject: "",
                executeAtStart: true,
                preExecution: function () {},
                postExecution: function () {}
            };
        },

        initialize: function() {
            //console.log("Create new Table");
        }
    });

    app.TableModel = TableModel;
}(app));
