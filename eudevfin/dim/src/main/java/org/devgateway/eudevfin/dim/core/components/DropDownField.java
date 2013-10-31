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
import com.vaynberg.wicket.select2.Select2Choice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;

/**
 * @author aartimon@developmentgateway.org
 * @since 31 October 2013
 */
public class DropDownField<T> extends AbstractInputField<T> {

    private ChoiceProvider<T> choiceProvider;

    @SuppressWarnings("unchecked")
    public DropDownField(String id, IModel<T> model, String messageKeyGroup, ChoiceProvider<T> choiceProvider) {
        super(id, model, messageKeyGroup);
        //the field will already be populated by the AbstractInputField constructor
        ((Select2Choice<T>)field).setProvider(choiceProvider);
    }

    @Override
    protected FormComponent<T> newField(String id, IModel<T> model) {
        return new Select2Choice<T>(id, model);
    }
}
