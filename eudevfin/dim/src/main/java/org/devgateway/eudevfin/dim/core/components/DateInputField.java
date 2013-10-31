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

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

import java.util.Date;

/**
 * @author aartimon@developmentgateway.org
 * @since 17 October 2013
 */
public class DateInputField extends AbstractInputField<Date> {

    public DateInputField(String id, IModel<Date> model, String messageKeyGroup) {
        super(id, model, messageKeyGroup);
    }

    @Override
    protected TextField<Date> newField(String id, IModel<Date> model) {
        return new DateTextField(id, model);
    }
}
