/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.financial;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

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
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name="financial_tx_class_type",
    discriminatorType= DiscriminatorType.STRING)
@DiscriminatorValue("Standard")
//@Table(name="FINANCIAL_TRANSACTION")
public class FinancialTransaction extends AbstractTranslateable<FinancialTransactionTranslation>
        implements FinancialTransactionTrnInterface, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2191303184851850777L;


    private BigDecimal amount;
    private BigDecimal interestRate;
    private BigDecimal secondInterestRate;


    private String donorProjectNumber;
    private String crsIdentificationNumber;
    private String geoTargetArea;


    private Boolean cpa;
    private Boolean programmeBasedApproach;
    private Boolean investment;
    private Boolean associatedFinancing;
    private Boolean freestandingTechnicalCooperation;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime commitmentDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime expectedStartDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime expectedCompletionDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime firstRepaymentDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime finalRepaymentDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime reportingYear;


    @Columns(columns = {@Column(name = "commitment_curr"), @Column(name = "commitment_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
    private BigMoney commitments;

    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentCurrencyUnit")
    private CurrencyUnit currency;

    @Columns(columns = { @Column(name = "amounts_extended_curr"),
			@Column(name = "amounts_extended_amount"),
			@Column(name = "amounts_extended_counter_curr"),
			@Column(name = "amounts_extended_rate") })
	@Type(type = "org.devgateway.eudevfin.financial.usertypes.PersistentBigMoneyWithCurrencyAndFixedRate")
	private BigMoneyWithCurrencyAndFixedRate amountsExtended;
    
    
    @Columns(columns = { @Column(name = "amounts_extended_current_curr"),
			@Column(name = "amounts_extended_current_amount"),
			@Column(name = "amounts_extended_current_counter_curr"),
			@Column(name = "amounts_extended_current_rate") })
	@Type(type = "org.devgateway.eudevfin.financial.usertypes.PersistentBigMoneyWithCurrencyAndFixedRate")
	private BigMoneyWithCurrencyAndFixedRate amountsExtendedCurrent;

    @Columns(columns = { @Column(name = "amounts_extended_year1_curr"),
			@Column(name = "amounts_extended_year1_amount"),
			@Column(name = "amounts_extended_year1_counter_curr"),
			@Column(name = "amounts_extended_year1_rate") })
	@Type(type = "org.devgateway.eudevfin.financial.usertypes.PersistentBigMoneyWithCurrencyAndFixedRate")
	private BigMoneyWithCurrencyAndFixedRate amountsExtendedYear1;

    @Columns(columns = { @Column(name = "amounts_extended_year2_curr"),
			@Column(name = "amounts_extended_year2_amount"),
			@Column(name = "amounts_extended_year2_counter_curr"),
			@Column(name = "amounts_extended_year2_rate") })
	@Type(type = "org.devgateway.eudevfin.financial.usertypes.PersistentBigMoneyWithCurrencyAndFixedRate")
	private BigMoneyWithCurrencyAndFixedRate amountsExtendedYear2;

    
    @Columns(columns = { @Column(name = "amounts_received_curr"),
			@Column(name = "amounts_received_amount"),
			@Column(name = "amounts_received_counter_curr"),
			@Column(name = "amounts_received_rate") })
	@Type(type = "org.devgateway.eudevfin.financial.usertypes.PersistentBigMoneyWithCurrencyAndFixedRate")
	private BigMoneyWithCurrencyAndFixedRate amountsReceived;

    
    @Columns(columns = { @Column(name = "amounts_untied_curr"),
			@Column(name = "amounts_untied_amount"),
			@Column(name = "amounts_untied_counter_curr"),
			@Column(name = "amounts_untied_rate") })
	@Type(type = "org.devgateway.eudevfin.financial.usertypes.PersistentBigMoneyWithCurrencyAndFixedRate")
	private BigMoneyWithCurrencyAndFixedRate amountsUntied;
    
    @Columns(columns = { @Column(name = "amounts_partially_untied_curr"),
			@Column(name = "amounts_partially_untied_amount"),
			@Column(name = "amounts_partially_untied_counter_curr"),
			@Column(name = "amounts_partially_untied_rate") })
	@Type(type = "org.devgateway.eudevfin.financial.usertypes.PersistentBigMoneyWithCurrencyAndFixedRate")
	private BigMoneyWithCurrencyAndFixedRate amountsPartiallyUntied;
    
    @Columns(columns = { @Column(name = "amounts_tied_curr"),
			@Column(name = "amounts_tied_amount"),
			@Column(name = "amounts_tied_counter_curr"),
			@Column(name = "amounts_tied_rate") })
	@Type(type = "org.devgateway.eudevfin.financial.usertypes.PersistentBigMoneyWithCurrencyAndFixedRate")
	private BigMoneyWithCurrencyAndFixedRate amountsTied;
    
    @Columns(columns = { @Column(name = "amounts_irtc_curr"),
			@Column(name = "amounts_irtc_amount"),
			@Column(name = "amounts_irtc_counter_curr"),
			@Column(name = "amounts_irtc_rate") })
	@Type(type = "org.devgateway.eudevfin.financial.usertypes.PersistentBigMoneyWithCurrencyAndFixedRate")
	private BigMoneyWithCurrencyAndFixedRate amountOfIRTC;

    @Columns(columns = { @Column(name = "amounts_exp_commit_curr"),
			@Column(name = "amounts_exp_commit_amount"),
			@Column(name = "amounts_exp_commit_counter_curr"),
			@Column(name = "amounts_exp_commit_rate") })
	@Type(type = "org.devgateway.eudevfin.financial.usertypes.PersistentBigMoneyWithCurrencyAndFixedRate")
	private BigMoneyWithCurrencyAndFixedRate projectAmountExpertCommitments;

    @Columns(columns = { @Column(name = "amounts_exp_extended_curr"),
			@Column(name = "amounts_exp_extended_amount"),
			@Column(name = "amounts_exp_extended_counter_curr"),
			@Column(name = "amounts_exp_extended_rate") })
	@Type(type = "org.devgateway.eudevfin.financial.usertypes.PersistentBigMoneyWithCurrencyAndFixedRate")
	private BigMoneyWithCurrencyAndFixedRate projectAmountExpertExtended;

    
    @Columns(columns = { @Column(name = "amounts_export_credit_curr"),
			@Column(name = "amounts_export_credit_amount"),
			@Column(name = "amounts_export_credit_counter_curr"),
			@Column(name = "amounts_export_credit_rate") })
	@Type(type = "org.devgateway.eudevfin.financial.usertypes.PersistentBigMoneyWithCurrencyAndFixedRate")
	private BigMoneyWithCurrencyAndFixedRate amountOfExportCreditInAFPackage;

    @Columns(columns = { @Column(name = "amounts_interest_received_curr"),
			@Column(name = "amounts_interest_received_amount"),
			@Column(name = "amounts_interest_received_counter_curr"),
			@Column(name = "amounts_interest_received_rate") })
	@Type(type = "org.devgateway.eudevfin.financial.usertypes.PersistentBigMoneyWithCurrencyAndFixedRate")
	private BigMoneyWithCurrencyAndFixedRate interestReceived;

    @Columns(columns = { @Column(name = "amounts_prin_disb_curr"),
			@Column(name = "amounts_prin_disb_amount"),
			@Column(name = "amounts_prin_disb_counter_curr"),
			@Column(name = "amounts_prin_disb_rate") })
	@Type(type = "org.devgateway.eudevfin.financial.usertypes.PersistentBigMoneyWithCurrencyAndFixedRate")
	private BigMoneyWithCurrencyAndFixedRate principalDisbursedOutstanding;
    

    @Columns(columns = { @Column(name = "amounts_arears_prin_curr"),
			@Column(name = "amounts_arears_prin_amount"),
			@Column(name = "amounts_arears_prin_counter_curr"),
			@Column(name = "amounts_arears_prin_rate") })
	@Type(type = "org.devgateway.eudevfin.financial.usertypes.PersistentBigMoneyWithCurrencyAndFixedRate")
	private BigMoneyWithCurrencyAndFixedRate arrearsOfPrincipal;

    @Columns(columns = { @Column(name = "amounts_arears_interest_curr"),
			@Column(name = "amounts_arears_interest_amount"),
			@Column(name = "amounts_arears_interest_counter_curr"),
			@Column(name = "amounts_arears_interest_rate") })
	@Type(type = "org.devgateway.eudevfin.financial.usertypes.PersistentBigMoneyWithCurrencyAndFixedRate")
	private BigMoneyWithCurrencyAndFixedRate arrearsOfInterest;
    
//    @ManyToOne
//    private RecipientCategory recipient;

    @ManyToOne
    private Area recipient;

    @ManyToOne
    private Category sector;

    @ManyToOne
    private Category natureOfSubmission;

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
    private Category numberOfRepaymentsAnnum;


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

    @ManyToOne
    private Category biMultilateral;


//	@Version	
//	public Long version = null;
//	
//	@ManyToOne(  optional= false )
//	@JoinColumn(name="organization_id")
//	private Organization sourceOrganization;

    @ManyToOne(optional = false)
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
//	@Override
//	public String toString() {
//		return "FinancialTransaction ["
//				+ (amount != null ? "amount=" + amount + ", " : "")
//				+ (amountsReceived != null ? "amountsReceived="
//						+ amountsReceived + ", " : "")
//				+ (commitments != null ? "commitments=" + commitments + ", "
//						: "")
//				+ (description != null ? "description=" + description : "")
//				+ "]";
//	}

    //TODO: remove and uncomment the original toString()
    //
    @Override
    public String toString() {
        return "\nFinancialTransaction{" +
                "\n   amount=" + amount +
                "\n   interestRate=" + interestRate +
                "\n   secondInterestRate=" + secondInterestRate +
                "\n   donorProjectNumber='" + donorProjectNumber + '\'' +
                "\n   crsIdentificationNumber='" + crsIdentificationNumber + '\'' +
                "\n   geoTargetArea='" + geoTargetArea + '\'' +
                "\n   cpa=" + cpa +
                "\n   programmeBasedApproach=" + programmeBasedApproach +
                "\n   investment=" + investment +
                "\n   associatedFinancing=" + associatedFinancing +
                "\n   commitmentDate=" + commitmentDate +
                "\n   expectedStartDate=" + expectedStartDate +
                "\n   expectedCompletionDate=" + expectedCompletionDate +
                "\n   firstRepaymentDate=" + firstRepaymentDate +
                "\n   finalRepaymentDate=" + finalRepaymentDate +
                "\n   reportingYear=" + reportingYear +
                "\n   currency=" + currency +
                "\n   amountsExtended=" + amountsExtended +
                "\n   amountsExtendedCurrent=" + amountsExtendedCurrent +
                "\n   amountsExtendedYear1=" + amountsExtendedYear1 +
                "\n   amountsExtendedYear2=" + amountsExtendedYear2 +
                "\n   amountsReceived=" + amountsReceived +
                "\n   amountsUntied=" + amountsUntied +
                "\n   amountsPartiallyUntied=" + amountsPartiallyUntied +
                "\n   amountsTied=" + amountsTied +
                "\n   amountOfIRTC=" + amountOfIRTC +
                "\n   projectAmountExpertCommitments=" + projectAmountExpertCommitments +
                "\n   projectAmountExpertExtended=" + projectAmountExpertExtended +
                "\n   amountOfExportCreditInAFPackage=" + amountOfExportCreditInAFPackage +
                "\n   interestReceived=" + interestReceived +
                "\n   principalDisbursedOutstanding=" + principalDisbursedOutstanding +
                "\n   arrearsOfPrincipal=" + arrearsOfPrincipal +
                "\n   arrearsOfInterest=" + arrearsOfInterest +
                "\n   recipient=" + recipient +
                "\n   sector=" + sector +
                "\n   natureOfSubmission=" + natureOfSubmission +
                "\n   odaType=" + odaType +
                "\n   typeOfFlow=" + typeOfFlow +
                "\n   typeOfFinance=" + typeOfFinance +
                "\n   administrativeType=" + administrativeType +
                "\n   typeOfAid=" + typeOfAid +
                "\n   typeOfRepayment=" + typeOfRepayment +
                "\n   numberOfRepaymentsAnnum=" + numberOfRepaymentsAnnum +
                "\n   biodiversity=" + biodiversity +
                "\n   climateChangeMitigation=" + climateChangeMitigation +
                "\n   climateChangeAdaptation=" + climateChangeAdaptation +
                "\n   desertification=" + desertification +
                "\n   genderEquality=" + genderEquality +
                "\n   aidToEnvironment=" + aidToEnvironment +
                "\n   pdgg=" + pdgg +
                "\n   tradeDevelopment=" + tradeDevelopment +
                "\n   reportingOrganization=" + reportingOrganization +
                "\n   extendingAgency=" + extendingAgency +
                "\n   channelOfDelivery=" + channelOfDelivery +
                "\n   channel=" + channel +
                '}';
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
    
    @Override
    public String getShortDescription() {
        return (String) this.get("shortDescription");
    }

    @Override
    public void setShortDescription(String shortDescription) {
        this.set("shortDescription", shortDescription);
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

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getSecondInterestRate() {
        return secondInterestRate;
    }

    public void setSecondInterestRate(BigDecimal secondInterestRate) {
        this.secondInterestRate = secondInterestRate;
    }

    public Boolean getProgrammeBasedApproach() {
        return programmeBasedApproach;
    }

    public void setProgrammeBasedApproach(Boolean programmeBasedApproach) {
        this.programmeBasedApproach = programmeBasedApproach;
    }

    public Boolean getInvestment() {
        return investment;
    }

    public void setInvestment(Boolean investment) {
        this.investment = investment;
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



//    public RecipientCategory getRecipient() {
//        return recipient;
//    }
//
//    public void setRecipient(RecipientCategory recipient) {
//        this.recipient = recipient;
//    }
    
    

    public BigMoneyWithCurrencyAndFixedRate getAmountsExtended() {
		return amountsExtended;
	}


	public void setAmountsExtended(BigMoneyWithCurrencyAndFixedRate amountsExtended) {
		this.amountsExtended = amountsExtended;
	}


	public BigMoneyWithCurrencyAndFixedRate getAmountsExtendedCurrent() {
		return amountsExtendedCurrent;
	}


	public void setAmountsExtendedCurrent(
			BigMoneyWithCurrencyAndFixedRate amountsExtendedCurrent) {
		this.amountsExtendedCurrent = amountsExtendedCurrent;
	}


	public BigMoneyWithCurrencyAndFixedRate getAmountsExtendedYear1() {
		return amountsExtendedYear1;
	}


	public void setAmountsExtendedYear1(
			BigMoneyWithCurrencyAndFixedRate amountsExtendedYear1) {
		this.amountsExtendedYear1 = amountsExtendedYear1;
	}


	public BigMoneyWithCurrencyAndFixedRate getAmountsExtendedYear2() {
		return amountsExtendedYear2;
	}


	public void setAmountsExtendedYear2(
			BigMoneyWithCurrencyAndFixedRate amountsExtendedYear2) {
		this.amountsExtendedYear2 = amountsExtendedYear2;
	}


	public BigMoneyWithCurrencyAndFixedRate getAmountsReceived() {
		return amountsReceived;
	}


	public void setAmountsReceived(BigMoneyWithCurrencyAndFixedRate amountsReceived) {
		this.amountsReceived = amountsReceived;
	}


	public BigMoneyWithCurrencyAndFixedRate getAmountsUntied() {
		return amountsUntied;
	}


	public void setAmountsUntied(BigMoneyWithCurrencyAndFixedRate amountsUntied) {
		this.amountsUntied = amountsUntied;
	}


	public BigMoneyWithCurrencyAndFixedRate getAmountsPartiallyUntied() {
		return amountsPartiallyUntied;
	}


	public void setAmountsPartiallyUntied(
			BigMoneyWithCurrencyAndFixedRate amountsPartiallyUntied) {
		this.amountsPartiallyUntied = amountsPartiallyUntied;
	}


	public BigMoneyWithCurrencyAndFixedRate getAmountsTied() {
		return amountsTied;
	}


	public void setAmountsTied(BigMoneyWithCurrencyAndFixedRate amountsTied) {
		this.amountsTied = amountsTied;
	}


	public BigMoneyWithCurrencyAndFixedRate getAmountOfIRTC() {
		return amountOfIRTC;
	}


	public void setAmountOfIRTC(BigMoneyWithCurrencyAndFixedRate amountOfIRTC) {
		this.amountOfIRTC = amountOfIRTC;
	}


	public BigMoneyWithCurrencyAndFixedRate getProjectAmountExpertCommitments() {
		return projectAmountExpertCommitments;
	}


	public void setProjectAmountExpertCommitments(
			BigMoneyWithCurrencyAndFixedRate projectAmountExpertCommitments) {
		this.projectAmountExpertCommitments = projectAmountExpertCommitments;
	}


	public BigMoneyWithCurrencyAndFixedRate getProjectAmountExpertExtended() {
		return projectAmountExpertExtended;
	}


	public void setProjectAmountExpertExtended(
			BigMoneyWithCurrencyAndFixedRate projectAmountExpertExtended) {
		this.projectAmountExpertExtended = projectAmountExpertExtended;
	}


	public BigMoneyWithCurrencyAndFixedRate getAmountOfExportCreditInAFPackage() {
		return amountOfExportCreditInAFPackage;
	}


	public void setAmountOfExportCreditInAFPackage(
			BigMoneyWithCurrencyAndFixedRate amountOfExportCreditInAFPackage) {
		this.amountOfExportCreditInAFPackage = amountOfExportCreditInAFPackage;
	}


	public BigMoneyWithCurrencyAndFixedRate getInterestReceived() {
		return interestReceived;
	}


	public void setInterestReceived(
			BigMoneyWithCurrencyAndFixedRate interestReceived) {
		this.interestReceived = interestReceived;
	}


	public BigMoneyWithCurrencyAndFixedRate getPrincipalDisbursedOutstanding() {
		return principalDisbursedOutstanding;
	}


	public void setPrincipalDisbursedOutstanding(
			BigMoneyWithCurrencyAndFixedRate principalDisbursedOutstanding) {
		this.principalDisbursedOutstanding = principalDisbursedOutstanding;
	}


	public BigMoneyWithCurrencyAndFixedRate getArrearsOfPrincipal() {
		return arrearsOfPrincipal;
	}


	public void setArrearsOfPrincipal(
			BigMoneyWithCurrencyAndFixedRate arrearsOfPrincipal) {
		this.arrearsOfPrincipal = arrearsOfPrincipal;
	}


	public BigMoneyWithCurrencyAndFixedRate getArrearsOfInterest() {
		return arrearsOfInterest;
	}


	public void setArrearsOfInterest(
			BigMoneyWithCurrencyAndFixedRate arrearsOfInterest) {
		this.arrearsOfInterest = arrearsOfInterest;
	}


	public Area getRecipient() {
		return recipient;
	}


	public void setRecipient(Area recipient) {
		this.recipient = recipient;
	}


	public Category getSector() {
        return sector;
    }

    public void setSector(Category sector) {
        this.sector = sector;
    }

    public Category getNatureOfSubmission() {
        return natureOfSubmission;
    }

    public void setNatureOfSubmission(Category natureOfSubmission) {
        this.natureOfSubmission = natureOfSubmission;
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

    public Category getNumberOfRepaymentsAnnum() {
        return numberOfRepaymentsAnnum;
    }

    public void setNumberOfRepaymentsAnnum(Category numberOfRepaymentsAnnum) {
        this.numberOfRepaymentsAnnum = numberOfRepaymentsAnnum;
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

    public Category getBiMultilateral() {
        return biMultilateral;
    }

    public void setBiMultilateral(Category biMultilateral) {
        this.biMultilateral = biMultilateral;
    }

    public Boolean getFreestandingTechnicalCooperation() {
        return freestandingTechnicalCooperation;
    }

    public void setFreestandingTechnicalCooperation(Boolean freestandingTechnicalCooperation) {
        this.freestandingTechnicalCooperation = freestandingTechnicalCooperation;
    }

	public BigMoney getCommitments() {
		return commitments;
	}


	public void setCommitments(BigMoney commitments) {
		this.commitments = commitments;
	}
    
}
