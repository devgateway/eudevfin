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
