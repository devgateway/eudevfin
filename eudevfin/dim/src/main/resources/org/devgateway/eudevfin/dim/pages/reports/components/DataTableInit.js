/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

function initDataTable2(tableId, dataAccessId) {
    var tableDefinition = new app.TableDefinitionModel({
        colHeaders: ["Col 1", "Col 2", "Amount"],
        colTypes: ['numeric', 'string', 'numeric'],
        colFormats: ['%d', '%s', '%d'],
        colWidths: ['30%', '40', '30%'],
        colSortable: [true, true, true],
        // sortBy: [[2, 'desc']],	for now the sort on CDA is not working
        paginate: true,
        filter: true,
        info: true,
        dataAccessId: "simpleSQLQuery"
    });
    var table = new app.TableModel({
        name: tableId,
        listeners: ['sectorListParameter'],
        parameters: ['sectorListParameter'],
        chartDefinition: tableDefinition.toJSON(),
        htmlObject: tableId,
        executeAtStart: true,
        preChange: function (value) {
            return value;
        },
        postFetch: function (values) {
            console.log(values);
            return values;
        },
        preExecution: function () {
            return undefined;
        },
        postExecution: function () {
        }
    });

    Dashboards.addComponent(table.toJSON());
}


function initDataTable(parametersJson) {
    debugger;
    var table = new app.TableModel(parametersJson/*$.extend(parametersJson, {
     preChange: function (value) {
     return value;
     },
     postFetch: function (values) {
     console.log(values);
     return values;
     },
     preExecution: function () {
     return undefined;
     },
     postExecution: function () {
     }
     })*/);

    Dashboards.addComponent(table.toJSON());
}


/*name: tableId,
 listeners: ['sectorListParameter'],
 parameters: [['xxx', 'sectorListParameter']],
 htmlObject: tableId,
 executeAtStart: true,
 chartDefinition: tableDefinition.toJSON(),
 */
