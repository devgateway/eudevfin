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
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
import org.devgateway.eudevfin.metadata.common.util.CategoryConstants;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.temporary.SB;

/**
 * @author mihai Bilateral and Multilateral ODA forms, Advance Questionnaire,
 *         CRS++ and FSS forms 13. Type of aid only the code B01 (Core support
 *         to NGOs, other private bodies, PPPs and research institutes could be
 *         chosen) should be allowed If in the field 8/9 Channel the codes 2xxxx
 *         are chosen; 13. Type of aid Bilateral form If B01: Core support to
 *         NGOs, other private bodies, PPPs and research institutes, then
 *         Bi_multi =3 13. Type of aid Multilateral form If B02: Core
 *         contributions to multilateral institutions, then Bi_multi =2
 */
public class TypeOfAidField13ChannelCodeValidator extends Behavior implements IValidator<Category> {

	private static final long serialVersionUID = 6723482301156822226L;
	public static final String CHANNEL_2xxxx_REGEX = "2[0-9]{4}";
	public static final String CHANNEL_1xxxx_REGEX = "1[0-9]{4}";

	private Pattern channel2xxxxPattern = Pattern.compile(CHANNEL_2xxxx_REGEX);
	private Pattern channel1xxxxPattern = Pattern.compile(CHANNEL_1xxxx_REGEX);

	private DropDownField<ChannelCategory> channelCategory;
	private String transactionType;

	public TypeOfAidField13ChannelCodeValidator(String transactionType, DropDownField<ChannelCategory> channelCategory) {
		this.channelCategory = channelCategory;
		this.transactionType = transactionType;
	}

	@Override
	public void validate(IValidatable<Category> validatable) {
		if (!Strings.isEmpty(transactionType)
				&& (Strings.isEqual(transactionType, SB.BILATERAL_ODA_CRS)
						|| Strings.isEqual(transactionType, SB.MULTILATERAL_ODA_CRS) || Strings.isEqual(
						transactionType, SB.BILATERAL_ODA_FORWARD_SPENDING))) {
		//DISABLED upon request
		//	validateB01For2xxxx(validatable);
		//	validateB01For1xxxx(validatable);
		}
		
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

	/**
	 * don't allow B01 selection for 1xxxx fields
	 * 
	 * @param validatable
	 */
	private void validateB01For1xxxx(IValidatable<Category> validatable) {
		if (channelCategory.getField().getModelObject() != null
				&& channel1xxxxPattern.matcher(channelCategory.getField().getModelObject().getCode()).matches()
				&& validatable.getValue() != null
				&& CategoryConstants.TypeOfAid.B01.equals(validatable.getValue().getDisplayableCode())) {
			ValidationError error = new ValidationError(this);
			validatable.error(decorateB01For1xxxx(error, validatable));
		}
	}

	/**
	 * allow B01 selection for 2xxxx fields
	 * 
	 * @param validatable
	 */
	public void validateB01For2xxxx(IValidatable<Category> validatable) {
		if (channelCategory.getField().getModelObject() != null && validatable.getValue() != null)
		if (channel2xxxxPattern.matcher(channelCategory.getField().getModelObject().getCode()).matches()				
				^ CategoryConstants.TypeOfAid.B01.equals(validatable.getValue().getDisplayableCode())) {
			ValidationError error = new ValidationError(this);
			validatable.error(decorateB01For2xxxx(error, validatable));
		}
	}

	protected IValidationError decorateB01For1xxxx(ValidationError error, IValidatable<Category> validatable) {
		return error;
	}

	protected IValidationError decorateB01For2xxxx(ValidationError error, IValidatable<Category> validatable) {
		return error;
	}

}
