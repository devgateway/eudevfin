/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

function addNetODATable (parametersJson) {
	'use strict';

    var table = new app.TableModel(_.extend(parametersJson, {
        preChange: function (value) {
            return value;
        },

        postFetch: function (values) {
	        var i, j , k,
		        len = values.resultset.length;

			// set 0 to 'null' values
	        for(i = 0; i < len; i++) {
		        k = values.resultset[i].length;
		        for(j = 1; j < k; j++) {
					if (values.resultset[i][j] === null || values.resultset[i][j] === undefined) {
						values.resultset[i][j] = 0;
					}
		        }
	        }

	        for(i = 0; i < len - 2; i++) {
		        k = values.resultset[i].length;
		        // add (last_year - 1 / last_year) column
		        values.resultset[i][k] = values.resultset[i][k - 2] / values.resultset[i][k - 1];
	        }

	        for(i = len - 2; i < len; i++) {
		        k = values.resultset[i].length;
		        // add (last_year - 1 / last_year) column
		        values.resultset[i][k] = 0;
	        }

	        // add colIndex, colName and colType metadata
	        values.metadata.push({
		        colIndex: 4,
		        colName: "percent",
		        colType: "Numeric"
	        });

            return values;
        },

        preExecution: function () {
            return undefined;
        },

        postExecution: function () {}
    }));

    table = table.toJSON();
	app.addComponent(table);
}

function addTopTenRecipients (parametersJson) {
	'use strict';

	var table = new app.TableModel(_.extend(parametersJson, {
		preChange: function (value) {
			return value;
		},

		postFetch: function (values) {
			return values;
		},

		preExecution: function () {
			return undefined;
		},

		postExecution: function () {}
	}));

	table = table.toJSON();
	app.addComponent(table);
}

function addTopTenMemoShare (parametersJson) {
	'use strict';

	var table = new app.TableModel(_.extend(parametersJson, {
		preChange: function (value) {
			return value;
		},

		postFetch: function (values) {
			// swap axis
			var metadata = [],
				resultset = [];

			metadata.push({
				colIndex: 0,
				colName: "Top",
				colType: "String"
			});

			metadata.push({
				colIndex: 0,
				colName: "percent",
				colType: "Number"
			});

			resultset.push(['Top 5 recipients', values.resultset[0][0]]);
			resultset.push(['Top 10 recipients', values.resultset[0][1]]);
			resultset.push(['Top 20 recipients', values.resultset[0][2]]);

			values.metadata = metadata;
			values.resultset = resultset;

			return values;
		},

		preExecution: function () {
			return undefined;
		},

		postExecution: function () {}
	}));

	table = table.toJSON();
	app.addComponent(table);
}
