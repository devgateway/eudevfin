/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.components.util;

import java.io.Serializable;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.joda.time.LocalDateTime;

/**
 *
 * @author alcr
 */
public class TransactionSearchModel implements Serializable {

    private static final long serialVersionUID = 7823208128059387955L;

    private Area geographicFocus;
    private Organization financingInstitution;
    private LocalDateTime reportingYear;
    private CustomFinancialTransaction transaction;

    private String amountUSD;
    private String amountRON;
    private String amountEUR;

    public Area getGeographicFocus() {
        return geographicFocus;
    }

    public void setGeographicFocus(Area geographicFocus) {
        this.geographicFocus = geographicFocus;
    }

    public Organization getFinancingInstitution() {
        return financingInstitution;
    }

    public void setFinancingInstitution(Organization financingInstitution) {
        this.financingInstitution = financingInstitution;
    }

    public LocalDateTime getReportingYear() {
        return reportingYear;
    }

    public void setReportingYear(LocalDateTime reportingYear) {
        this.reportingYear = reportingYear;
    }

    public String getAmountUSD() {
        return amountUSD;
    }

    public void setAmountUSD(String amountUSD) {
        this.amountUSD = amountUSD;
    }

    public String getAmountRON() {
        return amountRON;
    }

    public void setAmountRON(String amountRON) {
        this.amountRON = amountRON;
    }

    public String getAmountEUR() {
        return amountEUR;
    }

    public void setAmountEUR(String amountEUR) {
        this.amountEUR = amountEUR;
    }

    public CustomFinancialTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(CustomFinancialTransaction transaction) {
        this.transaction = transaction;
    }
}