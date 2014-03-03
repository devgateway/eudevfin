/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dashboard.components;

/**
 * Extends {@link org.devgateway.eudevfin.dashboard.components.BaseParameters} with the custom fields
 * needed by the DataTable
 *
 * @author aartimon
 * @since 13/12/13
 */
public class TableParameters extends BaseParameters {
	private static final String TYPE = "tableComponent";
    private TableDefinitionParameters chartDefinition = new TableDefinitionParameters();

    public TableParameters(String id) {
        super(id);
	    setType(TYPE);
    }

	public TableParameters(String name, String id) {
		super(name, id);
		setType(TYPE);
	}

    @Override
    protected BaseParameters getInstance() {
        return this;
    }

	public TableDefinitionParameters getChartDefinition() {
		return chartDefinition;
	}

	public void setChartDefinition(TableDefinitionParameters chartDefinition) {
		this.chartDefinition = chartDefinition;
	}
}
