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

import de.agilecoders.wicket.extensions.javascript.jasny.InputMaskBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;

/**
 * <p>Creates an input field with attached label and placeholder, see constructor for more info</p>
 *
 * @author aartimon@developmentgateway.org
 * @since 17 October 2013
 */
public class TextInputField<T> extends AbstractInputField<T> {

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
    public TextInputField(String id, IModel<T> model, String messageKeyGroup) {
        super(id, model, messageKeyGroup);
    }

    @Override
    protected TextField<T> newField(String id, IModel<T> model) {
        return new TextField<T>(id, model);
    }

    @SuppressWarnings("unchecked")
    public TextField<T> getField(){
        return (TextField<T>) field;

    }

    public AbstractInputField<T> decorateAsEmailField(){
        field.add((IValidator) EmailAddressValidator.getInstance());
        prepender.setVisible(true);
        prepender.setDefaultModel(Model.of("@"));
        return this;
    }

    public AbstractInputField<T> decorateMask(final String mask){
        field.add(new InputMaskBehavior() {
            @Override
            protected String getMask() {
                return mask;
            }
        });
        return this;
    }



}