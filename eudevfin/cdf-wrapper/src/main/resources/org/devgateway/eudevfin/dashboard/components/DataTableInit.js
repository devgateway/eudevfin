/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

function initDataTable(parametersJson) {    
    var table = new app.TableModel(_.extend(parametersJson, {
        preChange: function (value) {
            return value;
        },

        postFetch: function (values) {
            //console.log(values);
            return values;
        },

        preExecution: function () {
            return undefined;
        },

        postExecution: function () {}
    }));

    table = table.toJSON();

    Dashboards.addComponent(table);

    Dashboards.update(table);
}
