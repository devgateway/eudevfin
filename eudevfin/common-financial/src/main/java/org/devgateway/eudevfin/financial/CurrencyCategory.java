/**
 * 
 */
package org.devgateway.eudevfin.financial;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 * @author Alex
 *
 */
@Entity
@Audited
@DiscriminatorValue("Currency")
public class CurrencyCategory extends Category {

	private static final long serialVersionUID = -7847940152929772336L;
	
	private boolean mainCurrency	= false;

	public boolean isMainCurrency() {
		return mainCurrency;
	}

	public void setMainCurrency(boolean mainCurrency) {
		this.mainCurrency = mainCurrency;
	}
	
	

}
