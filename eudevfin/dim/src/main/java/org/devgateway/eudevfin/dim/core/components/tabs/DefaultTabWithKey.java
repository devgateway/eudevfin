/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.core.components.tabs;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * Class that builds an {@link AbstractTabWithKey} wrapped around a given {@link Panel} that has to implement {@link ITabWithKey}
 *
 * @author aartimon
 * @since 12/12/13
 */
public class DefaultTabWithKey extends AbstractTabWithKey {
    private static final Logger logger = Logger.getLogger(DefaultTabWithKey.class);
    private final Class<? extends Panel> panel;

    private DefaultTabWithKey(StringResourceModel title, String tabKey, Class<? extends Panel> panel) {
        super(title, tabKey);
        this.panel = panel;
    }

    @Override
    public WebMarkupContainer getPanel(String panelId) {
        try {
            Constructor<? extends Panel> constructor = panel.getConstructor(String.class);
            return constructor.newInstance(panelId);
        } catch (Exception e) {
            logger.error("Can't spawn new panel: " + panel, e);
            throw new AssertionError("Can't create new panel: " + panel);
        }
    }

    public static DefaultTabWithKey of(Class<? extends Panel> panel, Component askingComponent) {
        String key;
        try {
            Field field = panel.getField("KEY");
            key = (String) field.get(String.class);
        } catch (Exception e) {
            logger.error("Can't get KEY field:", e);
            throw new AssertionError("KEY field is required for panel:" + panel);
        }
        return new DefaultTabWithKey(new StringResourceModel(key, askingComponent, null), key, panel);
    }
}
