/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.core.components.tabs;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * Copy of AjaxTabbedPanel, with bootstrap compatibility
 *
 * @author aartimon@developmentgateway.org
 * @see org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel
 * @since 01 November 2013
 */
public class BootstrapAjaxTabbedPanel<T extends ITab> extends TabbedPanel<T> {
    private static final long serialVersionUID = 1531649939509988579L;
    private boolean ajax = true;

    public static enum Orientation {LEFT, RIGHT, TOP, BOTTOM}

    private Orientation orientation = Orientation.TOP;

    /**
     * Creates a new tabbed panel with ajax support and bootstrap look
     *
     * @param id   wicket placeholder id
     * @param tabs list of tabs that are going to populate the panel
     */
    public BootstrapAjaxTabbedPanel(final String id, final List<T> tabs) {
        this(id, tabs, null);
    }

    private BootstrapAjaxTabbedPanel(final String id, final List<T> tabs, IModel<Integer> model) {
        super(id, tabs, model);
        setOutputMarkupId(true);

        setVersioned(false);
    }

    @Override
    protected WebMarkupContainer newLink(final String linkId, final int index) {
        if (ajax)
            return new AjaxFallbackLink<Void>(linkId) {

                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(final AjaxRequestTarget target) {
                    setSelectedTab(index);
                    if (target != null) {
                        target.add(BootstrapAjaxTabbedPanel.this);
                    }
                    onAjaxUpdate(target);
                }

            };
        else
            return super.newLink(linkId, index);
    }

    protected void onAjaxUpdate(final AjaxRequestTarget target) {
        //Override if needed
    }

    @Override
    protected String getTabContainerCssClass() {
        String css;
        switch (orientation) {
            case BOTTOM:
                css = "tabs-below";
                break;
            case LEFT:
                css = "tabs-left";
                break;
            case RIGHT:
                css = "tabs-right";
                break;
            default: //TOP
                css = "";
        }


        return "tabbable " + css;
    }


    @Override
    protected String getSelectedTabCssClass() {
        return "active";
    }

    @Override
    protected String getLastTabCssClass() {
        return "";
    }

    /**
     * Allows the tabbed panel to work with ajax enabled or not
     *
     * @param ajax set to false in order to disable ajax loading of panels
     * @return current instance of {@link org.devgateway.eudevfin.dim.core.components.tabs.BootstrapAjaxTabbedPanel}
     */
    public BootstrapAjaxTabbedPanel<T> setAjax(boolean ajax) {
        this.ajax = ajax;
        return this;
    }

    /**
     * Allows to change the tabs placement relative to the panel
     *
     * @param orientation
     * @return
     */
    public BootstrapAjaxTabbedPanel<T> positionTabs(Orientation orientation) {
        this.orientation = orientation;
        return this;
    }
}
