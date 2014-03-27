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
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.util.CategoryConstants;
import org.devgateway.eudevfin.ui.common.temporary.SB;

/**
 * @author mihai
 * 
 *         Rio Markers validator.
 *         {@link CategoryConstants.Markers#MARKER_3} is valid only for desertification
 *        
 */
public class MarkersValidator extends Behavior implements IValidator<Category> {

	private static final long serialVersionUID = -1197816416536140235L;
	private boolean desertification;

	public MarkersValidator(boolean desertification) {
		this.desertification=desertification;
	}
	
	public MarkersValidator() {
		this.desertification=false;
	}

	@Override
	public void validate(IValidatable<Category> validatable) {
		if (desertification)
			return;

		if (validatable != null && validatable.getValue().getCode().equals(CategoryConstants.Markers.MARKER_3)) {
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
