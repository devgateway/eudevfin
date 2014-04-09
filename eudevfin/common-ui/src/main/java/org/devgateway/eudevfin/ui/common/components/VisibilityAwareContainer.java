/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.components;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

/**
 * Container that will be visible if at least one child that's instance of a class implementing {@link AbstractField}
 * is visible. This can be used mostly for hiding html that's used to encapsulate groups of fields.
 *
 * @author Alexandru Artimon
 * @since 09/04/14
 */
public class VisibilityAwareContainer extends WebMarkupContainer {
    public VisibilityAwareContainer(String id) {
        super(id);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();

        final Model<Boolean> oneVisibleChild = Model.of(Boolean.FALSE);
        this.visitChildren(AbstractField.class, new IVisitor<Component, Object>() {
            @Override
            public void component(Component object, IVisit<Object> visit) {
                object.configure(); //force an early configure on the visited object
                if (object.determineVisibility()) { //use determineVisibility instead of isVisible, handles all cases!
                    oneVisibleChild.setObject(Boolean.TRUE);
                    visit.stop();
                }
            }
        });

        this.setVisibilityAllowed(oneVisibleChild.getObject());
    }
}
