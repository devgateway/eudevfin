/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

function initDataTable(parametersJson) {
	'use strict';

    var table = new app.TableModel(_.extend(parametersJson, {
        preChange: function (value) {
            return value;
        },

        postFetch: function (values) {
	        var i, j , k,
		        len = values.resultset.length,
		        TOTAL_NUMBER_OF_COLUMNS = 6;

	        for(i = 0; i < len; i++) {
		        k = values.resultset[i].length;
		        for(j = 1; j < k; j++) {
					if (values.resultset[i][j] === null || values.resultset[i][j] === undefined) {
						values.resultset[i][j] = 0;
					}
		        }
				for (j = k; j < TOTAL_NUMBER_OF_COLUMNS; j++) {
					values.resultset[i][j] = 0;
				}
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
