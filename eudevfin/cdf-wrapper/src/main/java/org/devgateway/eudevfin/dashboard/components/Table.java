/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dashboard.components;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * Entity to spawn a Data Table
 *
 * @author aartimon
 * @since 13/12/13
 */
public class Table extends Panel implements IParametersProvider {
    private final WebMarkupContainer table;
    private TableParameters parameters;
	private String initFunction;

    public Table(String id, String dataAccessId, String messageKey) {
        super(id);

        Label title = new Label("title", new StringResourceModel(messageKey, this, null, null));
        add(title);

        table = new WebMarkupContainer("table");
        table.setOutputMarkupId(true);
        add(table);

        String tableId = table.getMarkupId();
        parameters = new TableParameters(id, tableId);

	    parameters.getChartDefinition().setDataAccessId(dataAccessId);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forUrl("/js/dataTables/css/demo_page.css"));
        response.render(CssHeaderItem.forUrl("/js/dataTables/css/demo_table.css"));
        response.render(CssHeaderItem.forUrl("/js/dataTables/css/demo_table_jui.css"));

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Table.class, "TableInit.js")));
        response.render(OnDomReadyHeaderItem.forScript(this.initFunction + "(" + parameters().toJson() + ");"));
    }

    @Override
    public BaseParameters parameters() {
        return parameters;
    }

	public TableParameters getParameters() {
		return parameters;
	}

	public void setParameters(TableParameters parameters) {
		this.parameters = parameters;
	}

	public String getInitFunction() {
		return initFunction;
	}

	public void setInitFunction(String initFunction) {
		this.initFunction = initFunction;
	}
}
