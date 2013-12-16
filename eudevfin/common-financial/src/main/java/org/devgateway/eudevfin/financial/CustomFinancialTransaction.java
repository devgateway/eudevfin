/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.financial;

import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.joda.time.LocalDateTime;

public class CustomFinancialTransaction extends FinancialTransaction {
    private Boolean CPA;
    private Boolean projectCoFinanced;


    private BigMoney futureDebtPrincipal;
    private BigMoney futureDebtInterest;
    private BigMoney budgetMTEFDisbursement;
    private BigMoney budgetMTEFDisbursementP1;
    private BigMoney budgetMTEFDisbursementP2;
    private BigMoney budgetMTEFDisbursementP3;
    private BigMoney budgetMTEFDisbursementP4;


    private LocalDateTime dataAsPerDate;
    private LocalDateTime phasingOutYear;

    private Organization firstCoFinancingAgency;
    private Organization secondCoFinancingAgency;
    private Organization thirdCoFinancingAgency;

    private CurrencyUnit firstAgencyCurrency;
    private CurrencyUnit secondAgencyCurrency;
    private CurrencyUnit thirdAgencyCurrency;

    private BigMoney firstAgencyAmount;
    private BigMoney secondAgencyAmount;
    private BigMoney thirdAgencyAmount;

    private Category rmnch;
    private Category recipientCode;
    private Category recipientPriority;

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

    public LocalDateTime getDataAsPerDate() {
        return dataAsPerDate;
    }

    public void setDataAsPerDate(LocalDateTime dataAsPerDate) {
        this.dataAsPerDate = dataAsPerDate;
    }

    public Boolean getCPA() {
        return CPA;
    }

    public void setCPA(Boolean CPA) {
        this.CPA = CPA;
    }

    public BigMoney getFutureDebtPrincipal() {
        return futureDebtPrincipal;
    }

    public void setFutureDebtPrincipal(BigMoney futureDebtPrincipal) {
        this.futureDebtPrincipal = futureDebtPrincipal;
    }

    public BigMoney getFutureDebtInterest() {
        return futureDebtInterest;
    }

    public void setFutureDebtInterest(BigMoney futureDebtInterest) {
        this.futureDebtInterest = futureDebtInterest;
    }

    public Boolean getProjectCoFinanced() {
        return projectCoFinanced;
    }

    public void setProjectCoFinanced(Boolean projectCoFinanced) {
        this.projectCoFinanced = projectCoFinanced;
    }

    public Organization getFirstCoFinancingAgency() {
        return firstCoFinancingAgency;
    }

    public void setFirstCoFinancingAgency(Organization firstCoFinancingAgency) {
        this.firstCoFinancingAgency = firstCoFinancingAgency;
    }

    public Organization getSecondCoFinancingAgency() {
        return secondCoFinancingAgency;
    }

    public void setSecondCoFinancingAgency(Organization secondCoFinancingAgency) {
        this.secondCoFinancingAgency = secondCoFinancingAgency;
    }

    public Organization getThirdCoFinancingAgency() {
        return thirdCoFinancingAgency;
    }

    public void setThirdCoFinancingAgency(Organization thirdCoFinancingAgency) {
        this.thirdCoFinancingAgency = thirdCoFinancingAgency;
    }

    public CurrencyUnit getFirstAgencyCurrency() {
        return firstAgencyCurrency;
    }

    public void setFirstAgencyCurrency(CurrencyUnit firstAgencyCurrency) {
        this.firstAgencyCurrency = firstAgencyCurrency;
    }

    public CurrencyUnit getSecondAgencyCurrency() {
        return secondAgencyCurrency;
    }

    public void setSecondAgencyCurrency(CurrencyUnit secondAgencyCurrency) {
        this.secondAgencyCurrency = secondAgencyCurrency;
    }

    public CurrencyUnit getThirdAgencyCurrency() {
        return thirdAgencyCurrency;
    }

    public void setThirdAgencyCurrency(CurrencyUnit thirdAgencyCurrency) {
        this.thirdAgencyCurrency = thirdAgencyCurrency;
    }

    public BigMoney getFirstAgencyAmount() {
        return firstAgencyAmount;
    }

    public void setFirstAgencyAmount(BigMoney firstAgencyAmount) {
        this.firstAgencyAmount = firstAgencyAmount;
    }

    public BigMoney getSecondAgencyAmount() {
        return secondAgencyAmount;
    }

    public void setSecondAgencyAmount(BigMoney secondAgencyAmount) {
        this.secondAgencyAmount = secondAgencyAmount;
    }

    public BigMoney getThirdAgencyAmount() {
        return thirdAgencyAmount;
    }

    public void setThirdAgencyAmount(BigMoney thirdAgencyAmount) {
        this.thirdAgencyAmount = thirdAgencyAmount;
    }

    public Category getRmnch() {
        return rmnch;
    }

    public void setRmnch(Category rmnch) {
        this.rmnch = rmnch;
    }

    public String getBudgetCode() {
        return budgetCode;
    }

    public void setBudgetCode(String budgetCode) {
        this.budgetCode = budgetCode;
    }

    public String getBudgetLine() {
        return budgetLine;
    }

    public void setBudgetLine(String budgetLine) {
        this.budgetLine = budgetLine;
    }

    public String getBudgetActivity() {
        return budgetActivity;
    }

    public void setBudgetActivity(String budgetActivity) {
        this.budgetActivity = budgetActivity;
    }

    public Category getRecipientCode() {
        return recipientCode;
    }

    public void setRecipientCode(Category recipientCode) {
        this.recipientCode = recipientCode;
    }

    public Category getRecipientPriority() {
        return recipientPriority;
    }

    public void setRecipientPriority(Category recipientPriority) {
        this.recipientPriority = recipientPriority;
    }

    public LocalDateTime getPhasingOutYear() {
        return phasingOutYear;
    }

    public void setPhasingOutYear(LocalDateTime phasingOutYear) {
        this.phasingOutYear = phasingOutYear;
    }

    public BigMoney getBudgetMTEFDisbursement() {
        return budgetMTEFDisbursement;
    }

    public void setBudgetMTEFDisbursement(BigMoney budgetMTEFDisbursement) {
        this.budgetMTEFDisbursement = budgetMTEFDisbursement;
    }

    public BigMoney getBudgetMTEFDisbursementP1() {
        return budgetMTEFDisbursementP1;
    }

    public void setBudgetMTEFDisbursementP1(BigMoney budgetMTEFDisbursementP1) {
        this.budgetMTEFDisbursementP1 = budgetMTEFDisbursementP1;
    }

    public BigMoney getBudgetMTEFDisbursementP2() {
        return budgetMTEFDisbursementP2;
    }

    public void setBudgetMTEFDisbursementP2(BigMoney budgetMTEFDisbursementP2) {
        this.budgetMTEFDisbursementP2 = budgetMTEFDisbursementP2;
    }

    public BigMoney getBudgetMTEFDisbursementP3() {
        return budgetMTEFDisbursementP3;
    }

    public void setBudgetMTEFDisbursementP3(BigMoney budgetMTEFDisbursementP3) {
        this.budgetMTEFDisbursementP3 = budgetMTEFDisbursementP3;
    }

    public BigMoney getBudgetMTEFDisbursementP4() {
        return budgetMTEFDisbursementP4;
    }

    public void setBudgetMTEFDisbursementP4(BigMoney budgetMTEFDisbursementP4) {
        this.budgetMTEFDisbursementP4 = budgetMTEFDisbursementP4;
    }

    public String getBudgetMTEFCode() {
        return budgetMTEFCode;
    }

    public void setBudgetMTEFCode(String budgetMTEFCode) {
        this.budgetMTEFCode = budgetMTEFCode;
    }

    public String getBudgetMTEFLine() {
        return budgetMTEFLine;
    }

    public void setBudgetMTEFLine(String budgetMTEFLine) {
        this.budgetMTEFLine = budgetMTEFLine;
    }

    public String getBudgetMTEFActivity() {
        return budgetMTEFActivity;
    }

    public void setBudgetMTEFActivity(String budgetMTEFActivity) {
        this.budgetMTEFActivity = budgetMTEFActivity;
    }

    public String getBudgetMTEFCodeP1() {
        return budgetMTEFCodeP1;
    }

    public void setBudgetMTEFCodeP1(String budgetMTEFCodeP1) {
        this.budgetMTEFCodeP1 = budgetMTEFCodeP1;
    }

    public String getBudgetMTEFLineP1() {
        return budgetMTEFLineP1;
    }

    public void setBudgetMTEFLineP1(String budgetMTEFLineP1) {
        this.budgetMTEFLineP1 = budgetMTEFLineP1;
    }

    public String getBudgetMTEFActivityP1() {
        return budgetMTEFActivityP1;
    }

    public void setBudgetMTEFActivityP1(String budgetMTEFActivityP1) {
        this.budgetMTEFActivityP1 = budgetMTEFActivityP1;
    }

    public String getBudgetMTEFCodeP2() {
        return budgetMTEFCodeP2;
    }

    public void setBudgetMTEFCodeP2(String budgetMTEFCodeP2) {
        this.budgetMTEFCodeP2 = budgetMTEFCodeP2;
    }

    public String getBudgetMTEFLineP2() {
        return budgetMTEFLineP2;
    }

    public void setBudgetMTEFLineP2(String budgetMTEFLineP2) {
        this.budgetMTEFLineP2 = budgetMTEFLineP2;
    }

    public String getBudgetMTEFActivityP2() {
        return budgetMTEFActivityP2;
    }

    public void setBudgetMTEFActivityP2(String budgetMTEFActivityP2) {
        this.budgetMTEFActivityP2 = budgetMTEFActivityP2;
    }

    public String getBudgetMTEFCodeP3() {
        return budgetMTEFCodeP3;
    }

    public void setBudgetMTEFCodeP3(String budgetMTEFCodeP3) {
        this.budgetMTEFCodeP3 = budgetMTEFCodeP3;
    }

    public String getBudgetMTEFLineP3() {
        return budgetMTEFLineP3;
    }

    public void setBudgetMTEFLineP3(String budgetMTEFLineP3) {
        this.budgetMTEFLineP3 = budgetMTEFLineP3;
    }

    public String getBudgetMTEFActivityP3() {
        return budgetMTEFActivityP3;
    }

    public void setBudgetMTEFActivityP3(String budgetMTEFActivityP3) {
        this.budgetMTEFActivityP3 = budgetMTEFActivityP3;
    }

    public String getBudgetMTEFCodeP4() {
        return budgetMTEFCodeP4;
    }

    public void setBudgetMTEFCodeP4(String budgetMTEFCodeP4) {
        this.budgetMTEFCodeP4 = budgetMTEFCodeP4;
    }

    public String getBudgetMTEFLineP4() {
        return budgetMTEFLineP4;
    }

    public void setBudgetMTEFLineP4(String budgetMTEFLineP4) {
        this.budgetMTEFLineP4 = budgetMTEFLineP4;
    }

    public String getBudgetMTEFActivityP4() {
        return budgetMTEFActivityP4;
    }

    public void setBudgetMTEFActivityP4(String budgetMTEFActivityP4) {
        this.budgetMTEFActivityP4 = budgetMTEFActivityP4;
    }
}
