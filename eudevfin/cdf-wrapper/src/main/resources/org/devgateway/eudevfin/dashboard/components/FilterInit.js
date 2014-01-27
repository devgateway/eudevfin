/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

function initFilter(parametersJson) {
	'use strict';

	var filterId = parametersJson.htmlObject;
    var filter = new app.FilterModel(_.extend(parametersJson, {
	    preChange: function (value) {
			// we need to this in order to filter MDX results
		    if (value !== 'Members') {
		        return '[' + value + ']';
		    } else {
			    return value;
		    }
	    },

        postFetch: function (values) {
            return values;
        },
        preExecution: function () {
            return undefined;
        },
        postExecution: function () {
	        // add the 'All Options' option
	        $('#' + filterId + ' select').prepend('<option value="Members">All Options</option>');

	        // mark the first option as selected
	        $('#' + filterId + ' select').val($('#' + filterId + ' select option:first').val()).change();
	        // TODO - this should be done in a pretty way

	        //app.biMultilateralListParameter = "Members";
	        eval(this.parameter + ' =  "Members"');
        }
    }));

    filter = filter.toJSON();
	app.addComponent(filter);
}
