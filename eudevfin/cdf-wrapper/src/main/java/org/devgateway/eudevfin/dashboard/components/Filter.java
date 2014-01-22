/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dashboard.components;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * Simple filter implementation
 *
 * @author aartimon
 * @since 13/12/13
 */
public class Filter extends Panel {

    private final WebMarkupContainer filter;
    private FilterProperties properties = new FilterProperties();
    private String parameter;
    private String dataAccessId;

    private class FilterProperties {

    }

    public Filter(String id, String dataAccessId, String messageKey, String parameter) {
        super(id);
        this.parameter = parameter;
        this.dataAccessId = dataAccessId;

	    Label title = new Label("title", new StringResourceModel(messageKey, this, null, null));
	    add(title);

        filter = new WebMarkupContainer("filter");
        filter.setOutputMarkupId(true);
        add(filter);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Filter.class, "FilterInit.js")));
        response.render(OnDomReadyHeaderItem.forScript("initFilter('" + filter.getMarkupId() + "', '" + dataAccessId + "', '" + parameter + "');"));
    }

    public String getParameter() {
        return parameter;
    }
}
