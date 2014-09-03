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

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.eudevfin.metadata.common.domain.Category;

public class PurposeField15CodeValidator extends Behavior implements IValidator<Category> {


	private static final long serialVersionUID = 2399353497790191290L;
	
	private String transactionType;

	public PurposeField15CodeValidator(String transactionType) {
		this.transactionType = transactionType;
	}

	@Override
	public void validate(IValidatable<Category> validatable) {
		if (!Strings.isEmpty(transactionType)) {
			if (validatable.getValue() != null && validatable.getValue().isLastAncestor()) {
				ValidationError error = new ValidationError(this);
				validatable.error(decorateParentError(error, validatable));
			}
		}


	}

	protected IValidationError decorateParentError(ValidationError error, IValidatable<Category> validatable) {
		return error;
	}



}
