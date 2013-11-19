package org.devgateway.eudevfin.financial;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.devgateway.eudevfin.financial.translate.FinancialTransactionTranslation;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
@Entity
@Audited
@Table(name="FINANCIAL_TRANSACTION")
public class FinancialTransaction extends AbstractTranslateable<FinancialTransactionTranslation> implements Serializable{
	
	private BigDecimal amount;
	
	private String donorProjectNumber;
	
//	
//	@Version
//	public Long version = null;
//	
	@ManyToOne(  optional= false )
	@JoinColumn(name="ORGANIZATION_ID")
	private Organization sourceOrganization;
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getDescription() {		 
		return (String) this.get("description");
		
	}
	public void setDescription(String description) {
		this.set("description", description);
	}

	public Organization getSourceOrganization() {
		return sourceOrganization;
	}
	public void setSourceOrganization(Organization sourceOrganization) {
		this.sourceOrganization = sourceOrganization;
	}
	
//	public Long getVersion() {
//		return version;
//	}
//	public void setVersion(Long version) {
//		this.version = version;
//	}
	@Override
	public String toString() {
		return String.format("Id %s, amount %f, description %s", id, amount.doubleValue(), getDescription());
	}
	@Override
	protected FinancialTransactionTranslation newTranslationInstance() {
		return new FinancialTransactionTranslation();
	}
	public String getDonorProjectNumber() {
		return donorProjectNumber;
	}
	public void setDonorProjectNumber(String donorProjectNumber) {
		this.donorProjectNumber = donorProjectNumber;
	}
	

}