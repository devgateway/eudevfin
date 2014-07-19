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

import java.math.BigDecimal;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * @author mihai
 * Validates Amounts
 */
public class FinancialAmountBigDecimalValidator extends Behavior implements IValidator<BigDecimal> {



	/* (non-Javadoc)
	 * @see org.apache.wicket.validation.IValidator#validate(org.apache.wicket.validation.IValidatable)
	 */
	@Override
	public void validate(IValidatable<BigDecimal> validatable) {
		
		if(validatable.getValue().compareTo(BigDecimal.ZERO)<=0) {
			ValidationError error = new ValidationError(this);
			error.addKey("validation.positiveAmount");
			validatable.error(error);
		}
	}

}
