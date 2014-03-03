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
			if (this.name === 'biMultilateralFilter') {
				if (value !== 'Members') {
					return '[Name].' + '[' + value + ']';
				} else {
					return '[All BiMultilaterals]';
				}
			}

		    if (this.name === 'extendingAgenciesFilter') {
			    if (value !== 'Members') {
				    return '[Acronym].' + '[' + value + ']';
			    } else {
				    return '[All Extending Agencies]';
			    }
		    }

		    if (this.name === 'sectorFilter') {
			    if (value !== 'Members') {
				    return '[Name].' + '[' + value + ']';
			    } else {
				    return '[All Sectors]';
			    }
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
	        if (this.name === 'biMultilateralFilter') {
		        eval(this.parameter + ' =  "[All BiMultilaterals]"');
	        }

	        if (this.name === 'extendingAgenciesFilter') {
		        eval(this.parameter + ' =  "[All Extending Agencies]"');
	        }

	        if (this.name === 'sectorFilter') {
		        eval(this.parameter + ' =  "[All Sectors]"');
	        }
        }
    }));

    filter = filter.toJSON();
	app.addComponent(filter);
}
