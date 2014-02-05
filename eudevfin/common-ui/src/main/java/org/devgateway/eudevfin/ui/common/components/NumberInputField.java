/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.components;

import java.math.BigDecimal;

import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.RangeValidator;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.InputBehavior;
import de.agilecoders.wicket.extensions.javascript.jasny.InputMaskBehavior;

/**
 * <p>Creates a similar input field as {@link TextInputField} but for {@link Number}S
 * This is HTML5 
 * 
 * @author mihai
 * @since 17 October 2013
 * @see TextInputField
 */
public class NumberInputField<T extends Number & Comparable<T>> extends AbstractInputField<T,NumberTextField<T>> {

    /**
     * <p>Creates an input field with attached label and placeholder</p>
     *
     * @param id              component id
     * @param model           model to be used by the text field
     * @param messageKeyGroup Message key group prefix for the resources used by the component
     */
    public NumberInputField(String id, IModel<T> model, String messageKeyGroup) {
        super(id, model, messageKeyGroup);
    }

    /**
     * Simplified constructor that lets the user use the wicket:id as the messageKeyGroup as well
     *
     * @param id    component id
     * @param model model to be used by the text field
     * @see TextInputField(String, IModel, String)
     */
    public NumberInputField(String id, IModel<T> model) {
        this(id, model, id);      
    }

    @Override
    protected NumberTextField<T> newField(String id, IModel<T> model) {
        return new NumberTextField<>(id, model);
    }

    @SuppressWarnings("unchecked")
    public NumberTextField<T> getField() {
        return (NumberTextField<T>) field;

    }

    public NumberInputField<T> decorateMask(final String mask) {
        field.add(new InputMaskBehavior() {
            @Override
            protected String getMask() {
                return mask;
            }
        });
        return this;
    }

    public NumberInputField<T> typeInteger() {
        field.setType(Integer.class);
        return this;
    }

    public NumberInputField<T> typeBigDecimal() {
        field.setType(BigDecimal.class);
        return this;
    }


    @Override
    public NumberInputField<T> required() {
        super.required();
        return this;
    }

    public NumberInputField<T> range(T min, T max) {                   
        field.add(RangeValidator.range(min, max));
        return this;
    }

    @Override
    public NumberInputField<T> hideLabel() {
        super.hideLabel();
        return this;
    }

    @Override
    public NumberInputField<T> setSize(InputBehavior.Size size) {
        super.setSize(size);
        return this;
    }
}
