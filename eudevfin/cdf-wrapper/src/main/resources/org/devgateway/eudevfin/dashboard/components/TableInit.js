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
	        // this is just for the demo, for the actual dashboard page we should change this behavior
	        var newResultSet = [];
	        var len = values.resultset.length;
	        for (var i = 0; i < len; i++){
		        newResultSet.push(values.resultset[i].splice(2, 6));
	        }

	        values.resultset = newResultSet;
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