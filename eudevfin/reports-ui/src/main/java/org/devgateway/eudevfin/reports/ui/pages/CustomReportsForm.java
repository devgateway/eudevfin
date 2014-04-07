package org.devgateway.eudevfin.reports.ui.pages;

import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.ui.common.components.DropDownField;

import java.io.Serializable;

/**
 * @author idobre
 * @since 4/2/14
 */
public class CustomReportsForm implements Serializable {
    private Area recipient;
    private Category geography;
    private Organization nationalInstitution;
    private DropDownField<ChannelCategory> multilateralAgency;
    private Category typeOfFlowbiMulti;
    private Category typeOfAid;
    private Category sector;
    private Integer year;
    private Integer startingYear;
    private Integer completitionYear;
    private Boolean coFinancingTransactionsOnly;
    private Boolean CPAOnly;
    private Boolean humanitarianAid;

    public Area getRecipient() {
        return recipient;
    }

    public void setRecipient(Area recipient) {
        this.recipient = recipient;
    }

    public Category getGeography() {
        return geography;
    }

    public void setGeography(Category geography) {
        this.geography = geography;
    }

    public Organization getNationalInstitution() {
        return nationalInstitution;
    }

    public void setNationalInstitution(Organization nationalInstitution) {
        this.nationalInstitution = nationalInstitution;
    }

    public DropDownField<ChannelCategory> getMultilateralAgency() {
        return multilateralAgency;
    }

    public void setMultilateralAgency(DropDownField<ChannelCategory> multilateralAgency) {
        this.multilateralAgency = multilateralAgency;
    }

    public Category getTypeOfFlowbiMulti() {
        return typeOfFlowbiMulti;
    }

    public void setTypeOfFlowbiMulti(Category typeOfFlowbiMulti) {
        this.typeOfFlowbiMulti = typeOfFlowbiMulti;
    }

    public Category getTypeOfAid() {
        return typeOfAid;
    }

    public void setTypeOfAid(Category typeOfAid) {
        this.typeOfAid = typeOfAid;
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

    public Integer getStartingYear() {
        return startingYear;
    }

    public void setStartingYear(Integer startingYear) {
        this.startingYear = startingYear;
    }

    public Integer getCompletitionYear() {
        return completitionYear;
    }

    public void setCompletitionYear(Integer completitionYear) {
        this.completitionYear = completitionYear;
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

    public Boolean getHumanitarianAid() {
        return humanitarianAid;
    }

    public void setHumanitarianAid(Boolean humanitarianAid) {
        this.humanitarianAid = humanitarianAid;
    }
}