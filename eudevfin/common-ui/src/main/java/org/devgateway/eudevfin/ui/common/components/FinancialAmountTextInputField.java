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
package org.devgateway.eudevfin.ui.common.components;

import java.math.BigDecimal;

import org.apache.wicket.model.IModel;
import org.devgateway.eudevfin.ui.common.validators.FinancialAmountBigDecimalValidator;

/**
 * @author mihai
 * A {@link NumberTextInputField} for financials. Positive numbers only and type is always {@link BigDecimal}
 * @see FinancialAmountBigDecimalValidator
 *
 */
public class FinancialAmountTextInputField extends NumberTextInputField<BigDecimal> {
    /**
     * @param id
     * @param model
     * @param messageKeyGroup
     */
    public FinancialAmountTextInputField(String id, IModel<BigDecimal> model, String messageKeyGroup) {
            super(id, model, messageKeyGroup);
            initialize();
    }

    /**
     * @param id
     * @param model
     */
    public FinancialAmountTextInputField(String id, IModel<BigDecimal> model) {
            super(id, model);
            initialize();
    }

    protected void initialize() {
            this.typeBigDecimal();
            this.getField().add(new FinancialAmountBigDecimalValidator());
    }

    @Override
    public FinancialAmountTextInputField required() {
            return (FinancialAmountTextInputField) super.required();
    }
}
