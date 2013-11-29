package org.devgateway.eudevfin.financial;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.devgateway.eudevfin.financial.translate.FinancialTransactionTranslation;
import org.devgateway.eudevfin.financial.translate.FinancialTransactionTrnInterface;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.joda.time.LocalDateTime;
@Entity
@Audited
//@Table(name="FINANCIAL_TRANSACTION")
public class FinancialTransaction extends AbstractTranslateable<FinancialTransactionTranslation> 
							implements FinancialTransactionTrnInterface,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2191303184851850777L;
	
	
	private BigDecimal amount;
	private BigDecimal interestRate;
	private BigDecimal seciondInterestRate;
	
	
	private String donorProjectNumber;
	private String crsIdentificationNumber;
	private String geoTargetArea;
	
	@Lob
	private String shortDescription;
	@Lob
	private String description;
	
	private Boolean cpa;
	private Boolean programmeBasedApproach;
	private Boolean investement;
	private Boolean associatedFinancing;
	
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime commitmentDate;
	
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime expectedStartDate;
	
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime expectedCompletionDate;
	
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime firstRepaymentDate;
	
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime finalRepaymentDate;
	
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime reportingYear;
	
	
	
	
	
	@Columns(columns={@Column(name="commitment_curr"),@Column(name="commitment_amount")})
	@Type(type="org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
	private BigMoney commitments;
	
	@Type(type="org.jadira.usertype.moneyandcurrency.joda.PersistentCurrencyUnit")
	private CurrencyUnit currency;
	
	@Columns(columns={@Column(name="amounts_extended_curr"),@Column(name="amounts_extended_amount")})
	@Type(type="org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
	private BigMoney amountsExtended;
	
	@Columns(columns={@Column(name="amounts_extended_current_curr"),@Column(name="amounts_extended_current_amount")})
	@Type(type="org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
	private BigMoney amountsExtendedCurrent;
	
	@Columns(columns={@Column(name="amounts_extended_year1_curr"),@Column(name="amounts_extended_year1_amount")})
	@Type(type="org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
	private BigMoney amountsExtendedYear1;
	
	@Columns(columns={@Column(name="amounts_extended_year2_curr"),@Column(name="amounts_extended_year2_amount")})
	@Type(type="org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
	private BigMoney amountsExtendedYear2;
	
	@Columns(columns={@Column(name="amounts_received_curr"),@Column(name="amounts_received_amount")})
	@Type(type="org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
	private BigMoney amountsReceived;
	
	@Columns(columns={@Column(name="amounts_untied_curr"),@Column(name="amounts_untied_amount")})
	@Type(type="org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
	private BigMoney amountsUntied;
	
	@Columns(columns={@Column(name="amounts_partially_untied_curr"),@Column(name="amounts_partially_untied_amount")})
	@Type(type="org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
	private BigMoney amountsPartiallyUntied;
	
	@Columns(columns={@Column(name="amounts_tied_curr"),@Column(name="amounts_tied_amount")})
	@Type(type="org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
	private BigMoney amountsTied;
	
	@Columns(columns={@Column(name="amounts_irtc_curr"),@Column(name="amounts_irtc_amount")})
	@Type(type="org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
	private BigMoney amountOfIRTC;
	
	@Columns(columns={@Column(name="amounts_exp_commit_curr"),@Column(name="amounts_exp_commit_amount")})
	@Type(type="org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
	private BigMoney projectAmountExpertCommitments;
	
	@Columns(columns={@Column(name="amounts_exp_extended_curr"),@Column(name="amounts_exp_extended_amount")})
	@Type(type="org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
	private BigMoney projectAmountExpertExtended;
	
	@Columns(columns={@Column(name="amounts_export_credit_curr"),@Column(name="amounts_exp_export_credit_amount")})
	@Type(type="org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
	private BigMoney amountOfExportCreditInAFPackage;
	
	@Columns(columns={@Column(name="amounts_interest_received_curr"),@Column(name="amounts_interest_received_amount")})
	@Type(type="org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
	private BigMoney interestReceived;
	
	@Columns(columns={@Column(name="amounts_prin_disb_curr"),@Column(name="amounts_prin_disb_amount")})
	@Type(type="org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
	private BigMoney principalDisbursedOutstanding;
	
	@Columns(columns={@Column(name="amounts_arears_prin_curr"),@Column(name="amounts_arears_prin_amount")})
	@Type(type="org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
	private BigMoney arearsOfPrincipal;
	
	@Columns(columns={@Column(name="amounts_arears_interest_curr"),@Column(name="amounts_arears_interest_amount")})
	@Type(type="org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
	private BigMoney arearsOfInterest;
	
	@ManyToOne
	private RecipientCategory recipient;
	
	
	
	@ManyToOne
	private Category sector; 
	
	@ManyToOne
	private Category natureOfSumbission;
	
	@ManyToOne
	private Category odaType;
	
	@ManyToOne
	private Category typeOfFlow;
	
	@ManyToOne
	private Category typeOfFinance;
	
	@ManyToOne
	private Category administrativeType;
	
	@ManyToOne
	private Category typeOfAid;
	
	@ManyToOne
	private Category typeOfRepayment;
	
	@ManyToOne
	private Category numberOfRepaymentsAnum;
	
	
	@ManyToOne
	private Category biodiversity;
	
	@ManyToOne
	private Category climateChangeMitigation;
	
	@ManyToOne
	private Category climateChangeAdaptation;
	
	@ManyToOne
	private Category desertification;
	
	@ManyToOne
	private Category genderEquality;
	
	@ManyToOne
	private Category aidToEnvironment;
	
	@ManyToOne
	private Category pdgg;
	
	@ManyToOne
	private Category tradeDevelopment;
	
	
//	@Version	
//	public Long version = null;
//	
//	@ManyToOne(  optional= false )
//	@JoinColumn(name="organization_id")
//	private Organization sourceOrganization;
	
	@ManyToOne(  optional= false )
	private Organization reportingOrganization;
	
	@ManyToOne
	private Organization extendingAgency;
	
	@ManyToOne
	private Organization channelOfDelivery;
	
	
	
	
	@ManyToOne
	private ChannelCategory channel;
	
	
	
//	public Long getVersion() {
//		return version;
//	}
//	public void setVersion(Long version) {
//		this.version = version;
//	}

	
	
	@Override
	protected FinancialTransactionTranslation newTranslationInstance() {
		return new FinancialTransactionTranslation();
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FinancialTransaction ["
				+ (amount != null ? "amount=" + amount + ", " : "")
				+ (amountsReceived != null ? "amountsReceived="
						+ amountsReceived + ", " : "")
				+ (commitments != null ? "commitments=" + commitments + ", "
						: "")
				+ (description != null ? "description=" + description : "")
				+ "]";
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
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	public BigDecimal getSeciondInterestRate() {
		return seciondInterestRate;
	}
	public void setSeciondInterestRate(BigDecimal seciondInterestRate) {
		this.seciondInterestRate = seciondInterestRate;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public Boolean getProgrammeBasedApproach() {
		return programmeBasedApproach;
	}
	public void setProgrammeBasedApproach(Boolean programmeBasedApproach) {
		this.programmeBasedApproach = programmeBasedApproach;
	}
	public Boolean getInvestement() {
		return investement;
	}
	public void setInvestement(Boolean investement) {
		this.investement = investement;
	}
	public Boolean getAssociatedFinancing() {
		return associatedFinancing;
	}
	public void setAssociatedFinancing(Boolean associatedFinancing) {
		this.associatedFinancing = associatedFinancing;
	}
	public LocalDateTime getExpectedCompletionDate() {
		return expectedCompletionDate;
	}
	public void setExpectedCompletionDate(LocalDateTime expectedCompletionDate) {
		this.expectedCompletionDate = expectedCompletionDate;
	}
	public LocalDateTime getFirstRepaymentDate() {
		return firstRepaymentDate;
	}
	public void setFirstRepaymentDate(LocalDateTime firstRepaymentDate) {
		this.firstRepaymentDate = firstRepaymentDate;
	}
	public LocalDateTime getFinalRepaymentDate() {
		return finalRepaymentDate;
	}
	public void setFinalRepaymentDate(LocalDateTime finalRepaymentDate) {
		this.finalRepaymentDate = finalRepaymentDate;
	}
	public LocalDateTime getReportingYear() {
		return reportingYear;
	}
	public void setReportingYear(LocalDateTime reportingYear) {
		this.reportingYear = reportingYear;
	}
	public CurrencyUnit getCurrency() {
		return currency;
	}
	public void setCurrency(CurrencyUnit currency) {
		this.currency = currency;
	}
	public BigMoney getAmountsExtended() {
		return amountsExtended;
	}
	public void setAmountsExtended(BigMoney amountsExtended) {
		this.amountsExtended = amountsExtended;
	}
	public BigMoney getAmountsExtendedCurrent() {
		return amountsExtendedCurrent;
	}
	public void setAmountsExtendedCurrent(BigMoney amountsExtendedCurrent) {
		this.amountsExtendedCurrent = amountsExtendedCurrent;
	}
	public BigMoney getAmountsExtendedYear1() {
		return amountsExtendedYear1;
	}
	public void setAmountsExtendedYear1(BigMoney amountsExtendedYear1) {
		this.amountsExtendedYear1 = amountsExtendedYear1;
	}
	public BigMoney getAmountsExtendedYear2() {
		return amountsExtendedYear2;
	}
	public void setAmountsExtendedYear2(BigMoney amountsExtendedYear2) {
		this.amountsExtendedYear2 = amountsExtendedYear2;
	}
	public BigMoney getAmountsReceived() {
		return amountsReceived;
	}
	public void setAmountsReceived(BigMoney amountsReceived) {
		this.amountsReceived = amountsReceived;
	}
	public BigMoney getAmountsUntied() {
		return amountsUntied;
	}
	public void setAmountsUntied(BigMoney amountsUntied) {
		this.amountsUntied = amountsUntied;
	}
	public BigMoney getAmountsPartiallyUntied() {
		return amountsPartiallyUntied;
	}
	public void setAmountsPartiallyUntied(BigMoney amountsPartiallyUntied) {
		this.amountsPartiallyUntied = amountsPartiallyUntied;
	}
	public BigMoney getAmountsTied() {
		return amountsTied;
	}
	public void setAmountsTied(BigMoney amountsTied) {
		this.amountsTied = amountsTied;
	}
	public BigMoney getAmountOfIRTC() {
		return amountOfIRTC;
	}
	public void setAmountOfIRTC(BigMoney amountOfIRTC) {
		this.amountOfIRTC = amountOfIRTC;
	}
	public BigMoney getProjectAmountExpertCommitments() {
		return projectAmountExpertCommitments;
	}
	public void setProjectAmountExpertCommitments(
			BigMoney projectAmountExpertCommitments) {
		this.projectAmountExpertCommitments = projectAmountExpertCommitments;
	}
	public BigMoney getProjectAmountExpertExtended() {
		return projectAmountExpertExtended;
	}
	public void setProjectAmountExpertExtended(BigMoney projectAmountExpertExtended) {
		this.projectAmountExpertExtended = projectAmountExpertExtended;
	}
	public BigMoney getAmountOfExportCreditInAFPackage() {
		return amountOfExportCreditInAFPackage;
	}
	public void setAmountOfExportCreditInAFPackage(
			BigMoney amountOfExportCreditInAFPackage) {
		this.amountOfExportCreditInAFPackage = amountOfExportCreditInAFPackage;
	}
	public BigMoney getInterestReceived() {
		return interestReceived;
	}
	public void setInterestReceived(BigMoney interestReceived) {
		this.interestReceived = interestReceived;
	}
	public BigMoney getPrincipalDisbursedOutstanding() {
		return principalDisbursedOutstanding;
	}
	public void setPrincipalDisbursedOutstanding(
			BigMoney principalDisbursedOutstanding) {
		this.principalDisbursedOutstanding = principalDisbursedOutstanding;
	}
	public BigMoney getArearsOfPrincipal() {
		return arearsOfPrincipal;
	}
	public void setArearsOfPrincipal(BigMoney arearsOfPrincipal) {
		this.arearsOfPrincipal = arearsOfPrincipal;
	}
	public BigMoney getArearsOfInterest() {
		return arearsOfInterest;
	}
	public void setArearsOfInterest(BigMoney arearsOfInterest) {
		this.arearsOfInterest = arearsOfInterest;
	}
	public RecipientCategory getRecipient() {
		return recipient;
	}
	public void setRecipient(RecipientCategory recipient) {
		this.recipient = recipient;
	}
	public Category getSector() {
		return sector;
	}
	public void setSector(Category sector) {
		this.sector = sector;
	}
	public Category getNatureOfSumbission() {
		return natureOfSumbission;
	}
	public void setNatureOfSumbission(Category natureOfSumbission) {
		this.natureOfSumbission = natureOfSumbission;
	}
	public Category getOdaType() {
		return odaType;
	}
	public void setOdaType(Category odaType) {
		this.odaType = odaType;
	}
	public Category getTypeOfFlow() {
		return typeOfFlow;
	}
	public void setTypeOfFlow(Category typeOfFlow) {
		this.typeOfFlow = typeOfFlow;
	}
	public Category getTypeOfFinance() {
		return typeOfFinance;
	}
	public void setTypeOfFinance(Category typeOfFinance) {
		this.typeOfFinance = typeOfFinance;
	}
	public Category getAdministrativeType() {
		return administrativeType;
	}
	public void setAdministrativeType(Category administrativeType) {
		this.administrativeType = administrativeType;
	}
	public Category getTypeOfAid() {
		return typeOfAid;
	}
	public void setTypeOfAid(Category typeOfAid) {
		this.typeOfAid = typeOfAid;
	}
	public Category getTypeOfRepayment() {
		return typeOfRepayment;
	}
	public void setTypeOfRepayment(Category typeOfRepayment) {
		this.typeOfRepayment = typeOfRepayment;
	}
	public Category getNumberOfRepaymentsAnum() {
		return numberOfRepaymentsAnum;
	}
	public void setNumberOfRepaymentsAnum(Category numberOfRepaymentsAnum) {
		this.numberOfRepaymentsAnum = numberOfRepaymentsAnum;
	}
	public Category getBiodiversity() {
		return biodiversity;
	}
	public void setBiodiversity(Category biodiversity) {
		this.biodiversity = biodiversity;
	}
	public Category getClimateChangeMitigation() {
		return climateChangeMitigation;
	}
	public void setClimateChangeMitigation(Category climateChangeMitigation) {
		this.climateChangeMitigation = climateChangeMitigation;
	}
	public Category getClimateChangeAdaptation() {
		return climateChangeAdaptation;
	}
	public void setClimateChangeAdaptation(Category climateChangeAdaptation) {
		this.climateChangeAdaptation = climateChangeAdaptation;
	}
	public Category getDesertification() {
		return desertification;
	}
	public void setDesertification(Category desertification) {
		this.desertification = desertification;
	}
	public Category getGenderEquality() {
		return genderEquality;
	}
	public void setGenderEquality(Category genderEquality) {
		this.genderEquality = genderEquality;
	}
	public Category getAidToEnvironment() {
		return aidToEnvironment;
	}
	public void setAidToEnvironment(Category aidToEnvironment) {
		this.aidToEnvironment = aidToEnvironment;
	}
	public Category getPdgg() {
		return pdgg;
	}
	public void setPdgg(Category pdgg) {
		this.pdgg = pdgg;
	}
	public Category getTradeDevelopment() {
		return tradeDevelopment;
	}
	public void setTradeDevelopment(Category tradeDevelopment) {
		this.tradeDevelopment = tradeDevelopment;
	}
	public Organization getReportingOrganization() {
		return reportingOrganization;
	}
	public void setReportingOrganization(Organization reportingOrganization) {
		this.reportingOrganization = reportingOrganization;
	}
	public Organization getExtendingAgency() {
		return extendingAgency;
	}
	public void setExtendingAgency(Organization extendingAgency) {
		this.extendingAgency = extendingAgency;
	}
	public Organization getChannelOfDelivery() {
		return channelOfDelivery;
	}
	public void setChannelOfDelivery(Organization channelOfDelivery) {
		this.channelOfDelivery = channelOfDelivery;
	}
	public ChannelCategory getChannel() {
		return channel;
	}
	public void setChannel(ChannelCategory channel) {
		this.channel = channel;
	}
	
	
}