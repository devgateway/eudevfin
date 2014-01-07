/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.core.components;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.InputBehavior;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;

/**
 * basic text area input field
 * @author aartimon@developmentgateway.org
 * @since 17 October 2013
 */
public class TextAreaInputField extends AbstractInputField<String> {
    private int rows = 3;

    public TextAreaInputField(String id, IModel<String> model) {
        super(id, model);
    }

    @Override
    protected void onBeforeRender() {
        if (!hasBeenRendered())
            getField().add(new AttributeModifier("rows", rows));
        super.onBeforeRender();
    }

    @Override
    protected TextArea<String> newField(String id, IModel<String> model) {
        return new TextArea<>(id, model);
    }

    public TextAreaInputField setRows(int rows){
        this.rows = rows;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param size input behavior size
     * @return
     */
    @Override
    public TextAreaInputField setSize(InputBehavior.Size size) {
        super.setSize(size);
        return this;
    }
}
