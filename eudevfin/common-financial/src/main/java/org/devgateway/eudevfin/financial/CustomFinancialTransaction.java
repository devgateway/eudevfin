/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.financial;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.devgateway.eudevfin.auth.common.domain.PersistedUserGroup;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.joda.time.LocalDateTime;

@Entity
@Audited
@DiscriminatorValue("Custom")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CustomFinancialTransaction extends FinancialTransaction {

    /**
     *
     */
    private static final long serialVersionUID = -3864606312112731784L;

    /**
     * @see SB##BILATERAL_ODA_ADVANCE_QUESTIONNAIRE
     */
	@Index(name="customfinancialtransaction_formtype_idx")
    private String formType;
	
	@Index(name="customfinancialtransaction_draft_idx")
    private Boolean draft = true;

	@Index(name="customfinancialtransaction_approved_idx")
    private Boolean approved = false;
	
    private Boolean projectCoFinanced;

    @Columns(columns = {@Column(name = "future_debt_principal_curr"),
            @Column(name = "future_debt_principal_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
    private BigMoney futureDebtPrincipal;

    @Columns(columns = {@Column(name = "future_debt_interest_curr"),
            @Column(name = "future_debt_interest_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
    private BigMoney futureDebtInterest;

    @Columns(columns = {@Column(name = "budget_mtef_disb_curr"),
            @Column(name = "budget_mtef_disb_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
    private BigMoney budgetMTEFDisbursement;

    @Columns(columns = {@Column(name = "budget_mtef_disb_p1_curr"),
            @Column(name = "budget_mtef_disb_p1_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
    private BigMoney budgetMTEFDisbursementP1;

    @Columns(columns = {@Column(name = "budget_mtef_disb_p2_curr"),
            @Column(name = "budget_mtef_disb_p2_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
    private BigMoney budgetMTEFDisbursementP2;

    @Columns(columns = {@Column(name = "budget_mtef_disb_p3_curr"),
            @Column(name = "budget_mtef_disb_p3_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
    private BigMoney budgetMTEFDisbursementP3;

    @Columns(columns = {@Column(name = "budget_mtef_disb_p4_curr"),
            @Column(name = "budget_mtef_disb_p4_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
    private BigMoney budgetMTEFDisbursementP4;


    @Columns(columns = {@Column(name = "first_agency_curr"),
            @Column(name = "first_agency_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
    private BigMoney firstAgencyAmount;

    @Columns(columns = {@Column(name = "second_agency_curr"),
            @Column(name = "second_agency_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
    private BigMoney secondAgencyAmount;

    @Columns(columns = {@Column(name = "third_agency_curr"),
            @Column(name = "third_agency_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
    private BigMoney thirdAgencyAmount;


    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime dataAsPerDate;
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime phasingOutYear;

    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentCurrencyUnit")
    private CurrencyUnit firstAgencyCurrency;
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentCurrencyUnit")
    private CurrencyUnit secondAgencyCurrency;
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentCurrencyUnit")
    private CurrencyUnit thirdAgencyCurrency;

    @ManyToOne
    private Category rmnch;
    @ManyToOne
    private Category recipientPriority;
    
    @ManyToOne
    private PersistedUserGroup persistedUserGroup;
    
    @ManyToOne
    private Category LevelOfCertainty;


    private String budgetCode;
    private String budgetLine;
    private String budgetActivity;
    private String budgetMTEFCode;
    private String budgetMTEFLine;
    private String budgetMTEFActivity;
    private String budgetMTEFCodeP1;
    private String budgetMTEFLineP1;
    private String budgetMTEFActivityP1;
    private String budgetMTEFCodeP2;
    private String budgetMTEFLineP2;
    private String budgetMTEFActivityP2;
    private String budgetMTEFCodeP3;
    private String budgetMTEFLineP3;
    private String budgetMTEFActivityP3;
    private String budgetMTEFCodeP4;
    private String budgetMTEFLineP4;
    private String budgetMTEFActivityP4;
    private String otherComments;

//    DEPRECATED: because we're just going to store the rate as BigDecimal
//    @Columns(columns={@Column(name="fixed_rate_base_currency"),@Column(name="fixed_rate_counter_currency"),@Column(name="fixed_rate")})
//	  @Type(type="org.jadira.usertype.exchangerate.joda.PersistentExchangeRate")
//    private ExchangeRate fixedExchangeRate;


    private BigDecimal fixedRate;

    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentCurrencyUnit")
    private CurrencyUnit otherCurrency;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<FileWrapper> uploadDocumentation;


    public Set<FileWrapper> getUploadDocumentation() {
        return this.uploadDocumentation;
    }

	public void setUploadDocumentation(final Set<FileWrapper> uploadDocumentation) {
		if (this.uploadDocumentation == null)
			this.uploadDocumentation = uploadDocumentation;
		else {
			this.uploadDocumentation.retainAll(uploadDocumentation);
			if(uploadDocumentation!=null) this.uploadDocumentation.addAll(uploadDocumentation);
		}
	}

    public Boolean getDraft() {
        return this.draft;
    }

    public void setDraft(final Boolean draft) {
        this.draft = draft;
    }

    public String getFormType() {
        return this.formType;
    }

    public void setFormType(final String formType) {
        this.formType = formType;
    }

    public BigDecimal getFixedRate() {
        return this.fixedRate;
    }

    public void setFixedRate(final BigDecimal fixedRate) {
        this.fixedRate = fixedRate;
    }

    public CurrencyUnit getOtherCurrency() {
        return this.otherCurrency;
    }

    public void setOtherCurrency(final CurrencyUnit otherCurrency) {
        this.otherCurrency = otherCurrency;
    }

    public LocalDateTime getDataAsPerDate() {
        return this.dataAsPerDate;
    }

    public void setDataAsPerDate(final LocalDateTime dataAsPerDate) {
        this.dataAsPerDate = dataAsPerDate;
    }

    public BigMoney getFutureDebtPrincipal() {
        return this.futureDebtPrincipal;
    }

    public void setFutureDebtPrincipal(final BigMoney futureDebtPrincipal) {
        this.futureDebtPrincipal = futureDebtPrincipal;
    }

    public BigMoney getFutureDebtInterest() {
        return this.futureDebtInterest;
    }

    public void setFutureDebtInterest(final BigMoney futureDebtInterest) {
        this.futureDebtInterest = futureDebtInterest;
    }

    public Boolean getProjectCoFinanced() {
        return this.projectCoFinanced;
    }

    public void setProjectCoFinanced(final Boolean projectCoFinanced) {
        this.projectCoFinanced = projectCoFinanced;
    }

    public String getFirstCoFinancingAgency() {
        return (String) this.get("firstCoFinancingAgency");
    }

    public void setFirstCoFinancingAgency(final String firstCoFinancingAgency) {
    	   this.set("firstCoFinancingAgency", firstCoFinancingAgency);
    }

    public String getSecondCoFinancingAgency() {
        return (String) this.get("secondCoFinancingAgency");
    }

    public void setSecondCoFinancingAgency(final String secondCoFinancingAgency) {
    	 this.set("secondCoFinancingAgency", secondCoFinancingAgency);
    }

    public String getThirdCoFinancingAgency() {
    	return (String) this.get("thirdCoFinancingAgency");
    }

    public void setThirdCoFinancingAgency(final String thirdCoFinancingAgency) {
    	 this.set("thirdCoFinancingAgency", thirdCoFinancingAgency);
    }

    public CurrencyUnit getFirstAgencyCurrency() {
        return this.firstAgencyCurrency;
    }

    public void setFirstAgencyCurrency(final CurrencyUnit firstAgencyCurrency) {
        this.firstAgencyCurrency = firstAgencyCurrency;
    }

    public CurrencyUnit getSecondAgencyCurrency() {
        return this.secondAgencyCurrency;
    }

    public void setSecondAgencyCurrency(final CurrencyUnit secondAgencyCurrency) {
        this.secondAgencyCurrency = secondAgencyCurrency;
    }

    public CurrencyUnit getThirdAgencyCurrency() {
        return this.thirdAgencyCurrency;
    }

    public void setThirdAgencyCurrency(final CurrencyUnit thirdAgencyCurrency) {
        this.thirdAgencyCurrency = thirdAgencyCurrency;
    }

    public BigMoney getFirstAgencyAmount() {
        return this.firstAgencyAmount;
    }

    public void setFirstAgencyAmount(final BigMoney firstAgencyAmount) {
        this.firstAgencyAmount = firstAgencyAmount;
    }

    public BigMoney getSecondAgencyAmount() {
        return this.secondAgencyAmount;
    }

    public void setSecondAgencyAmount(final BigMoney secondAgencyAmount) {
        this.secondAgencyAmount = secondAgencyAmount;
    }

    public BigMoney getThirdAgencyAmount() {
        return this.thirdAgencyAmount;
    }

    public void setThirdAgencyAmount(final BigMoney thirdAgencyAmount) {
        this.thirdAgencyAmount = thirdAgencyAmount;
    }

    public Category getRmnch() {
        return this.rmnch;
    }

    public void setRmnch(final Category rmnch) {
        this.rmnch = rmnch;
    }

    public String getBudgetCode() {
        return this.budgetCode;
    }

    public void setBudgetCode(final String budgetCode) {
        this.budgetCode = budgetCode;
    }

    public String getBudgetLine() {
        return this.budgetLine;
    }

    public void setBudgetLine(final String budgetLine) {
        this.budgetLine = budgetLine;
    }

    public String getBudgetActivity() {
        return this.budgetActivity;
    }

    public void setBudgetActivity(final String budgetActivity) {
        this.budgetActivity = budgetActivity;
    }

    public Category getRecipientPriority() {
        return this.recipientPriority;
    }

    public void setRecipientPriority(final Category recipientPriority) {
        this.recipientPriority = recipientPriority;
    }

    public LocalDateTime getPhasingOutYear() {
        return this.phasingOutYear;
    }

    public void setPhasingOutYear(final LocalDateTime phasingOutYear) {
        this.phasingOutYear = phasingOutYear;
    }

    public BigMoney getBudgetMTEFDisbursement() {
        return this.budgetMTEFDisbursement;
    }

    public void setBudgetMTEFDisbursement(final BigMoney budgetMTEFDisbursement) {
        this.budgetMTEFDisbursement = budgetMTEFDisbursement;
    }

    public BigMoney getBudgetMTEFDisbursementP1() {
        return this.budgetMTEFDisbursementP1;
    }

    public void setBudgetMTEFDisbursementP1(final BigMoney budgetMTEFDisbursementP1) {
        this.budgetMTEFDisbursementP1 = budgetMTEFDisbursementP1;
    }

    public BigMoney getBudgetMTEFDisbursementP2() {
        return this.budgetMTEFDisbursementP2;
    }

    public void setBudgetMTEFDisbursementP2(final BigMoney budgetMTEFDisbursementP2) {
        this.budgetMTEFDisbursementP2 = budgetMTEFDisbursementP2;
    }

    public BigMoney getBudgetMTEFDisbursementP3() {
        return this.budgetMTEFDisbursementP3;
    }

    public void setBudgetMTEFDisbursementP3(final BigMoney budgetMTEFDisbursementP3) {
        this.budgetMTEFDisbursementP3 = budgetMTEFDisbursementP3;
    }

    public BigMoney getBudgetMTEFDisbursementP4() {
        return this.budgetMTEFDisbursementP4;
    }

    public void setBudgetMTEFDisbursementP4(final BigMoney budgetMTEFDisbursementP4) {
        this.budgetMTEFDisbursementP4 = budgetMTEFDisbursementP4;
    }

    public String getBudgetMTEFCode() {
        return this.budgetMTEFCode;
    }

    public void setBudgetMTEFCode(final String budgetMTEFCode) {
        this.budgetMTEFCode = budgetMTEFCode;
    }

    public String getBudgetMTEFLine() {
        return this.budgetMTEFLine;
    }

    public void setBudgetMTEFLine(final String budgetMTEFLine) {
        this.budgetMTEFLine = budgetMTEFLine;
    }

    public String getBudgetMTEFActivity() {
        return this.budgetMTEFActivity;
    }

    public void setBudgetMTEFActivity(final String budgetMTEFActivity) {
        this.budgetMTEFActivity = budgetMTEFActivity;
    }

    public String getBudgetMTEFCodeP1() {
        return this.budgetMTEFCodeP1;
    }

    public void setBudgetMTEFCodeP1(final String budgetMTEFCodeP1) {
        this.budgetMTEFCodeP1 = budgetMTEFCodeP1;
    }

    public String getBudgetMTEFLineP1() {
        return this.budgetMTEFLineP1;
    }

    public void setBudgetMTEFLineP1(final String budgetMTEFLineP1) {
        this.budgetMTEFLineP1 = budgetMTEFLineP1;
    }

    public String getBudgetMTEFActivityP1() {
        return this.budgetMTEFActivityP1;
    }

    public void setBudgetMTEFActivityP1(final String budgetMTEFActivityP1) {
        this.budgetMTEFActivityP1 = budgetMTEFActivityP1;
    }

    public String getBudgetMTEFCodeP2() {
        return this.budgetMTEFCodeP2;
    }

    public void setBudgetMTEFCodeP2(final String budgetMTEFCodeP2) {
        this.budgetMTEFCodeP2 = budgetMTEFCodeP2;
    }

    public String getBudgetMTEFLineP2() {
        return this.budgetMTEFLineP2;
    }

    public void setBudgetMTEFLineP2(final String budgetMTEFLineP2) {
        this.budgetMTEFLineP2 = budgetMTEFLineP2;
    }

    public String getBudgetMTEFActivityP2() {
        return this.budgetMTEFActivityP2;
    }

    public void setBudgetMTEFActivityP2(final String budgetMTEFActivityP2) {
        this.budgetMTEFActivityP2 = budgetMTEFActivityP2;
    }

    public String getBudgetMTEFCodeP3() {
        return this.budgetMTEFCodeP3;
    }

    public void setBudgetMTEFCodeP3(final String budgetMTEFCodeP3) {
        this.budgetMTEFCodeP3 = budgetMTEFCodeP3;
    }

    public String getBudgetMTEFLineP3() {
        return this.budgetMTEFLineP3;
    }

    public void setBudgetMTEFLineP3(final String budgetMTEFLineP3) {
        this.budgetMTEFLineP3 = budgetMTEFLineP3;
    }

    public String getBudgetMTEFActivityP3() {
        return this.budgetMTEFActivityP3;
    }

    public void setBudgetMTEFActivityP3(final String budgetMTEFActivityP3) {
        this.budgetMTEFActivityP3 = budgetMTEFActivityP3;
    }

    public String getBudgetMTEFCodeP4() {
        return this.budgetMTEFCodeP4;
    }

    public void setBudgetMTEFCodeP4(final String budgetMTEFCodeP4) {
        this.budgetMTEFCodeP4 = budgetMTEFCodeP4;
    }

    public String getBudgetMTEFLineP4() {
        return this.budgetMTEFLineP4;
    }

    public void setBudgetMTEFLineP4(final String budgetMTEFLineP4) {
        this.budgetMTEFLineP4 = budgetMTEFLineP4;
    }

    public String getBudgetMTEFActivityP4() {
        return this.budgetMTEFActivityP4;
    }

    public void setBudgetMTEFActivityP4(final String budgetMTEFActivityP4) {
        this.budgetMTEFActivityP4 = budgetMTEFActivityP4;
    }

    public String getOtherComments() {
        return this.otherComments;
    }

    public void setOtherComments(final String otherComments) {
        this.otherComments = otherComments;
    }

	/**
	 * @return the persistedUserGroup
	 */
	public PersistedUserGroup getPersistedUserGroup() {
		return this.persistedUserGroup;
	}

	/**
	 * @param persistedUserGroup the persistedUserGroup to set
	 */
	public void setPersistedUserGroup(final PersistedUserGroup persistedUserGroup) {
		this.persistedUserGroup = persistedUserGroup;
	}

	/**
	 * @return the LevelOfCertainty
	 */
	public Category getLevelOfCertainty() {
		return this.LevelOfCertainty;
	}

	/**
	 * @param LevelOfCertainty the LevelOfCertainty to set
	 */
	public void setLevelOfCertainty(final Category LevelOfCertainty) {
		this.LevelOfCertainty = LevelOfCertainty;
	}

	/**
	 * @return the approved
	 */
	public Boolean getApproved() {
		return this.approved;
	}

	/**
	 * @param approved the approved to set
	 */
	public void setApproved(final Boolean approved) {
		this.approved = approved;
	}
}
