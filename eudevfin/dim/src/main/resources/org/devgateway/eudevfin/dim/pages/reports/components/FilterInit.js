/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

function initFilter(filterId, dataAccessId, parameter) {
    var filter = new app.FilterModel({
        name: filterId,
        parameter: parameter,
        htmlObject: filterId,
        queryDefinition: {
            dataAccessId: "sectorList",
            path: '/some/path.cda'
        },
        postFetch: function (values) {
            return values;
        },

        preExecution: function () {
            return undefined;
        },
        postExecution: function () {
        }
    });

    Dashboards.addComponent(filter.toJSON());
}

