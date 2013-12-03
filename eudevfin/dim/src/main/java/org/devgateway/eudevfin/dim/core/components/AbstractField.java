/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.core.components;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.ControlGroup;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.eudevfin.dim.core.permissions.PermissionAwareComponent;

/**
 * @author aartimon@developmentgateway.org
 * @since 30 October 2013
 */
public abstract class AbstractField<T> extends Panel implements PermissionAwareComponent {
    private static final long serialVersionUID = -5883044564199075156L;
    private final StringResourceModel labelText;

    protected final Component prepender;
    protected final Component appender;
    private final ControlGroup controlGroup;
    protected FormComponent<T> field;
    private final MarkupContainer xPenderController;


    public AbstractField(String id, IModel<T> model, String messageKeyGroup) {
        super(id, model);

        xPenderController = new WebMarkupContainer("xPenderController");
        prepender = new Label("prepender").setVisible(false);
        appender = new Label("appender").setVisible(false);

        //for label text we don't set a default value, this is mandatory for now
        labelText = new StringResourceModel(messageKeyGroup + ".label", this, null);

        //if the help text is not found an empty String is going to be used
        StringResourceModel helpText = new StringResourceModel(messageKeyGroup + ".help", this, null, "");

        controlGroup = new ControlGroup("control-group", labelText, helpText);
        controlGroup.setOutputMarkupId(true);
        xPenderController.add(prepender);
        xPenderController.add(appender);
        controlGroup.add(xPenderController);
        add(controlGroup);

    }

    @Override
    protected void onConfigure() {
        if (prepender.isVisible())
            xPenderController.add(new AttributeAppender("class", Model.of("input-prepend"), " "));

        if (appender.isVisible())
            xPenderController.add(new AttributeAppender("class", Model.of("input-append"), " "));
    }

    protected void addFormComponent(FormComponent<T> fc) {
        this.field = fc;
        field.setLabel(labelText);
        fc.setOutputMarkupId(true);
        fc.add(new AjaxFormComponentUpdatingBehavior(getUpdateMethod()) {
            private static final long serialVersionUID = -6554038647835016155L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(controlGroup);
                AbstractField.this.onUpdate(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, RuntimeException e) {
                target.add(controlGroup);
            }
        });
        xPenderController.add(fc);
    }

    /**
     * Override this to intercept the ajax onUpdate event
     *
     * @param target the ajax request target
     */
    protected void onUpdate(AjaxRequestTarget target) {
    }

    private String getUpdateMethod() {
        if (this instanceof DateInputField || this instanceof DropDownField)
            return "onchange";
        return "onblur";
    }

    protected FormComponent<T> getField() {
        return field;
    }

    public AbstractField<T> required() {
        field.setRequired(true);
        return this;
    }

    @Override
    public String getPermissionKey() {
        return this.getId();
    }

    @Override
    public void enableRequired() {
        required();
    }
}
