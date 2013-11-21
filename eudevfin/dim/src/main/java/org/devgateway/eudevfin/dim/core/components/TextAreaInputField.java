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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;

/**
 * @author aartimon@developmentgateway.org
 * @since 17 October 2013
 */
public class TextAreaInputField extends AbstractInputField<String> {
    private int rows = 3;

    public TextAreaInputField(String id, IModel<String> model, String messageKeyGroup) {
        super(id, model, messageKeyGroup);
    }

    @Override
    protected void onBeforeRender() {
        if (!hasBeenRendered())
            getField().add(new AttributeModifier("rows", rows));
        super.onBeforeRender();
    }

    @Override
    protected TextArea<String> newField(String id, IModel<String> model) {
        return new TextArea<String>(id, model);
    }

    public TextAreaInputField setRows(int rows){
        this.rows = rows;
        return this;
    }
}
