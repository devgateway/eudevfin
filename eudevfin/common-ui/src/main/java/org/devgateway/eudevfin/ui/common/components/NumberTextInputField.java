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

/**
 * 
 * A {@link TextInputField} for {@link Number}S
 * 
 * better solution will be to use {@link NumberTextField} but it is HTML5
 * 
 * 
 * @author mihai
 */
public class NumberTextInputField<T extends Number & Comparable<T>> extends TextInputField<T> {

	public NumberTextInputField(String id, IModel<T> model, String messageKeyGroup) {
		super(id, model, messageKeyGroup);
	}

	public NumberTextInputField(String id, IModel<T> model) {
		super(id, model, id);
	}

	public NumberTextInputField<T> range(T min, T max) {
		field.add(RangeValidator.range(min, max));
		return this;
	}

	@Override
	public NumberTextInputField<T> typeBigDecimal() {
		field.setType(BigDecimal.class);
		return this;
	}

	@Override
	public NumberTextInputField<T> required() {
		return (NumberTextInputField<T>) super.required();
	}

	@Override
	public NumberTextInputField<T> decorateMask(final String mask) {
		return (NumberTextInputField<T>) super.decorateMask(mask);
	}

	@Override
	public NumberTextInputField<T> typeInteger() {
		return (NumberTextInputField<T>) super.typeInteger();
	}

	@Override
	public NumberTextInputField<T> hideLabel() {
		return (NumberTextInputField<T>) super.hideLabel();
	}

	@Override
	public NumberTextInputField<T> setSize(InputBehavior.Size size) {
		return (NumberTextInputField<T>) super.setSize(size);
	}

}
