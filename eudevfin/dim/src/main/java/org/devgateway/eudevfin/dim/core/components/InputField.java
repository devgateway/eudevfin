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
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * @author aartimon@developmentgateway.org
 * @since 17 October 2013
 */
public class InputField<T> extends Panel {

    private TextField<T> field;
    private ControlGroup controlGroup;

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

        field = getField("field", model);
        field.setOutputMarkupId(true);

        field.add(new InputBehavior());
        field.add(new AttributeModifier("placeholder", placeholderText));

        controlGroup.add(field);
    }

    protected TextField<T> getField(String id, IModel<T> model) {
        return new TextField<T>(id, model);
    }

}
