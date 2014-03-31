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

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.util.CategoryConstants;
import org.devgateway.eudevfin.ui.common.temporary.SB;

/**
 * @author mihai
 * 
 *         ODAEU-35: Multilateral ODA, Advance Questionnaire and CRS++ forms
 *         Type of flow allowed = 2
 */
public class MultilateralField10CodeValidator extends Behavior implements IValidator<Category> {

	private static final long serialVersionUID = -1197816416536140235L;
	private String transactionType;

	public MultilateralField10CodeValidator(String transactionType) {
		this.transactionType = transactionType;
	}

	@Override
	public void validate(IValidatable<Category> validatable) {
		if (!Strings.isEmpty(transactionType)
				&& (Strings.isEqual(transactionType, SB.MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE) 
				|| Strings.isEqual(transactionType, SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE))) {
			if (validatable.getValue().getCode().equals(CategoryConstants.BiMultilateral.BI_MULTILATERAL_2))
				return;
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
