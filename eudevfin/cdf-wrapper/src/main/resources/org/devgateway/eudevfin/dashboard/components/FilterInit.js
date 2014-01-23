/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

// TODO: onChange event is not working!
var sectorListParameter,
    organizationListParameter,
	biMultilateralListParameter;

function initFilter(parametersJson) {
    var filter = new app.FilterModel(_.extend(parametersJson, {
        postFetch: function (values) {
	        // this is just for the demo, for the actual dashboard page we should change this behavior
	        var newResultSet = [];
	        var len = values.resultset.length;
	        for (var i = 0; i < len; i++) {
		        if (values.resultset[i][2] !== null && values.resultset[i][2] !== '#null') {
		            newResultSet.push([values.resultset[i][2]]);
		        }
	        }

	        values.resultset = newResultSet;

            return values;
        },
        preExecution: function () {
            return undefined;
        },
        postExecution: function () {}
    }));

    filter = filter.toJSON();

    Dashboards.addComponent(filter);

    Dashboards.update(filter);
}
