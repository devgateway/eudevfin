/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.reports.components;

/**
 * Extends {@link org.devgateway.eudevfin.dim.pages.reports.components.BaseParameters} with the custom fields
 * needed by the DataTable
 *
 * @author aartimon
 * @since 13/12/13
 */
public class DataTableParameters extends BaseParameters {
    private TableDefinitionParameters chartDefinition = new TableDefinitionParameters();
    private Boolean executeAtStart = Boolean.TRUE;

    public DataTableParameters(String id) {
        super(id);
    }

    @Override
    protected BaseParameters getInstance() {
        return this;
    }
}
