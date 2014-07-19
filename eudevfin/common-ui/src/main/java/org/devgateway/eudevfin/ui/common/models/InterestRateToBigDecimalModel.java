/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.ui.common.models;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.wicket.model.IComponentAssignedModel;

/**
 * Model for mapping a formatted interest rate to bigdecimal and back
 * 
 * @author mpostelnicu
 * @since 22.05.2014
 */
public class InterestRateToBigDecimalModel extends WrappingModel<String, BigDecimal> {
	private static final long serialVersionUID = -4642384184479408680L;
	private NumberFormat formatter;

	public InterestRateToBigDecimalModel(IComponentAssignedModel<BigDecimal> originalModel) {
		super(originalModel);
		formatter = new DecimalFormat("00000");
	}

	@Override
	public String getObject() {
		if (originalModel.getObject() == null)
			return null;
		Long i = originalModel.getObject().longValue();
		String number = formatter.format(i);
		return number;
	}

	@Override
	public void setObject(String object) {
		if (object == null)
			originalModel.setObject(null);
		else
			originalModel.setObject(BigDecimal.valueOf(new Long(object)));
	}
}
