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

import com.vaynberg.wicket.select2.ChoiceProvider;
import com.vaynberg.wicket.select2.Select2MultiChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;

import java.util.Collection;

/**
 * @author aartimon@developmentgateway.org
 * @since 31 October 2013
 */
public class MultiSelectField<T> extends AbstractInputField<Collection<T>> {

    private ChoiceProvider<T> choiceProvider;

    @SuppressWarnings("unchecked")
    public MultiSelectField(String id, IModel<Collection<T>> model, String messageKeyGroup, ChoiceProvider<T> choiceProvider) {
        super(id, model, messageKeyGroup);
        //the field will already be populated by the AbstractInputField constructor
        ((Select2MultiChoice<T>)field).setProvider(choiceProvider);
    }

    @Override
    protected FormComponent<Collection<T>> newField(String id, IModel<Collection<T>> model) {
        return new Select2MultiChoice<>(id, model);
    }
}
