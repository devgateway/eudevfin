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
import org.devgateway.eudevfin.metadata.common.util.CategoryConstants;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.temporary.SB;

/**
 * @author mihai
 * 
 *         ODAEU-35: Multilateral ODA, Advance Questionnaire and CRS++ forms
 *         Type of flow allowed = 2
 */
public class MultilateralField10TypeOfAidCodeValidator extends Behavior implements IValidator<Category> {

	private static final long serialVersionUID = -1197816416536140235L;
	private String transactionType;
	private DropDownField<Category> typeOfAid;

	public MultilateralField10TypeOfAidCodeValidator(String transactionType, DropDownField<Category> typeOfAid) {
		this.transactionType = transactionType;
		this.typeOfAid = typeOfAid;
	}

	@Override
	public void validate(IValidatable<Category> validatable) {

		if (!Strings.isEmpty(transactionType)
				&& (Strings.isEqual(transactionType, SB.MULTILATERAL_ODA_CRS) || Strings.isEqual(transactionType,
						SB.MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE))
				|| Strings.isEqual(transactionType, SB.MULTILATERAL_ODA_CRS)) {
			validateB02ForBiMulti2(validatable);
		}

	}

	private void validateB02ForBiMulti2(IValidatable<Category> validatable) {
		if (typeOfAid.getField().getModelObject() != null && validatable.getValue() != null)
			if (CategoryConstants.TypeOfAid.B02.equals(typeOfAid.getField().getModelObject().getDisplayableCode())
					^ CategoryConstants.BiMultilateral.BI_MULTILATERAL_2.equals(validatable.getValue().getCode())) {
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
