/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.components;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;

/**
 * Basic Checkbox input field
 * @author aartimon@developmentgateway.org
 * @since 31 October 2013
 */
public class CheckBoxField extends AbstractInputField<Boolean,CheckBox> {


    public CheckBoxField(String id, IModel<Boolean> model) {
        super(id, model);
    }

    @Override
    protected CheckBox newField(String id, IModel<Boolean> model) {
        return new CheckBox(id, model);
    }
}
