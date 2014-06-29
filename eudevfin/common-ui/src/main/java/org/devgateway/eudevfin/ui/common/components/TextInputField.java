/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.components;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.InputBehavior;
import de.agilecoders.wicket.extensions.javascript.jasny.InputMaskBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;

import java.math.BigDecimal;

/**
 * <p>Creates an input field with attached label and placeholder, see constructor for more info</p>
 *
 * @author aartimon@developmentgateway.org
 * @since 17 October 2013
 */
public class TextInputField<T> extends AbstractInputField<T,TextField<T>> {

    /**
     * <p>Creates an input field with attached label and placeholder</p>
     *
     * @param id              component id
     * @param model           model to be used by the text field
     * @param messageKeyGroup Message key group prefix for the resources used by the component
     */
    public TextInputField(String id, IModel<T> model, String messageKeyGroup) {
        super(id, model, messageKeyGroup);
    }

    /**
     * Simplified constructor that lets the user use the wicket:id as the messageKeyGroup as well
     *
     * @param id    component id
     * @param model model to be used by the text field
     * @see TextInputField(String, IModel, String)
     */
    public TextInputField(String id, IModel<T> model) {
        this(id, model, id);      
    }


    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        if (field.getType() == null)
            throw new RuntimeException("Please use one of the type methods (typeString, typeInteger, etc) on the " +
                    "TextInputField to be sure the type is properly set! Component id:" + getId());
    }

    @Override
    protected TextField<T> newField(String id, IModel<T> model) {
        return new TextField<>(id, model);
    }

    @SuppressWarnings("unchecked")
    public TextField<T> getField() {
        return (TextField<T>) field;

    }

    @SuppressWarnings("unchecked")
    public AbstractInputField<T,TextField<T>> decorateAsEmailField() {
        field.add((IValidator) EmailAddressValidator.getInstance());
        prepender.setVisible(true);
        prepender.setDefaultModel(Model.of("@"));        
        return this;
    }

    public TextInputField<T> decorateMask(final String mask) {
        field.add(new InputMaskBehavior() {
            @Override
            protected String getMask() {
                return mask;
            }
        });
        return this;
    }

    public TextInputField<T> typeString() {
        return typeString(254);
    }

    public TextInputField<T> typeString(int maxLength) {
        field.setType(String.class);
        field.add(StringValidator.maximumLength(maxLength));

        return this;
    }

    public TextInputField<T> typeInteger() {
        field.setType(Integer.class);
        return this;
    }

    public TextInputField<T> typeBigDecimal() {
        field.setType(BigDecimal.class);
        return this;
    }


    @Override
    public TextInputField<T> required() {
        super.required();
        return this;
    }

    public TextInputField<T> range(Integer min, Integer max) {
        if (!field.getType().isAssignableFrom(Integer.class))
            throw new RuntimeException("Please use the typeInteger() method to set the type, or range validator won't work!");
        field.add(RangeValidator.range(min, max));
        return this;
    }

    @Override
    public TextInputField<T> setSize(InputBehavior.Size size) {
        super.setSize(size);
        return this;
    }
}
