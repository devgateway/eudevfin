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

import java.util.regex.Pattern;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.ui.common.temporary.SB;

/**
 * @author mihai Advance Questionnaire Multilateral ODA 8/9. 
 * Channel and Code of Delivery Name only the following code and its subcodes 40000
 */
public class Field8CodeValidator extends Behavior implements IValidator<Category> {

	private static final long serialVersionUID = 7038929517889232434L;
	public static final String REGEX_MULTILATERAL_CHANNEL_CODE = "4[0-9]{4}";
	private Pattern pattern=Pattern.compile(REGEX_MULTILATERAL_CHANNEL_CODE);
	private String transactionType;
	private boolean reverse=false;


	public Field8CodeValidator(String transactionType) {
		this.transactionType=transactionType;
	}

	public Pattern getPattern() {
		return pattern;
	}

	@Override
	public void validate(IValidatable<Category> validatable) {
		if (!Strings.isEmpty(transactionType)
                && (Strings.isEqual(transactionType, SB.MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE) ||
                Strings.isEqual(transactionType, SB.MULTILATERAL_ODA_CRS))) {			
			// Check value against pattern
			if (validatable.getValue()!=null && pattern.matcher(validatable.getValue().getCode()).matches() == reverse) {
				ValidationError error = new ValidationError(this);
				error.setVariable("pattern", pattern.pattern());
				validatable.error(decorate(error, validatable));
			}
		}
	}

	public Field8CodeValidator reverse() {
		reverse = true;
		return this;
	}

	/**
	 * Allows subclasses to decorate reported errors
	 * 
	 * @param error
	 * @param validatable
	 * @return decorated error
	 */
	protected ValidationError decorate(ValidationError error, IValidatable<Category> validatable) {
		return error;
	}

}
