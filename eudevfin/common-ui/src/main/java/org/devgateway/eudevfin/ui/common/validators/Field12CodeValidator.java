/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

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
 * @author mihai
 * 
 *         Advance Questionnaire Bilateral and Multilateral ODA forms 12. Type
 *         of Finance only the following codes and their subcodes 100, 300, 400,
 *         500, 600
 */
public class Field12CodeValidator extends Behavior implements IValidator<Category> {

	private static final long serialVersionUID = 6723482301156822226L;
	public static final String REGEX= "(1|3|4|5|6)[0-9]{2}";
	private Pattern pattern=Pattern.compile(REGEX);
	private String transactionType;
	private boolean reverse=false;


	public Field12CodeValidator(String transactionType) {
		this.transactionType=transactionType;
	}

	public Pattern getPattern() {
		return pattern;
	}

	@Override
	public void validate(IValidatable<Category> validatable) {
		if (!Strings.isEmpty(transactionType)
                && (Strings.isEqual(transactionType, SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE) ||
                Strings.isEqual(transactionType, SB.MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE))) {			
				// Check value against pattern
			if (pattern.matcher(validatable.getValue().getDisplayableCode()).matches() == reverse) {
				ValidationError error = new ValidationError(this);
				error.setVariable("pattern", pattern.pattern());
				validatable.error(decorate(error, validatable));
			}
		}
	}

	public Field12CodeValidator reverse() {
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
