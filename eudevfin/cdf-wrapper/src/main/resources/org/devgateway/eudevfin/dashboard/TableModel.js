/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

// aplication variable
var app = app || {};

// Model that define a table
(function () {
    'use strict';

    var TableModel = Backbone.Model.extend({
        defaults: function () {
            return {
                name: "",
                type: "tableComponent",
                listeners: [],
                parameters: [],
                chartDefinition: null,
                htmlObject: "",
                executeAtStart: true,
                preExecution: function () {},
                postExecution: function () {}
            };
        },

        initialize: function () {
            //console.log("Create new Table");
        }
    });

    app.TableModel = TableModel;
}(app));
