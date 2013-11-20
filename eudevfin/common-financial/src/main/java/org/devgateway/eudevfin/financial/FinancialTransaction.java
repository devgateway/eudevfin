package org.devgateway.eudevfin.financial;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.devgateway.eudevfin.financial.translate.FinancialTransactionTranslation;
import org.devgateway.eudevfin.financial.translate.FinancialTransactionTrnInterface;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.joda.money.BigMoney;
import org.joda.time.LocalDateTime;
@Entity
@Audited
@Table(name="FINANCIAL_TRANSACTION")
public class FinancialTransaction extends AbstractTranslateable<FinancialTransactionTranslation> 
							implements FinancialTransactionTrnInterface,Serializable{
	
	private BigDecimal amount;

	
	private String donorProjectNumber;
	private String crsIdentificationNumber;
	private String geoTargetArea;
	
	private Boolean cpa;
	
	@Column
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private LocalDateTime commitmentDate;
	
	@Column
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private LocalDateTime expectedStartDate;
	
	@Columns(columns={@Column(name="commitment"),@Column(name="commitment_usd")})
	@Type(type="org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
	private BigMoney commitments;
	
//	@Version	
//	public Long version = null;
//	
	@ManyToOne(  optional= false )
	@JoinColumn(name="ORGANIZATION_ID")
	private Organization sourceOrganization;
	
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
	
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	@Override
	public String getDescription() {
		return (String) this.get("description");
		
	}
	@Override
	public void setDescription(String description) {
		this.set("description", description);
	}

	public Organization getSourceOrganization() {
		return sourceOrganization;
	}
	public void setSourceOrganization(Organization sourceOrganization) {
		this.sourceOrganization = sourceOrganization;
	}
	public String getDonorProjectNumber() {
		return donorProjectNumber;
	}
	public void setDonorProjectNumber(String donorProjectNumber) {
		this.donorProjectNumber = donorProjectNumber;
	}
	public String getCrsIdentificationNumber() {
		return crsIdentificationNumber;
	}
	public void setCrsIdentificationNumber(String crsIdentificationNumber) {
		this.crsIdentificationNumber = crsIdentificationNumber;
	}
	public String getGeoTargetArea() {
		return geoTargetArea;
	}
	public void setGeoTargetArea(String geoTargetArea) {
		this.geoTargetArea = geoTargetArea;
	}
	public Boolean getCpa() {
		return cpa;
	}
	public void setCpa(Boolean cpa) {
		this.cpa = cpa;
	}
	public LocalDateTime getCommitmentDate() {
		return commitmentDate;
	}
	public void setCommitmentDate(LocalDateTime commitmentDate) {
		this.commitmentDate = commitmentDate;
	}
	public LocalDateTime getExpectedStartDate() {
		return expectedStartDate;
	}
	public void setExpectedStartDate(LocalDateTime expectedStartDate) {
		this.expectedStartDate = expectedStartDate;
	}
	public BigMoney getCommitments() {
		return commitments;
	}
	public void setCommitments(BigMoney commitments) {
		this.commitments = commitments;
	}
	
	
}