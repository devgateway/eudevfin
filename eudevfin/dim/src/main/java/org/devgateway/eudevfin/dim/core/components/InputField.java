/*******************************************************************************
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    aartimon
 ******************************************************************************/

package org.devgateway.eudevfin.dim.core.components;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.ControlGroup;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.InputBehavior;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

/**
 * <p>Creates an input field with attached label and placeholder, see constructor for more info</p>
 *
 * @author aartimon@developmentgateway.org
 * @since 17 October 2013
 */
public class InputField<T> extends Panel {

    private TextField<T> field;
    private ControlGroup controlGroup;

    /**
     * <p>Creates an input field with attached label and placeholder</p>
     * <p>The {@param messageKeyGroup} is the prefix name for the resource keys that are being used.</p>
     * <p>For example if the prefix is <i>"login.username"</i> then the component will use:
     *      <li><i>"login.username<b>.label</b>"</i> for the Label text</li>
     *      <li><i>"login.username<b>.help</b>"</i> for the help text shown underneath the input field</li>
     *      <li><i>"login.username<b>.placeholder</b>"</i> for the placeholder used when the input field is empty</li>
     * </p>
     * @param id component id
     * @param model model to be used by the text field
     * @param messageKeyGroup Message key group prefix for the resources used by the component
     */
    public InputField(String id, IModel<T> model, String messageKeyGroup){
        super(id, model);

        //for label text we don't set a default value, this is mandatory for now
        StringResourceModel labelText = new StringResourceModel(messageKeyGroup + ".label", this, null);
        //if helptext is not found an empty String is going to be used
        StringResourceModel helpText = new StringResourceModel(messageKeyGroup + ".help", this, null, "");
        StringResourceModel placeholderText = new StringResourceModel(messageKeyGroup + ".placeholder", this, null, "");
        addComponents(model, labelText, helpText, placeholderText);
    }

    private void addComponents(IModel<T> model, IModel<String> labelText, IModel<String> helpText, StringResourceModel placeholderText){
        controlGroup = new ControlGroup("control-group", labelText, helpText);
        add(controlGroup);

        field = newField("field", model);
        field.setOutputMarkupId(true);

        field.add(new InputBehavior());
        field.add(new AttributeModifier("placeholder", placeholderText));
        field.add(new AttributeAppender("class", Model.of("input-block-level"), " "));


        controlGroup.add(field);
    }

    public TextField<T> getField() {
        return field;
    }

    protected TextField<T> newField(String id, IModel<T> model) {
        return new TextField<T>(id, model);
    }

}
