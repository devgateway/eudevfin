/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.components;

import com.vaynberg.wicket.select2.ChoiceProvider;
import com.vaynberg.wicket.select2.Select2Choice;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.InputBehavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;

/**
 * Basic Select2 drop down field
 * @author aartimon@developmentgateway.org
 * @since 31 October 2013
 */
public class DropDownField<T> extends AbstractInputField<T> {

    private ChoiceProvider<T> choiceProvider;

    public DropDownField(String id, IModel<T> model, ChoiceProvider<T> choiceProvider) {
        super(id, model);
        //the field will already be populated by the AbstractInputField constructor
        getField().setProvider(choiceProvider);
    }

    @Override
    protected FormComponent<T> newField(String id, IModel<T> model) {
        return new Select2Choice<>(id, model);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Select2Choice<T> getField() {
        return (Select2Choice<T>) field;
    }

    public DropDownField<T> disableSearch() {
        getField().getSettings().setMinimumResultsForSearch(Integer.MAX_VALUE);
        return this;
    }

    @Override
    public DropDownField<T> hideLabel() {
        super.hideLabel();
        return this;
    }

    @Override
    public DropDownField<T> setSize(InputBehavior.Size size) {
        super.setSize(size);
        return this;
    }
}
