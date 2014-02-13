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
