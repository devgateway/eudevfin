/**
 * 
 */
package org.devgateway.eudevfin.financial.translate;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.devgateway.eudevfin.common.dao.translation.AbstractTranslation;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

/**
 * @author Alex
 *
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FinancialTransactionTranslation extends AbstractTranslation<FinancialTransaction> implements FinancialTransactionTrnInterface {
	
	@Column(length=20000)
	private String description;
//	@Lob
	private String shortDescription;
	
	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.financial.translate.FinancialTransactionTrnInterface#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.financial.translate.FinancialTransactionTrnInterface#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getShortDescription() {
		return shortDescription;
	}

	@Override
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	
	
}
