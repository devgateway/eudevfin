/**
 * 
 */
package org.devgateway.eudevfin.financial.translate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.joda.time.LocalDateTime;

/**
 * @author Alex
 *
 */
@Entity
@Audited
@Table(name="FINANCIAL_TRANSACTION_TRANSLATION",
		uniqueConstraints=@UniqueConstraint(columnNames={"PARENT_ID","LOCALE"}))
public class FinancialTransactionTranslation extends AbstractTranslation implements FinancialTransactionTrnInterface {
	
	@ManyToOne(  optional= false )
	@JoinColumn(name="PARENT_ID")
	private FinancialTransaction parent;
	
	private String description;
	
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

	public FinancialTransaction getParent() {
		return parent;
	}

	public void setParent(FinancialTransaction parent) {
		this.parent = parent;
	}
	
	
	
	
	
}
