/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
/**
 * 
 */
package org.devgateway.eudevfin.ui.common.validators;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.joda.money.CurrencyUnit;

/**
 * @author Alex
 *
 */
public class DuplicateCurrencyValidator implements IValidator<CurrencyUnit> {
	
	private static final long serialVersionUID = -6569606425970227867L;
	private FormComponent formComponent;
	

	public DuplicateCurrencyValidator(FormComponent formComponent) {
		super();
		this.formComponent = formComponent;
	}




	/* (non-Javadoc)
	 * @see org.apache.wicket.validation.IValidator#validate(org.apache.wicket.validation.IValidatable)
	 */
	@Override
	public void validate(IValidatable<CurrencyUnit> validatable) {
		Form<?> form = this.formComponent.findParent(Form.class);
		IModel<FinancialTransaction> tx = (IModel<FinancialTransaction>) form
				.getInnermostModel();
		if (tx.getObject().getCurrency().equals(validatable.getValue())) {
			ValidationError error = new ValidationError(this);
			error.addKey("currencies.duplicate");
			validatable.error(error);
			//this.formComponent.clearInput();
		}
	}

}
