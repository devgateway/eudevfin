/*******************************************************************************
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    aartimon
 ******************************************************************************/

package org.devgateway.eudevfin.dim.pages.transaction;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.ControlGroup;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.devgateway.eudevfin.dim.core.Constants;
import org.devgateway.eudevfin.dim.core.components.tabs.BootstrapCssTabbedPanel;
import org.devgateway.eudevfin.dim.core.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@MountPath(value = "/transaction")
@AuthorizeInstantiation(Constants.ROLE_USER)
public class TransactionPage extends HeaderFooter {
    private static final Logger logger = Logger.getLogger(TransactionPage.class);

    @SuppressWarnings("unchecked")
    public TransactionPage() {

        final CompoundPropertyModel<FakeTransaction> model = new CompoundPropertyModel<>(new FakeTransaction());
        setModel(model);

        Form form = new Form("form");
        add(form);

        List<ITab> tabList = new ArrayList<>();
        tabList.add(IdentificationDataTab.newTab(this));
        tabList.add(BasicDataTab.newTab(this));
        tabList.add(SupplementaryDataTab.newTab(this));
        tabList.add(VolumeDataTab.newTab(this));
        tabList.add(ForLoansOnlyTab.newTab(this));
        tabList.add(AvailableComponentsTab.newTab(this));

        BootstrapCssTabbedPanel<ITab> bc = new BootstrapCssTabbedPanel<>("bc", tabList).
                positionTabs(BootstrapCssTabbedPanel.Orientation.RIGHT);
        form.add(bc);

        form.add(new IndicatingAjaxButton("submit", Model.of("Submit")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                logger.info("Submitted ok!");
                logger.info("Object:" + model.getObject());
            }

            @Override
            protected void onError(final AjaxRequestTarget target, Form<?> form) {
                logger.info("Error detected!");

                final Model<Boolean> shownFirstSection = Model.of(Boolean.FALSE);

                // visit form children and add to the ajax request the invalid
                // ones
                form.visitChildren(FormComponent.class,
                        new IVisitor<FormComponent, Void>() {
                            @Override
                            public void component(FormComponent component,
                                                  IVisit<Void> visit) {
                                if (!component.isValid()) {
                                    Component parent = component.findParent(ControlGroup.class);
                                    target.add(parent);
                                    if (!shownFirstSection.getObject()) {
                                        target.focusComponent(component);
                                        target.appendJavaScript("$('#" + component.getMarkupId() + "').parents('[class~=\"tab-pane\"]').siblings().attr(\"class\", \"tab-pane\");");
                                        target.appendJavaScript("$('#" + component.getMarkupId() + "').parents('[class~=\"tab-pane\"]').attr(\"class\", \"tab-pane active\");");

                                        target.appendJavaScript("$('#" + component.getMarkupId() + "').parents('[class~=\"tabbable\"]').children('ul').find('li').attr('class', '');");
                                        target.appendJavaScript("var idOfSection = $('#" + component.getMarkupId() + "').parents('[class~=\"tab-pane\"]').attr('id');$('#" + component.getMarkupId() + "').parents('[class~=\"tabbable\"]').children('ul').find('a[href=\"#' + idOfSection + '\"]').parent().attr('class', 'active');");

                                        shownFirstSection.setObject(Boolean.TRUE);
                                    }
                                }
                            }
                        });
            }
        });

    }

    @SuppressWarnings("UnusedDeclaration")
    private class FakeTransaction implements Serializable {
        //
        Integer reportingYear;
        Date commitmentDate;
        String reportingCountry;
        String extendingAgency;
        Integer crsId;
        Integer donorProjectNumber;
        Integer natureSubmission;
        //basic data tab
        String recipient;
        String CPA;
        String channelDelivery;
        String channelCode;
        String bilateralMultilateral;
        String typeOfFlow;
        String typeOfFinance;
        String typeOfAid;
        String activityProjectTitle;
        String sectorPurposeCode;
        //supplementary data tab
        String geographicalTargetArea;
        Date startingDate;
        Date completionDate;
        String description;
        String genderEquality;
        String aidToEnvironment;
        String pdGg;
        String tradeDevelopment;
        String freestandingTechnicalCooperation;
        String programbasedApproach;
        String investmentProject;
        String associatedFinancing;
        String biodiversity;
        String ccMitigation;
        String ccAdaptation;
        String desertification;
        String tbd96;
        String tbd97;
        String tbd98;
        String tbd99;
        //Volume Data
        String currency;
        String commitments;
        String amountsExtended;
        String amountsReceived;
        String amountUntied;
        String amountPartiallyUntied;
        String amountTied;
        String amountOfIRTC;
        String amountOfExpertsCommitments;
        String amountOfExpertsExtended;
        String amountOfExportCredit;

        //For Loans Only
        String typeOfRepayment;
        String numberOfRepayments;
        Integer interestRate;
        Integer secondInterestRate;
        Date firstRepaymentDate;
        Date finalRepaymentDate;
        Integer interestReceived;
        Integer principalDisbursed;
        Integer arrearsOfPrincipals;
        Integer arrearsOfInterest;
        Integer futureDebtPrincipal;
        Integer futureDebtInterest;

        private Date getCommitmentDate() {
            return commitmentDate;
        }

        private void setCommitmentDate(Date commitmentDate) {
            this.commitmentDate = commitmentDate;
        }

        private String getTypeOfRepayment() {
            return typeOfRepayment;
        }

        private void setTypeOfRepayment(String typeOfRepayment) {
            this.typeOfRepayment = typeOfRepayment;
        }

        private String getNumberOfRepayments() {
            return numberOfRepayments;
        }

        private void setNumberOfRepayments(String numberOfRepayments) {
            this.numberOfRepayments = numberOfRepayments;
        }

        private Integer getInterestRate() {
            return interestRate;
        }

        private void setInterestRate(Integer interestRate) {
            this.interestRate = interestRate;
        }

        private Integer getSecondInterestRate() {
            return secondInterestRate;
        }

        private void setSecondInterestRate(Integer secondInterestRate) {
            this.secondInterestRate = secondInterestRate;
        }

        private Date getFirstRepaymentDate() {
            return firstRepaymentDate;
        }

        private void setFirstRepaymentDate(Date firstRepaymentDate) {
            this.firstRepaymentDate = firstRepaymentDate;
        }

        private Date getFinalRepaymentDate() {
            return finalRepaymentDate;
        }

        private void setFinalRepaymentDate(Date finalRepaymentDate) {
            this.finalRepaymentDate = finalRepaymentDate;
        }

        private Integer getInterestReceived() {
            return interestReceived;
        }

        private void setInterestReceived(Integer interestReceived) {
            this.interestReceived = interestReceived;
        }

        private Integer getPrincipalDisbursed() {
            return principalDisbursed;
        }

        private void setPrincipalDisbursed(Integer principalDisbursed) {
            this.principalDisbursed = principalDisbursed;
        }

        private Integer getArrearsOfPrincipals() {
            return arrearsOfPrincipals;
        }

        private void setArrearsOfPrincipals(Integer arrearsOfPrincipals) {
            this.arrearsOfPrincipals = arrearsOfPrincipals;
        }

        private Integer getArrearsOfInterest() {
            return arrearsOfInterest;
        }

        private void setArrearsOfInterest(Integer arrearsOfInterest) {
            this.arrearsOfInterest = arrearsOfInterest;
        }

        private Integer getFutureDebtPrincipal() {
            return futureDebtPrincipal;
        }

        private void setFutureDebtPrincipal(Integer futureDebtPrincipal) {
            this.futureDebtPrincipal = futureDebtPrincipal;
        }

        private Integer getFutureDebtInterest() {
            return futureDebtInterest;
        }

        private void setFutureDebtInterest(Integer futureDebtInterest) {
            this.futureDebtInterest = futureDebtInterest;
        }

        private String getGeographicalTargetArea() {
            return geographicalTargetArea;
        }

        private void setGeographicalTargetArea(String geographicalTargetArea) {
            this.geographicalTargetArea = geographicalTargetArea;
        }

        private Date getStartingDate() {
            return startingDate;
        }

        private void setStartingDate(Date startingDate) {
            this.startingDate = startingDate;
        }

        private Date getCompletionDate() {
            return completionDate;
        }

        private void setCompletionDate(Date completionDate) {
            this.completionDate = completionDate;
        }

        private String getDescription() {
            return description;
        }

        private void setDescription(String description) {
            this.description = description;
        }

        private String getGenderEquality() {
            return genderEquality;
        }

        private void setGenderEquality(String genderEquality) {
            this.genderEquality = genderEquality;
        }

        private String getAidToEnvironment() {
            return aidToEnvironment;
        }

        private void setAidToEnvironment(String aidToEnvironment) {
            this.aidToEnvironment = aidToEnvironment;
        }

        private String getPdGg() {
            return pdGg;
        }

        private void setPdGg(String pdGg) {
            this.pdGg = pdGg;
        }

        private String getTradeDevelopment() {
            return tradeDevelopment;
        }

        private void setTradeDevelopment(String tradeDevelopment) {
            this.tradeDevelopment = tradeDevelopment;
        }

        private String getFreestandingTechnicalCooperation() {
            return freestandingTechnicalCooperation;
        }

        private void setFreestandingTechnicalCooperation(String freestandingTechnicalCooperation) {
            this.freestandingTechnicalCooperation = freestandingTechnicalCooperation;
        }

        private String getProgrambasedApproach() {
            return programbasedApproach;
        }

        private void setProgrambasedApproach(String programbasedApproach) {
            this.programbasedApproach = programbasedApproach;
        }

        private String getInvestmentProject() {
            return investmentProject;
        }

        private void setInvestmentProject(String investmentProject) {
            this.investmentProject = investmentProject;
        }

        private String getAssociatedFinancing() {
            return associatedFinancing;
        }

        private void setAssociatedFinancing(String associatedFinancing) {
            this.associatedFinancing = associatedFinancing;
        }

        private String getBiodiversity() {
            return biodiversity;
        }

        private void setBiodiversity(String biodiversity) {
            this.biodiversity = biodiversity;
        }

        private String getCcMitigation() {
            return ccMitigation;
        }

        private void setCcMitigation(String ccMitigation) {
            this.ccMitigation = ccMitigation;
        }

        private String getCcAdaptation() {
            return ccAdaptation;
        }

        private void setCcAdaptation(String ccAdaptation) {
            this.ccAdaptation = ccAdaptation;
        }

        private String getDesertification() {
            return desertification;
        }

        private void setDesertification(String desertification) {
            this.desertification = desertification;
        }

        private String getTbd96() {
            return tbd96;
        }

        private void setTbd96(String tbd96) {
            this.tbd96 = tbd96;
        }

        private String getTbd97() {
            return tbd97;
        }

        private void setTbd97(String tbd97) {
            this.tbd97 = tbd97;
        }

        private String getTbd98() {
            return tbd98;
        }

        private void setTbd98(String tbd98) {
            this.tbd98 = tbd98;
        }

        private String getTbd99() {
            return tbd99;
        }

        private void setTbd99(String tbd99) {
            this.tbd99 = tbd99;
        }

        private String getRecipient() {
            return recipient;
        }

        private void setRecipient(String recipient) {
            this.recipient = recipient;
        }

        private String getCPA() {
            return CPA;
        }

        private void setCPA(String CPA) {
            this.CPA = CPA;
        }

        private String getChannelDelivery() {
            return channelDelivery;
        }

        private void setChannelDelivery(String channelDelivery) {
            this.channelDelivery = channelDelivery;
        }

        private String getChannelCode() {
            return channelCode;
        }

        private void setChannelCode(String channelCode) {
            this.channelCode = channelCode;
        }

        private String getBilateralMultilateral() {
            return bilateralMultilateral;
        }

        private void setBilateralMultilateral(String bilateralMultilateral) {
            this.bilateralMultilateral = bilateralMultilateral;
        }

        private String getTypeOfFlow() {
            return typeOfFlow;
        }

        private void setTypeOfFlow(String typeOfFlow) {
            this.typeOfFlow = typeOfFlow;
        }

        private String getTypeOfFinance() {
            return typeOfFinance;
        }

        private void setTypeOfFinance(String typeOfFinance) {
            this.typeOfFinance = typeOfFinance;
        }

        private String getTypeOfAid() {
            return typeOfAid;
        }

        private void setTypeOfAid(String typeOfAid) {
            this.typeOfAid = typeOfAid;
        }

        private String getActivityProjectTitle() {
            return activityProjectTitle;
        }

        private void setActivityProjectTitle(String activityProjectTitle) {
            this.activityProjectTitle = activityProjectTitle;
        }

        private String getSectorPurposeCode() {
            return sectorPurposeCode;
        }

        private void setSectorPurposeCode(String sectorPurposeCode) {
            this.sectorPurposeCode = sectorPurposeCode;
        }

        private Integer getDonorProjectNumber() {
            return donorProjectNumber;
        }

        private void setDonorProjectNumber(Integer donorProjectNumber) {
            this.donorProjectNumber = donorProjectNumber;
        }

        private Integer getNatureSubmission() {
            return natureSubmission;
        }

        private void setNatureSubmission(Integer natureSubmission) {
            this.natureSubmission = natureSubmission;
        }

        private Integer getReportingYear() {
            return reportingYear;
        }

        private void setReportingYear(Integer reportingYear) {
            this.reportingYear = reportingYear;
        }

        private String getReportingCountry() {
            return reportingCountry;
        }

        private void setReportingCountry(String reportingCountry) {
            this.reportingCountry = reportingCountry;
        }

        private String getExtendingAgency() {
            return extendingAgency;
        }

        private void setExtendingAgency(String extendingAgency) {
            this.extendingAgency = extendingAgency;
        }

        private Integer getCrsId() {
            return crsId;
        }

        private void setCrsId(Integer crsId) {
            this.crsId = crsId;
        }

        private String getCurrency() {
            return currency;
        }

        private void setCurrency(String currency) {
            this.currency = currency;
        }

        private String getCommitments() {
            return commitments;
        }

        private void setCommitments(String commitments) {
            this.commitments = commitments;
        }

        private String getAmountsExtended() {
            return amountsExtended;
        }

        private void setAmountsExtended(String amountsExtended) {
            this.amountsExtended = amountsExtended;
        }

        private String getAmountsReceived() {
            return amountsReceived;
        }

        private void setAmountsReceived(String amountsReceived) {
            this.amountsReceived = amountsReceived;
        }

        private String getAmountUntied() {
            return amountUntied;
        }

        private void setAmountUntied(String amountUntied) {
            this.amountUntied = amountUntied;
        }

        private String getAmountPartiallyUntied() {
            return amountPartiallyUntied;
        }

        private void setAmountPartiallyUntied(String amountPartiallyUntied) {
            this.amountPartiallyUntied = amountPartiallyUntied;
        }

        private String getAmountTied() {
            return amountTied;
        }

        private void setAmountTied(String amountTied) {
            this.amountTied = amountTied;
        }

        private String getAmountOfIRTC() {
            return amountOfIRTC;
        }

        private void setAmountOfIRTC(String amountOfIRTC) {
            this.amountOfIRTC = amountOfIRTC;
        }

        private String getAmountOfExpertsCommitments() {
            return amountOfExpertsCommitments;
        }

        private void setAmountOfExpertsCommitments(String amountOfExpertsCommitments) {
            this.amountOfExpertsCommitments = amountOfExpertsCommitments;
        }

        private String getAmountOfExpertsExtended() {
            return amountOfExpertsExtended;
        }

        private void setAmountOfExpertsExtended(String amountOfExpertsExtended) {
            this.amountOfExpertsExtended = amountOfExpertsExtended;
        }

        private String getAmountOfExportCredit() {
            return amountOfExportCredit;
        }

        private void setAmountOfExportCredit(String amountOfExportCredit) {
            this.amountOfExportCredit = amountOfExportCredit;
        }

        @Override
        public String toString() {
            return "FakeTransaction{" +
                    "reportingYear=" + reportingYear +
                    ", commitmentDate=" + commitmentDate +
                    ", reportingCountry='" + reportingCountry + '\'' +
                    ", extendingAgency='" + extendingAgency + '\'' +
                    ", crsId=" + crsId +
                    ", donorProjectNumber=" + donorProjectNumber +
                    ", natureSubmission=" + natureSubmission +
                    ", recipient='" + recipient + '\'' +
                    ", CPA='" + CPA + '\'' +
                    ", channelDelivery='" + channelDelivery + '\'' +
                    ", channelCode='" + channelCode + '\'' +
                    ", bilateralMultilateral='" + bilateralMultilateral + '\'' +
                    ", typeOfFlow='" + typeOfFlow + '\'' +
                    ", typeOfFinance='" + typeOfFinance + '\'' +
                    ", typeOfAid='" + typeOfAid + '\'' +
                    ", activityProjectTitle='" + activityProjectTitle + '\'' +
                    ", sectorPurposeCode='" + sectorPurposeCode + '\'' +
                    ", geographicalTargetArea='" + geographicalTargetArea + '\'' +
                    ", startingDate=" + startingDate +
                    ", completionDate=" + completionDate +
                    ", description='" + description + '\'' +
                    ", genderEquality='" + genderEquality + '\'' +
                    ", aidToEnvironment='" + aidToEnvironment + '\'' +
                    ", pdGg='" + pdGg + '\'' +
                    ", tradeDevelopment='" + tradeDevelopment + '\'' +
                    ", freestandingTechnicalCooperation='" + freestandingTechnicalCooperation + '\'' +
                    ", programbasedApproach='" + programbasedApproach + '\'' +
                    ", investmentProject='" + investmentProject + '\'' +
                    ", associatedFinancing='" + associatedFinancing + '\'' +
                    ", biodiversity='" + biodiversity + '\'' +
                    ", ccMitigation='" + ccMitigation + '\'' +
                    ", ccAdaptation='" + ccAdaptation + '\'' +
                    ", desertification='" + desertification + '\'' +
                    ", tbd96='" + tbd96 + '\'' +
                    ", tbd97='" + tbd97 + '\'' +
                    ", tbd98='" + tbd98 + '\'' +
                    ", tbd99='" + tbd99 + '\'' +
                    ", currency='" + currency + '\'' +
                    ", commitments='" + commitments + '\'' +
                    ", amountsExtended='" + amountsExtended + '\'' +
                    ", amountsReceived='" + amountsReceived + '\'' +
                    ", amountUntied='" + amountUntied + '\'' +
                    ", amountPartiallyUntied='" + amountPartiallyUntied + '\'' +
                    ", amountTied='" + amountTied + '\'' +
                    ", amountOfIRTC='" + amountOfIRTC + '\'' +
                    ", amountOfExpertsCommitments='" + amountOfExpertsCommitments + '\'' +
                    ", amountOfExpertsExtended='" + amountOfExpertsExtended + '\'' +
                    ", amountOfExportCredit='" + amountOfExportCredit + '\'' +
                    ", typeOfRepayment='" + typeOfRepayment + '\'' +
                    ", numberOfRepayments='" + numberOfRepayments + '\'' +
                    ", interestRate=" + interestRate +
                    ", secondInterestRate=" + secondInterestRate +
                    ", firstRepaymentDate=" + firstRepaymentDate +
                    ", finalRepaymentDate=" + finalRepaymentDate +
                    ", interestReceived=" + interestReceived +
                    ", principalDisbursed=" + principalDisbursed +
                    ", arrearsOfPrincipals=" + arrearsOfPrincipals +
                    ", arrearsOfInterest=" + arrearsOfInterest +
                    ", futureDebtPrincipal=" + futureDebtPrincipal +
                    ", futureDebtInterest=" + futureDebtInterest +
                    '}';
        }
    }
}
