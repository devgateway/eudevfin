/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.components;

import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig.View;

/**
 * Basic bootstrap date input field
 *
 * @author aartimon@developmentgateway.org
 * @since 17 October 2013
 */
public class DateInputField extends AbstractInputField<Date,TextField<Date>> {

    public DateInputField(String id, IModel<Date> model) {
        super(id, model);
        
		IndicatingAjaxLink<String> remove = new IndicatingAjaxLink<String>("clearField") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				DateInputField.this.field.setModelObject(null);
				target.add(DateInputField.this.field);
			}
		};
		
		xPenderController.add(remove);
    }

    private String getFormat() {
        return "dd/MM/yyyy";
    }

    @Override
    protected TextField<Date> newField(String id, IModel<Date> model) {
        DateTextFieldConfig config = new DateTextFieldConfig().withView(View.Year).
        		withFormat(getFormat()).autoClose(true).forceParse(false).showTodayButton(true).highlightToday(true);        
        DateTextField date = new DateTextField(id, model, config);
        return date;
    }
}
