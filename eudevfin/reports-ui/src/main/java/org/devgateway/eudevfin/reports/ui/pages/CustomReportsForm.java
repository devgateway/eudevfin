package org.devgateway.eudevfin.reports.ui.pages;

import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;

import java.io.Serializable;

/**
 * @author idobre
 * @since 4/2/14
 */
public class CustomReportsForm implements Serializable {
    private Area recipient;
    private String geopraphy;
    private Category sector;
    private Integer year;
    private Boolean coFinancingTransactionsOnly;
    private Boolean CPAOnly;

    public Area getRecipient() {
        return recipient;
    }

    public void setRecipient(Area recipient) {
        this.recipient = recipient;
    }

    public String getGeopraphy() {
        return geopraphy;
    }

    public void setGeopraphy(String geopraphy) {
        this.geopraphy = geopraphy;
    }

    public Category getSector() {
        return sector;
    }

    public void setSector(Category sector) {
        this.sector = sector;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Boolean getCoFinancingTransactionsOnly() {
        return coFinancingTransactionsOnly;
    }

    public void setCoFinancingTransactionsOnly(Boolean coFinancingTransactionsOnly) {
        this.coFinancingTransactionsOnly = coFinancingTransactionsOnly;
    }

    public Boolean getCPAOnly() {
        return CPAOnly;
    }

    public void setCPAOnly(Boolean CPAOnly) {
        this.CPAOnly = CPAOnly;
    }
}
