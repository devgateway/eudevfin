/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.components;

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
import org.devgateway.eudevfin.ui.common.models.ProxyModel;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwareComponent;

/**
 * Abstract class that provides the common functionality for all the fields use din the project
 * The FormComponent is wrapped in a Bootstrap Control Group that has a prepender and an appender
 *
 * @author aartimon@developmentgateway.org
 * @since 30 October 2013
 */
public abstract class AbstractField<T, FIELD extends FormComponent<T>> extends Panel implements PermissionAwareComponent {
    private static final long serialVersionUID = -5883044564199075156L;
    private ProxyModel<String> labelText;

    final Component prepender;
    private final Component appender;
    private final DetailedHelpControlGroup controlGroup;
    FIELD field;
    private final MarkupContainer xPenderController;

    /**
     * @param id              wicket placeholder id
     * @param model           component's model
     * @param messageKeyGroup Message key group prefix for the resources used by the component
     * @see org.devgateway.eudevfin.ui.common.components.TextInputField for example on how messageKeyGroup is used
     */
    AbstractField(String id, IModel<T> model, String messageKeyGroup) {
        super(id, model);
        this.setRenderBodyOnly(true);

        xPenderController = new WebMarkupContainer("xPenderController");
        prepender = new Label("prepender").setVisible(false);
        appender = new Label("appender").setVisible(false);

        //for label text we don't set a default value, this is mandatory for now
        labelText = new ProxyModel<String>(new StringResourceModel(messageKeyGroup + ".label", this, null));

        //if the help text is not found an empty String is going to be used
        StringResourceModel helpText = new StringResourceModel(messageKeyGroup + ".help", this, null, "");
        
        //if the detailed help text is not found an empty String is going to be used
        StringResourceModel detailedHelpText = new StringResourceModel(messageKeyGroup + ".help.detail", this, null, "");


        controlGroup = new DetailedHelpControlGroup("control-group", labelText, helpText,detailedHelpText);
        controlGroup.setOutputMarkupId(true);
        xPenderController.add(prepender);
        xPenderController.add(appender);
        controlGroup.add(xPenderController);
        controlGroup.add(new AttributeAppender("class", Model.of("span4"), " "));
        add(controlGroup);

    }

    @Override
    protected void onConfigure() {
        if (prepender.isVisible())
            xPenderController.add(new AttributeAppender("class", Model.of("input-prepend"), " "));

        if (appender.isVisible())
            xPenderController.add(new AttributeAppender("class", Model.of("input-append"), " "));
    }

    /**
     * Internal method used to properly init form components
     *
     * @param fc current form component
     */
    void addFormComponent(FIELD fc) {
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
     * Override this method to intercept the ajax onUpdate event
     *
     * @param target the ajax request target
     */
    protected void onUpdate(AjaxRequestTarget target) {
    }

    private String getUpdateMethod() {
        if (this instanceof DateInputField || this instanceof DropDownField || this instanceof MultiSelectField)
            return "onchange";
        return "onblur";
    }

    public FIELD getField() {
        return field;
    }

    AbstractField<T,FIELD> required() {
        field.setRequired(true);
        return this;
    }

    AbstractField<T,FIELD> hideLabel() {
        labelText.replaceModel(Model.of(""));
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
