/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.core.components.tabs;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devgateway.eudevfin.dim.core.components.PermissionAwareLabel;

import java.util.List;

/**
 * Tabbed Panel that renders all the tabs in the page and uses JS to instantly switch between tabs
 *
 * @author aartimon@developmentgateway.org
 * @since 05 November 2013
 */
public class BootstrapJSTabbedPanel<T extends ITabWithKey> extends Panel {
    private final List<T> tabs;

    public static enum Orientation {LEFT, RIGHT, TOP, BOTTOM}

    private Orientation orientation = Orientation.TOP;

    /**
     * Create a new tabbed panel that instantly switches between tabs using js
     *
     * @param id   wicket placeholder id
     * @param tabs list of tabs to render
     */
    @SuppressWarnings("WicketForgeJavaIdInspection")
    public BootstrapJSTabbedPanel(String id, final List<T> tabs) {
        super(id);
        this.setOutputMarkupId(true);

        this.tabs = tabs;

        TransparentWebMarkupContainer tmc = new TransparentWebMarkupContainer("content") {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("class", getTabContainerCssClass());
            }
        };
        add(tmc);

        final IModel<Integer> tabCount = new AbstractReadOnlyModel<Integer>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Integer getObject() {
                return BootstrapJSTabbedPanel.this.tabs.size();
            }
        };

        final String markupId = this.getMarkupId();

        add(new Loop("tabNames", tabCount) {
            @Override
            protected void populateItem(LoopItem item) {
                final int index = item.getIndex();
                final T tab = BootstrapJSTabbedPanel.this.tabs.get(index);

                if (index == 0)
                    item.add(new AttributeModifier("class", getSelectedTabCssClass()));

                Label label = new PermissionAwareLabel("tabNamesLink", tab.getTitle(), tab.getKey());
                label.add(new AttributeModifier("href", "#" + getTabId(markupId, index)));
                item.add(label);
            }
        });

        add(new Loop("tabContent", tabCount) {

            @Override
            protected void populateItem(LoopItem item) {
                final int index = item.getIndex();
                final T tab = BootstrapJSTabbedPanel.this.tabs.get(index);

                if (index == 0)
                    item.add(new AttributeAppender("class", Model.of(getSelectedTabCssClass()), " "));

                item.add(new AttributeModifier("id", getTabId(markupId, index)));
                item.add(tab.getPanel("tabContentPanel"));
            }
        });

    }

    private String getTabId(String markupId, int item) {
        return markupId + "-" + item;
    }

    String getTabContainerCssClass() {
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

    String getSelectedTabCssClass() {
        return "active";
    }

    public BootstrapJSTabbedPanel<T> positionTabs(Orientation orientation) {
        this.orientation = orientation;
        return this;
    }


}
