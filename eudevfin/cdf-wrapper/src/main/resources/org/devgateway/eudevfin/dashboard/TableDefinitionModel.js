/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

// aplication variable
var app = app || {};

// Model that define a table definition
(function () {
    'use strict';

    var TableDefinitionModel = Backbone.Model.extend({
        defaults: function () {
            return {
                colHeaders: ["Col 1", "Col 2", "Col 3"],
                colTypes: ['string', 'numeric', 'numeric'],
                colFormats: ['%s', '%d', '%d'],
                colWidths: ['40%', '40%', '20%'],
                colSortable: [true, true, true],
                tableStyle: "themeroller",
                sortBy: [[1, 'desc']],
                paginationType: "full_numbers",
                paginateServerside: false,
                paginate: false,
                filter: false,
                info: false,
                sort: true,
                lengthChange: false,
                displayLength: 10,
                sDom: 'T<"clear">lfrtip',
                oTableTools: {
                    sSwfPath: 'js/tableTools/swf/copy_csv_xls_pdf.swf'
                },

                // query properties
                dataAccessId: "",
                path: '/some/path'
            };
        },

        initialize: function () {
            //console.log("Create new Table Definition");
        }
    });

    app.TableDefinitionModel = TableDefinitionModel;
}(app));
