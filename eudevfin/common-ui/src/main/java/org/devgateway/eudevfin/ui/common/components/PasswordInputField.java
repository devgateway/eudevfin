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

package org.devgateway.eudevfin.ui.common.components;

import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

/**
 * @author aartimon@developmentgateway.org
 * @since 17 October 2013
 */
public class PasswordInputField extends AbstractInputField<String> {


    public PasswordInputField(String id, IModel<String> model) {
        this(id, model, id);
    }
    public PasswordInputField(String id, IModel<String> model, String messageKeyGroup) {
        super(id, model, messageKeyGroup);
    }

    @Override
    protected TextField<String> newField(String id, IModel<String> model) {
        return new PasswordTextField(id, model);
    }
}
