/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages;

import org.devgateway.eudevfin.dim.core.DimComponentInfo;
import org.devgateway.eudevfin.dim.pages.transaction.crs.*;
import org.devgateway.eudevfin.dim.pages.transaction.custom.*;
import org.devgateway.eudevfin.ui.common.components.*;

import java.util.HashMap;

/**
 * @author Alexandru Artimon
 * @since 14/04/14
 */
public abstract class AbstractFormStructureTest extends BaseDimTest {

    private static final String IDENTIFICATION_DATA_TAB_PATH = "form:bc:tabContent:0:tabContentPanel:";
    private static final String BASIC_DATA_TAB_PATH = "form:bc:tabContent:1:tabContentPanel:";
    private static final String SUPPLEMENTARY_DATA_TAB_PATH = "form:bc:tabContent:2:tabContentPanel:";
    private static final String VOLUME_DATA_TAB_PATH = "form:bc:tabContent:3:tabContentPanel:";
    private static final String FOR_LOANS_ONLY_TAB_PATH = "form:bc:tabContent:4:tabContentPanel:";

    private static final String POLICY_OBJECTIVES_GROUP = "policyObjectivesGroup";
    private static final String TYPE_OF_AID_GROUP = "typeOfAidGroup";
    private static final String EXTENSION_PANEL1 = "extensionPanel1";
    private static final String EXTENSION_PANEL2 = "extensionPanel2";
    private static final String EXTENSION_PANEL3 = "extensionPanel3";
    private static final String BUDGET_MTEF_TABLE = EXTENSION_PANEL2 + ":budgetMTEFTable";
    private static final String DISBURSEMENT_GROUP = BUDGET_MTEF_TABLE + ":disbursementGroup";
    private static final String CODE_GROUP = BUDGET_MTEF_TABLE + ":codeGroup";
    private static final String LINE_GROUP = BUDGET_MTEF_TABLE + ":lineGroup";
    private static final String ACTIVITY_GROUP = BUDGET_MTEF_TABLE + ":activityGroup";
    private static final String CO_FINANCING = EXTENSION_PANEL2 + ":14coFinancing";
    private static final String GROUP_1ST_AGENCY = CO_FINANCING + ":14group1stAgency:";

    private HashMap<String, DimComponentInfo> components;


    public HashMap<String, DimComponentInfo> getComponents() {
        return components;
    }

    protected void initComponents() {
        components = new HashMap<>();
        initCustomIdentificationDataTab();
        initCustomBasicDataTab();
        initCustomSupplementaryDataTab();
        initCustomVolumeDataTab();
        initCustomForLoansOnlyTab();
    }

    private void initIdentificationDataTab() {
        components.put("1reportingYear", new DimComponentInfo(IdentificationDataTab.KEY, IDENTIFICATION_DATA_TAB_PATH + "1reportingYear", TextInputField.class));
        components.put("1bCommitmentDate", new DimComponentInfo(IdentificationDataTab.KEY, IDENTIFICATION_DATA_TAB_PATH + "1bCommitmentDate", DateInputField.class));
        components.put("3extendingAgency", new DimComponentInfo(IdentificationDataTab.KEY, IDENTIFICATION_DATA_TAB_PATH + "3extendingAgency", DropDownField.class));
        components.put("4crsId", new DimComponentInfo(IdentificationDataTab.KEY, IDENTIFICATION_DATA_TAB_PATH + "4crsId", TextInputField.class));
        components.put("5donorProjectNumber", new DimComponentInfo(IdentificationDataTab.KEY, IDENTIFICATION_DATA_TAB_PATH + "5donorProjectNumber", TextInputField.class));
        components.put("6natureSubmission", new DimComponentInfo(IdentificationDataTab.KEY, IDENTIFICATION_DATA_TAB_PATH + "6natureSubmission", DropDownField.class));
    }

    private void initCustomIdentificationDataTab() {
        initIdentificationDataTab();
        components.put("1aDataAsPerDate", new DimComponentInfo(CustomIdentificationDataTab.KEY, IDENTIFICATION_DATA_TAB_PATH + "1aDataAsPerDate", DateInputField.class));
        components.put("6aLevelOfCertainty", new DimComponentInfo(CustomIdentificationDataTab.KEY, IDENTIFICATION_DATA_TAB_PATH + "6aLevelOfCertainty", DropDownField.class));


    }

    private void testBasicDataTab() {
        components.put("7recipient", new DimComponentInfo(BasicDataTab.KEY, BASIC_DATA_TAB_PATH + "7recipient", DropDownField.class));
        components.put("8channelDelivery", new DimComponentInfo(BasicDataTab.KEY, BASIC_DATA_TAB_PATH + "8channelDelivery", DropDownField.class));
        components.put("10bilateralMultilateral", new DimComponentInfo(BasicDataTab.KEY, BASIC_DATA_TAB_PATH + "10bilateralMultilateral", DropDownField.class));
        components.put("11typeOfFlow", new DimComponentInfo(BasicDataTab.KEY, BASIC_DATA_TAB_PATH + "11typeOfFlow", DropDownField.class));
        components.put("12typeOfFinance", new DimComponentInfo(BasicDataTab.KEY, BASIC_DATA_TAB_PATH + "12typeOfFinance", DropDownField.class));
        components.put("13typeOfAid", new DimComponentInfo(BasicDataTab.KEY, BASIC_DATA_TAB_PATH + "13typeOfAid", DropDownField.class));
        components.put("14activityProjectTitle", new DimComponentInfo(BasicDataTab.KEY, BASIC_DATA_TAB_PATH + "14activityProjectTitle", TextAreaInputField.class));
        components.put("15sectorPurposeCode", new DimComponentInfo(BasicDataTab.KEY, BASIC_DATA_TAB_PATH + "15sectorPurposeCode", DropDownField.class));
    }

    private void initCustomBasicDataTab() {
        testBasicDataTab();
        components.put("7aRecipientCode", new DimComponentInfo(CustomBasicDataTab.KEY, BASIC_DATA_TAB_PATH + EXTENSION_PANEL3 + ":7aRecipientCode", DropDownField.class));
        components.put("7cPriorityStatus", new DimComponentInfo(CustomBasicDataTab.KEY, BASIC_DATA_TAB_PATH + EXTENSION_PANEL3 + ":7cPriorityStatus", DropDownField.class));
        components.put("7bCPA", new DimComponentInfo(CustomBasicDataTab.KEY, BASIC_DATA_TAB_PATH + EXTENSION_PANEL1 + ":7bCPA", DropDownField.class));
        components.put("7dPhasingOutYear", new DimComponentInfo(CustomBasicDataTab.KEY, BASIC_DATA_TAB_PATH + EXTENSION_PANEL1 + ":7dPhasingOutYear", TextInputField.class));
        components.put("14aProjectCoFinanced", new DimComponentInfo(CustomBasicDataTab.KEY, BASIC_DATA_TAB_PATH + EXTENSION_PANEL2 + ":14aProjectCoFinanced", DropDownField.class));
        components.put("14bisUploadDocumentation", new DimComponentInfo(CustomBasicDataTab.KEY, BASIC_DATA_TAB_PATH + EXTENSION_PANEL2 + ":14bisUploadDocumentation", MultiFileUploadField.class));
        components.put("14coFinancing", new DimComponentInfo(CustomBasicDataTab.KEY, BASIC_DATA_TAB_PATH + CO_FINANCING, PermissionAwareContainer.class));
        components.put("14group1stAgency", new DimComponentInfo(CustomBasicDataTab.KEY, BASIC_DATA_TAB_PATH + GROUP_1ST_AGENCY, VisibilityAwareContainer.class));
        components.put("14b1stAgency", new DimComponentInfo(CustomBasicDataTab.KEY, BASIC_DATA_TAB_PATH + GROUP_1ST_AGENCY + "14b1stAgency", DropDownField.class));
        components.put("14c1stAgencyAmount", new DimComponentInfo(CustomBasicDataTab.KEY, BASIC_DATA_TAB_PATH + GROUP_1ST_AGENCY + "14c1stAgencyAmount", TextInputField.class));
        components.put("14d1stAgencyCurrency", new DimComponentInfo(CustomBasicDataTab.KEY, BASIC_DATA_TAB_PATH + GROUP_1ST_AGENCY + "14d1stAgencyCurrency", DropDownField.class));
        components.put("14e2ndAgency", new DimComponentInfo(CustomBasicDataTab.KEY, BASIC_DATA_TAB_PATH + CO_FINANCING + ":14e2ndAgency", DropDownField.class));
        components.put("14f2ndAgencyAmount", new DimComponentInfo(CustomBasicDataTab.KEY, BASIC_DATA_TAB_PATH + CO_FINANCING + ":14f2ndAgencyAmount", TextInputField.class));
        components.put("14g2ndAgencyCurrency", new DimComponentInfo(CustomBasicDataTab.KEY, BASIC_DATA_TAB_PATH + CO_FINANCING + ":14g2ndAgencyCurrency", DropDownField.class));
        components.put("14h3rdAgency", new DimComponentInfo(CustomBasicDataTab.KEY, BASIC_DATA_TAB_PATH + CO_FINANCING + ":14h3rdAgency", DropDownField.class));
        components.put("14i3rdAgencyAmount", new DimComponentInfo(CustomBasicDataTab.KEY, BASIC_DATA_TAB_PATH + CO_FINANCING + ":14i3rdAgencyAmount", TextInputField.class));
        components.put("14j3rdAgencyCurrency", new DimComponentInfo(CustomBasicDataTab.KEY, BASIC_DATA_TAB_PATH + CO_FINANCING + ":14j3rdAgencyCurrency", DropDownField.class));
    }

    private void initSupplementaryDataTab() {
        components.put("16geographicalTargetArea", new DimComponentInfo(SupplementaryDataTab.KEY, SUPPLEMENTARY_DATA_TAB_PATH + "16geographicalTargetArea", TextInputField.class));
        components.put("17startingDate", new DimComponentInfo(SupplementaryDataTab.KEY, SUPPLEMENTARY_DATA_TAB_PATH + "17startingDate", DateInputField.class));
        components.put("18completionDate", new DimComponentInfo(SupplementaryDataTab.KEY, SUPPLEMENTARY_DATA_TAB_PATH + "18completionDate", DateInputField.class));
        components.put("19description", new DimComponentInfo(SupplementaryDataTab.KEY, SUPPLEMENTARY_DATA_TAB_PATH + "19description", TextAreaInputField.class));
        components.put(POLICY_OBJECTIVES_GROUP, new DimComponentInfo(SupplementaryDataTab.KEY, SUPPLEMENTARY_DATA_TAB_PATH + POLICY_OBJECTIVES_GROUP, VisibilityAwareContainer.class));
        components.put("20genderEquality", new DimComponentInfo(SupplementaryDataTab.KEY, SUPPLEMENTARY_DATA_TAB_PATH + POLICY_OBJECTIVES_GROUP + ":20genderEquality", DropDownField.class));
        components.put("21aidToEnvironment", new DimComponentInfo(SupplementaryDataTab.KEY, SUPPLEMENTARY_DATA_TAB_PATH + POLICY_OBJECTIVES_GROUP + ":21aidToEnvironment", DropDownField.class));
        components.put("22pdGg", new DimComponentInfo(SupplementaryDataTab.KEY, SUPPLEMENTARY_DATA_TAB_PATH + POLICY_OBJECTIVES_GROUP + ":22pdGg", DropDownField.class));
        components.put("23tradeDevelopment", new DimComponentInfo(SupplementaryDataTab.KEY, SUPPLEMENTARY_DATA_TAB_PATH + POLICY_OBJECTIVES_GROUP + ":23tradeDevelopment", DropDownField.class));
        components.put("28biodiversity", new DimComponentInfo(SupplementaryDataTab.KEY, SUPPLEMENTARY_DATA_TAB_PATH + POLICY_OBJECTIVES_GROUP + ":28biodiversity", DropDownField.class));
        components.put("29ccMitigation", new DimComponentInfo(SupplementaryDataTab.KEY, SUPPLEMENTARY_DATA_TAB_PATH + POLICY_OBJECTIVES_GROUP + ":29ccMitigation", DropDownField.class));
        components.put("30ccAdaptation", new DimComponentInfo(SupplementaryDataTab.KEY, SUPPLEMENTARY_DATA_TAB_PATH + POLICY_OBJECTIVES_GROUP + ":30ccAdaptation", DropDownField.class));
        components.put("31desertification", new DimComponentInfo(SupplementaryDataTab.KEY, SUPPLEMENTARY_DATA_TAB_PATH + POLICY_OBJECTIVES_GROUP + ":31desertification", DropDownField.class));
        components.put(TYPE_OF_AID_GROUP, new DimComponentInfo(SupplementaryDataTab.KEY, SUPPLEMENTARY_DATA_TAB_PATH + TYPE_OF_AID_GROUP, VisibilityAwareContainer.class));
        components.put("24freestandingTechnicalCooperation", new DimComponentInfo(SupplementaryDataTab.KEY, SUPPLEMENTARY_DATA_TAB_PATH + TYPE_OF_AID_GROUP + ":24freestandingTechnicalCooperation", DropDownField.class));
        components.put("25programmeBasedApproach", new DimComponentInfo(SupplementaryDataTab.KEY, SUPPLEMENTARY_DATA_TAB_PATH + TYPE_OF_AID_GROUP + ":25programmeBasedApproach", DropDownField.class));
        components.put("26investmentProject", new DimComponentInfo(SupplementaryDataTab.KEY, SUPPLEMENTARY_DATA_TAB_PATH + TYPE_OF_AID_GROUP + ":26investmentProject", DropDownField.class));
        components.put("27associatedFinancing", new DimComponentInfo(SupplementaryDataTab.KEY, SUPPLEMENTARY_DATA_TAB_PATH + TYPE_OF_AID_GROUP + ":27associatedFinancing", DropDownField.class));
    }

    private void initCustomSupplementaryDataTab() {
        initSupplementaryDataTab();
        components.put("otherMarkersContainer", new DimComponentInfo(CustomSupplementaryDataTab.KEY, SUPPLEMENTARY_DATA_TAB_PATH + "otherMarkersContainer", VisibilityAwareContainer.class));
        components.put("96rmnch", new DimComponentInfo(CustomSupplementaryDataTab.KEY, SUPPLEMENTARY_DATA_TAB_PATH + "otherMarkersContainer:" + "96rmnch", DropDownField.class));
    }

    private void initVolumeDataTab() {
        components.put("32currency", new DimComponentInfo(VolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + "32currency", DropDownField.class));
        components.put("33commitments", new DimComponentInfo(VolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + "33commitments", TextInputField.class));
        components.put("34amountsExtended", new DimComponentInfo(VolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + "34amountsExtended", TextInputField.class));
        components.put("35amountsReceived", new DimComponentInfo(VolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + "35amountsReceived", TextInputField.class));
        components.put("36amountUntied", new DimComponentInfo(VolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + "36amountUntied", TextInputField.class));
        components.put("37amountPartiallyUntied", new DimComponentInfo(VolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + "37amountPartiallyUntied", TextInputField.class));
        components.put("38amountTied", new DimComponentInfo(VolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + "38amountTied", TextInputField.class));
        components.put("39amountOfIRTC", new DimComponentInfo(VolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + "39amountOfIRTC", TextInputField.class));
        components.put("40amountOfExpertsCommitments", new DimComponentInfo(VolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + "40amountOfExpertsCommitments", TextInputField.class));
        components.put("41amountOfExpertsExtended", new DimComponentInfo(VolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + "41amountOfExpertsExtended", TextInputField.class));
        components.put("42amountOfExportCredit", new DimComponentInfo(VolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + "42amountOfExportCredit", TextInputField.class));
    }

    private void initCustomVolumeDataTab() {
        initVolumeDataTab();
        components.put("34bBudgetCode", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + EXTENSION_PANEL1 + ":34bBudgetCode", TextInputField.class));
        components.put("34cBudgetLine", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + EXTENSION_PANEL1 + ":34cBudgetLine", TextInputField.class));
        components.put("34dBudgetActivity", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + EXTENSION_PANEL1 + ":34dBudgetActivity", TextInputField.class));
        components.put("budgetMTEFTable", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + BUDGET_MTEF_TABLE, PermissionAwareContainer.class));
        components.put("disbursementGroup", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + DISBURSEMENT_GROUP, VisibilityAwareContainer.class));
        components.put("disbursement", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + DISBURSEMENT_GROUP + ":disbursement", TextInputField.class));
        components.put("disbursementP1", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + DISBURSEMENT_GROUP + ":disbursementP1", TextInputField.class));
        components.put("disbursementP2", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + DISBURSEMENT_GROUP + ":disbursementP2", TextInputField.class));
        components.put("disbursementP3", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + DISBURSEMENT_GROUP + ":disbursementP3", TextInputField.class));
        components.put("disbursementP4", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + DISBURSEMENT_GROUP + ":disbursementP4", TextInputField.class));
        components.put("codeGroup", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + CODE_GROUP, VisibilityAwareContainer.class));
        components.put("code", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + CODE_GROUP + ":code", TextInputField.class));
        components.put("codeP1", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + CODE_GROUP + ":codeP1", TextInputField.class));
        components.put("codeP2", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + CODE_GROUP + ":codeP2", TextInputField.class));
        components.put("codeP3", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + CODE_GROUP + ":codeP3", TextInputField.class));
        components.put("codeP4", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + CODE_GROUP + ":codeP4", TextInputField.class));
        components.put("lineGroup", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + LINE_GROUP, VisibilityAwareContainer.class));
        components.put("line", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + LINE_GROUP + ":line", TextInputField.class));
        components.put("lineP1", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + LINE_GROUP + ":lineP1", TextInputField.class));
        components.put("lineP2", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + LINE_GROUP + ":lineP2", TextInputField.class));
        components.put("lineP3", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + LINE_GROUP + ":lineP3", TextInputField.class));
        components.put("lineP4", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + LINE_GROUP + ":lineP4", TextInputField.class));
        components.put("activityGroup", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + ACTIVITY_GROUP, VisibilityAwareContainer.class));
        components.put("activity", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + ACTIVITY_GROUP + ":activity", TextInputField.class));
        components.put("activityP1", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + ACTIVITY_GROUP + ":activityP1", TextInputField.class));
        components.put("activityP2", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + ACTIVITY_GROUP + ":activityP2", TextInputField.class));
        components.put("activityP3", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + ACTIVITY_GROUP + ":activityP3", TextInputField.class));
        components.put("activityP4", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + ACTIVITY_GROUP + ":activityP4", TextInputField.class));
        components.put("32bOtherCurrency", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + EXTENSION_PANEL3 + ":32bOtherCurrency", DropDownField.class));
        components.put("32cExchangeRate", new DimComponentInfo(CustomVolumeDataTab.KEY, VOLUME_DATA_TAB_PATH + EXTENSION_PANEL3 + ":32cExchangeRate", TextInputField.class));
    }

    private void initForLoansOnlyTab() {
        components.put("44typeOfRepayment", new DimComponentInfo(ForLoansOnlyTab.KEY, FOR_LOANS_ONLY_TAB_PATH + "44typeOfRepayment", DropDownField.class));
        components.put("45numberOfRepayments", new DimComponentInfo(ForLoansOnlyTab.KEY, FOR_LOANS_ONLY_TAB_PATH + "45numberOfRepayments", DropDownField.class));
        components.put("46interestRate", new DimComponentInfo(ForLoansOnlyTab.KEY, FOR_LOANS_ONLY_TAB_PATH + "46interestRate", TextInputField.class));
        components.put("47secondInterestRate", new DimComponentInfo(ForLoansOnlyTab.KEY, FOR_LOANS_ONLY_TAB_PATH + "47secondInterestRate", TextInputField.class));
        components.put("48firstRepaymentDate", new DimComponentInfo(ForLoansOnlyTab.KEY, FOR_LOANS_ONLY_TAB_PATH + "48firstRepaymentDate", DateInputField.class));
        components.put("49finalRepaymentDate", new DimComponentInfo(ForLoansOnlyTab.KEY, FOR_LOANS_ONLY_TAB_PATH + "49finalRepaymentDate", DateInputField.class));
        components.put("50interestReceived", new DimComponentInfo(ForLoansOnlyTab.KEY, FOR_LOANS_ONLY_TAB_PATH + "50interestReceived", TextInputField.class));
        components.put("51principalDisbursed", new DimComponentInfo(ForLoansOnlyTab.KEY, FOR_LOANS_ONLY_TAB_PATH + "51principalDisbursed", TextInputField.class));
        components.put("52arrearsOfPrincipals", new DimComponentInfo(ForLoansOnlyTab.KEY, FOR_LOANS_ONLY_TAB_PATH + "52arrearsOfPrincipals", TextInputField.class));
        components.put("53arrearsOfInterest", new DimComponentInfo(ForLoansOnlyTab.KEY, FOR_LOANS_ONLY_TAB_PATH + "53arrearsOfInterest", TextInputField.class));
    }

    private void initCustomForLoansOnlyTab() {
        initForLoansOnlyTab();
        components.put("54futureDebtPrincipal", new DimComponentInfo(CustomForLoansOnlyTab.KEY, FOR_LOANS_ONLY_TAB_PATH + "54futureDebtPrincipal", TextInputField.class));
        components.put("54futureDebtInterest", new DimComponentInfo(CustomForLoansOnlyTab.KEY, FOR_LOANS_ONLY_TAB_PATH + "54futureDebtInterest", TextInputField.class));
    }
}
