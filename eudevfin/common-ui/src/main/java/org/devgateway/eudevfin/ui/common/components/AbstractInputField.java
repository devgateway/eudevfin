/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.components;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.InputBehavior;

/**
 * Extension of the {@link org.devgateway.eudevfin.ui.common.components.AbstractField} that adds some common functionality
 * found in input fields: placeholder text, input behavior, etc
 *
 * @param <T> the data type of the field
 * @param <FIELD> the enclosing field, descendant of {@link FormComponent}
 */
public abstract class AbstractInputField<T,  FIELD extends FormComponent<T>> extends AbstractField<T, FIELD > {
    private IModel<String> placeholderText;


    /**
     * Constructor that will use the id as the messageKeyGroup
     *
     * @param id    component id
     * @param model associated
     */
    AbstractInputField(String id, IModel<T> model) {
        this(id, model, id);
    }

    /**
     * Base constructor, initialises the placeholder model using the messageKeyGroup
     *
     * <p>The {@param messageKeyGroup} is the prefix name for the resource keys that are being used.</p>
     * <p>For example if the prefix is <i>"login.username"</i> then the component will use:
     * <li><i>"login.username<b>.label</b>"</i> for the Label text</li>
     * <li><i>"login.username<b>.help</b>"</i> for the help text shown underneath the input field</li>
     * <li><i>"login.username<b>.placeholder</b>"</i> for the placeholder used when the input field is empty</li>
     * </p>

     * @param id wicket placeholder id
     * @param model component's model
     * @param messageKeyGroup Message key group prefix for the resources used by the component
     */
    AbstractInputField(String id, IModel<T> model, String messageKeyGroup) {
        super(id, model, messageKeyGroup);

        placeholderText = new StringResourceModel(messageKeyGroup + ".placeholder", this, null, "");
        addComponents(model);
    }

    private void addComponents(IModel<T> model) {
        addFormComponent(newField("field", model));
        field.setOutputMarkupId(true);

        field.add(new InputBehavior(InputBehavior.Size.Large));
    }

    @Override
    protected void onBeforeRender() {
        if (!hasBeenRendered())
            field.add(new AttributeModifier("placeholder", placeholderText));

        super.onBeforeRender();
    }

    /**
     * Set the current field to be required
     * @return current component
     */
    @Override
    @SuppressWarnings("unchecked")
    public AbstractInputField<T,FIELD> required() {
        return (AbstractInputField<T,FIELD>) super.required();
    }

    /**
     * Override the placeholder text that's usually set by providing a message key group
     * @param placeholderText
     */
    public void setPlaceholderText(IModel<String> placeholderText) {
        this.placeholderText = placeholderText;
    }

    /**
     * Modifies the width of the input field
     * @param size input behavior size
     * @return current component
     */
    protected AbstractInputField<T,FIELD> setSize(InputBehavior.Size size) {
        List<InputBehavior> list = field.getBehaviors(InputBehavior.class);
        if (list.size() != 1)
            throw new AssertionError("an InputBehavior must be attached to current field!");

        for (InputBehavior b : list)
            b.size(size);

        return this;
    }

    /**
     * Abstract method used to spawn a new form component instance
     * @param id
     * @param model
     * @return
     */
    protected abstract FIELD newField(String id, IModel<T> model);
}
