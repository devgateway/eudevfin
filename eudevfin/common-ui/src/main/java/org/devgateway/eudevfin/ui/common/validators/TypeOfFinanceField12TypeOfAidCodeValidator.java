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
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.util.CategoryConstants;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.temporary.SB;

/**
 * @author mihai
 * 
 *         12. Type of Finance correlated with Type of Aid both forms Types of
 *         finance 600 must be associated with type of aid F01 (Debt relief)'
 */
public class TypeOfFinanceField12TypeOfAidCodeValidator extends Behavior implements IValidator<Category> {

	public static final String REGEX = "(6)[0-9]{2}";
	private Pattern pattern = Pattern.compile(REGEX);

	private static final long serialVersionUID = -1197816416536140235L;

	private DropDownField<Category> typeOfAid;

	public TypeOfFinanceField12TypeOfAidCodeValidator(DropDownField<Category> typeOfAid) {
		this.typeOfAid = typeOfAid;
	}

	
	@Override
	public void validate(IValidatable<Category> validatable) {
		if (typeOfAid.getField().getModelObject() != null && validatable.getValue() == null)
			if (CategoryConstants.TypeOfAid.F01.equals(typeOfAid.getField().getModelObject().getDisplayableCode())
					^ pattern.matcher(validatable.getValue().getDisplayableCode()).matches()) {
				ValidationError error = new ValidationError(this);
				validatable.error(decorate(error, validatable));
			}
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
