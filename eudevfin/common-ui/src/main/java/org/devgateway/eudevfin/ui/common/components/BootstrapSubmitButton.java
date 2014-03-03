/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

/**
 *
 */
package org.devgateway.eudevfin.ui.common.components;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.ControlGroup;

/**
 * @author mihai
 */
public abstract class BootstrapSubmitButton extends IndicatingAjaxButton {

    /**
     * @param id
     * @param model
     */
    public BootstrapSubmitButton(String id, IModel<String> model) {    	
        super(id, model);
        add(new AttributeAppender("class", new Model<String>("btn btn-primary"), " "));
    }

    @Override
    protected abstract void onSubmit(AjaxRequestTarget target, Form<?> form);

    /**
     * Override this to implement customized visitation code for components of the form
     *
     * @param component
     * @param visit
     */
    public void componentVisitor(AjaxRequestTarget target, FormComponent component,
                                 IVisit<Void> visit) {
        if (!component.isValid()) {
            Component parent = component
                    .findParent(ControlGroup.class);
            target.add(parent);
        }
    }

    @Override
    protected void onError(final AjaxRequestTarget target, Form<?> form) {
        form.visitChildren(FormComponent.class,
                new IVisitor<FormComponent, Void>() {
                    @Override
                    public void component(FormComponent component,
                                          IVisit<Void> visit) {
                        componentVisitor(target, component, visit);
                    }
                });
    }
}
