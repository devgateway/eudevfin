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
        postFetch: function (values) {
	        var i, k,
                item,
                NUMBER_OF_YEARS = 3,
                FIRST_YEAR = 2011,
                noResults = true,
		        len = values.resultset.length;

            if(values.metadata[1].colName != FIRST_YEAR) {
                item = {
                    colIndex: 1,
                    colName: "" + FIRST_YEAR,
                    colType: "Numeric"
                }

                // add the new dimension
                values.metadata.splice(1, 0, item);
                for(i = 0; i < len; i++) {
                    values.resultset[i].splice(1, 0, null);
                }

                // update the 'colIndex' property
                for(i = 2; i < values.metadata.length; i++) {
                    values.metadata[i].colIndex = i;
                }
            }

            if(values.metadata[2].colName != (FIRST_YEAR + 1)) {
                item = {
                    colIndex: 2,
                    colName: "" + (FIRST_YEAR + 1),
                    colType: "Numeric"
                }

                // add the new dimension
                values.metadata.splice(2, 0, item);
                for(i = 0; i < len; i++) {
                    values.resultset[i].splice(2, 0, null);
                }

                // update the 'colIndex' property
                for(i = 3; i < values.metadata.length; i++) {
                    values.metadata[i].colIndex = i;
                }
            }

            if(values.metadata[3].colName != (FIRST_YEAR + 2)) {
                item = {
                    colIndex: 3,
                    colName: "" + (FIRST_YEAR + 2),
                    colType: "Numeric"
                }

                // add the new dimension
                values.metadata.splice(3, 0, item);
                for(i = 0; i < len; i++) {
                    values.resultset[i].splice(3, 0, null);
                }

                // update the 'colIndex' property
                for(i = 4; i < values.metadata.length; i++) {
                    values.metadata[i].colIndex = i;
                }
            }

            // set the 'noResults' flag to false
            // indicate that we received at least 2 columns of results from the server
            // the first column is the one with headers: 'Curent (USD)', 'Bilateral share', etc..
            if (values.resultset[0].length > 1) {
                noResults = false;
            }

            // if we don't have any results, update the 'values' variable
            // we can not use 'values.queryInfo.totalRows' variable here because the query
            // will always return the first column ('Curent (USD)', 'Bilateral share', etc..)
            if (noResults) {
                values.metadata = [];
                values.resultset = [];
                values.queryInfo.totalRows = 0;

                return values;
            }

	        // add '2012 / 2013' type of column
	        for(i = 0; i < len - 2; i++) {
		        // add (last_year - 1 / last_year) column
                if (values.resultset[i][NUMBER_OF_YEARS - 1] === 0 || values.resultset[i][NUMBER_OF_YEARS - 1] === null) {
                    values.resultset[i][NUMBER_OF_YEARS + 1] = null;
                } else {
                    if (values.resultset[i][NUMBER_OF_YEARS] === 0 || values.resultset[i][NUMBER_OF_YEARS] === null) {
                        values.resultset[i][NUMBER_OF_YEARS + 1] = null;
                    } else {
                        values.resultset[i][NUMBER_OF_YEARS + 1] = (values.resultset[i][NUMBER_OF_YEARS - 1] / values.resultset[i][NUMBER_OF_YEARS]) * 100;
                    }
                }
	        }

	        for(i = len - 2; i < len; i++) {
		        k = values.resultset[i].length;
		        // add (last_year - 1 / last_year) column
		        values.resultset[i][NUMBER_OF_YEARS + 1] = null;
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

        postExecution: function () {
	        if (this.rawData.queryInfo.totalRows === 0 || this.rawData.queryInfo.totalRows === '0') {
		        //var htmlObject = this.htmlObject;
		        //$('#' + htmlObject).closest('div.box').hide();
		        app.noData();
	        }
        }
    }));

    table = table.toJSON();
	app.addComponent(table);
}

function addTopTenRecipients (parametersJson) {
	'use strict';

	var table = new app.TableModel(_.extend(parametersJson, {
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
		postFetch: function (values) {
			// check if we have any values
			// CDF is keeping the 'totalRows' value as a string!?
			if (values.queryInfo.totalRows !== 0 && values.queryInfo.totalRows !== '0') {
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

                if (values.resultset[0][0] !== undefined) {
				    resultset.push(['Top 5 recipients', values.resultset[0][0]]);
                }
                if (values.resultset[0][1] !== undefined) {
				    resultset.push(['Top 10 recipients', values.resultset[0][1]]);
                }
                if (values.resultset[0][2] !== undefined) {
				    resultset.push(['Top 20 recipients', values.resultset[0][2]]);
                }

				values.metadata = metadata;
				values.resultset = resultset;
			}

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
