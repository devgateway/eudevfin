/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.Loop;
import org.devgateway.eudevfin.dim.pages.transaction.crs.TransactionPage;
import org.devgateway.eudevfin.dim.pages.transaction.custom.CustomTransactionPage;
import org.devgateway.eudevfin.ui.common.components.*;
import org.devgateway.eudevfin.ui.common.components.tabs.BootstrapJSTabbedPanel;
import org.junit.Test;

/**
 * @author Alexandru Artimon
 * @since 14/04/14
 */
public class AdvancedQuestionnaireTest extends LoginPageTest {

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


    private void testTransactionPage() {
        tester.assertComponent("form", Form.class);
        tester.assertComponent("form:bc", BootstrapJSTabbedPanel.class);
        tester.assertComponent("form:submit", TransactionPage.TransactionPageSubmitButton.class);
        tester.assertComponent("form:cancel", BootstrapCancelButton.class);
        tester.assertComponent("feedback", NotificationPanel.class);
        tester.assertComponent("form:bc:tabContent", Loop.class);
        testCustomIdentificationDataTab();
        testCustomBasicDataTab();
        testCustomSupplementaryDataTab();
        testCustomVolumeDataTab();
        testCustomForLoansOnlyTab();
    }

    private void testIdentificationDataTab() {
        tester.assertComponent(IDENTIFICATION_DATA_TAB_PATH + "1reportingYear", TextInputField.class);
        tester.assertComponent(IDENTIFICATION_DATA_TAB_PATH + "1bCommitmentDate", DateInputField.class);
        tester.assertComponent(IDENTIFICATION_DATA_TAB_PATH + "3extendingAgency", DropDownField.class);
        tester.assertComponent(IDENTIFICATION_DATA_TAB_PATH + "4crsId", TextInputField.class);
        tester.assertComponent(IDENTIFICATION_DATA_TAB_PATH + "5donorProjectNumber", TextInputField.class);
        tester.assertComponent(IDENTIFICATION_DATA_TAB_PATH + "6natureSubmission", DropDownField.class);
    }

    private void testCustomIdentificationDataTab() {
        testIdentificationDataTab();
        tester.assertComponent(IDENTIFICATION_DATA_TAB_PATH + "1aDataAsPerDate", DateInputField.class);
        tester.assertComponent(IDENTIFICATION_DATA_TAB_PATH + "6aLevelOfCertainty", DropDownField.class);


    }

    private void testBasicDataTab() {
        tester.assertComponent(BASIC_DATA_TAB_PATH + "7recipient", DropDownField.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + "8channelDelivery", DropDownField.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + "10bilateralMultilateral", DropDownField.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + "11typeOfFlow", DropDownField.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + "12typeOfFinance", DropDownField.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + "13typeOfAid", DropDownField.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + "14activityProjectTitle", TextAreaInputField.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + "15sectorPurposeCode", DropDownField.class);
    }

    private void testCustomBasicDataTab() {
        testBasicDataTab();
        tester.assertComponent(BASIC_DATA_TAB_PATH + EXTENSION_PANEL3 + ":7aRecipientCode", DropDownField.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + EXTENSION_PANEL3 + ":7cPriorityStatus", DropDownField.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + EXTENSION_PANEL1 + ":7bCPA", DropDownField.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + EXTENSION_PANEL1 + ":7dPhasingOutYear", TextInputField.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + EXTENSION_PANEL2 + ":14bisUploadDocumentation", MultiFileUploadField.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + EXTENSION_PANEL2 + ":14aProjectCoFinanced", DropDownField.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + CO_FINANCING, PermissionAwareContainer.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + GROUP_1ST_AGENCY, VisibilityAwareContainer.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + GROUP_1ST_AGENCY + "14b1stAgency", DropDownField.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + GROUP_1ST_AGENCY + "14c1stAgencyAmount", TextInputField.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + GROUP_1ST_AGENCY + "14d1stAgencyCurrency", DropDownField.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + CO_FINANCING + ":14e2ndAgency", DropDownField.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + CO_FINANCING + ":14f2ndAgencyAmount", TextInputField.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + CO_FINANCING + ":14g2ndAgencyCurrency", DropDownField.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + CO_FINANCING + ":14h3rdAgency", DropDownField.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + CO_FINANCING + ":14i3rdAgencyAmount", TextInputField.class);
        tester.assertComponent(BASIC_DATA_TAB_PATH + CO_FINANCING + ":14j3rdAgencyCurrency", DropDownField.class);
    }

    private void testSupplementaryDataTab() {
        tester.assertComponent(SUPPLEMENTARY_DATA_TAB_PATH + "16geographicalTargetArea", TextInputField.class);
        tester.assertComponent(SUPPLEMENTARY_DATA_TAB_PATH + "17startingDate", DateInputField.class);
        tester.assertComponent(SUPPLEMENTARY_DATA_TAB_PATH + "18completionDate", DateInputField.class);
        tester.assertComponent(SUPPLEMENTARY_DATA_TAB_PATH + "19description", TextAreaInputField.class);
        tester.assertComponent(SUPPLEMENTARY_DATA_TAB_PATH + POLICY_OBJECTIVES_GROUP, VisibilityAwareContainer.class);
        tester.assertComponent(SUPPLEMENTARY_DATA_TAB_PATH + POLICY_OBJECTIVES_GROUP + ":20genderEquality", DropDownField.class);
        tester.assertComponent(SUPPLEMENTARY_DATA_TAB_PATH + POLICY_OBJECTIVES_GROUP + ":21aidToEnvironment", DropDownField.class);
        tester.assertComponent(SUPPLEMENTARY_DATA_TAB_PATH + POLICY_OBJECTIVES_GROUP + ":22pdGg", DropDownField.class);
        tester.assertComponent(SUPPLEMENTARY_DATA_TAB_PATH + POLICY_OBJECTIVES_GROUP + ":23tradeDevelopment", DropDownField.class);
        tester.assertComponent(SUPPLEMENTARY_DATA_TAB_PATH + POLICY_OBJECTIVES_GROUP + ":28biodiversity", DropDownField.class);
        tester.assertComponent(SUPPLEMENTARY_DATA_TAB_PATH + POLICY_OBJECTIVES_GROUP + ":29ccMitigation", DropDownField.class);
        tester.assertComponent(SUPPLEMENTARY_DATA_TAB_PATH + POLICY_OBJECTIVES_GROUP + ":30ccAdaptation", DropDownField.class);
        tester.assertComponent(SUPPLEMENTARY_DATA_TAB_PATH + POLICY_OBJECTIVES_GROUP + ":31desertification", DropDownField.class);
        tester.assertComponent(SUPPLEMENTARY_DATA_TAB_PATH + TYPE_OF_AID_GROUP, VisibilityAwareContainer.class);
        tester.assertComponent(SUPPLEMENTARY_DATA_TAB_PATH + TYPE_OF_AID_GROUP + ":24freestandingTechnicalCooperation", DropDownField.class);
        tester.assertComponent(SUPPLEMENTARY_DATA_TAB_PATH + TYPE_OF_AID_GROUP + ":25programmeBasedApproach", DropDownField.class);
        tester.assertComponent(SUPPLEMENTARY_DATA_TAB_PATH + TYPE_OF_AID_GROUP + ":26investmentProject", DropDownField.class);
        tester.assertComponent(SUPPLEMENTARY_DATA_TAB_PATH + TYPE_OF_AID_GROUP + ":27associatedFinancing", DropDownField.class);
    }

    private void testCustomSupplementaryDataTab() {
        testSupplementaryDataTab();
        tester.assertComponent(SUPPLEMENTARY_DATA_TAB_PATH + "otherMarkersContainer", VisibilityAwareContainer.class);
        tester.assertComponent(SUPPLEMENTARY_DATA_TAB_PATH + "otherMarkersContainer:" + "96rmnch", DropDownField.class);
    }

    private void testVolumeDataTab() {
        tester.assertComponent(VOLUME_DATA_TAB_PATH + "32currency", DropDownField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + "33commitments", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + "34amountsExtended", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + "35amountsReceived", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + "36amountUntied", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + "37amountPartiallyUntied", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + "38amountTied", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + "39amountOfIRTC", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + "40amountOfExpertsCommitments", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + "41amountOfExpertsExtended", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + "42amountOfExportCredit", TextInputField.class);
    }

    private void testCustomVolumeDataTab() {
        testVolumeDataTab();
        tester.assertComponent(VOLUME_DATA_TAB_PATH + EXTENSION_PANEL1 + ":34bBudgetCode", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + EXTENSION_PANEL1 + ":34cBudgetLine", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + EXTENSION_PANEL1 + ":34dBudgetActivity", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + BUDGET_MTEF_TABLE, PermissionAwareContainer.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + DISBURSEMENT_GROUP, VisibilityAwareContainer.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + DISBURSEMENT_GROUP + ":disbursement", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + DISBURSEMENT_GROUP + ":disbursementP1", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + DISBURSEMENT_GROUP + ":disbursementP2", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + DISBURSEMENT_GROUP + ":disbursementP3", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + DISBURSEMENT_GROUP + ":disbursementP4", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + CODE_GROUP, VisibilityAwareContainer.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + CODE_GROUP + ":code", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + CODE_GROUP + ":codeP1", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + CODE_GROUP + ":codeP2", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + CODE_GROUP + ":codeP3", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + CODE_GROUP + ":codeP4", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + LINE_GROUP, VisibilityAwareContainer.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + LINE_GROUP + ":line", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + LINE_GROUP + ":lineP1", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + LINE_GROUP + ":lineP2", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + LINE_GROUP + ":lineP3", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + LINE_GROUP + ":lineP4", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + ACTIVITY_GROUP, VisibilityAwareContainer.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + ACTIVITY_GROUP + ":activity", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + ACTIVITY_GROUP + ":activityP1", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + ACTIVITY_GROUP + ":activityP2", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + ACTIVITY_GROUP + ":activityP3", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + ACTIVITY_GROUP + ":activityP4", TextInputField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + EXTENSION_PANEL3 + ":32bOtherCurrency", DropDownField.class);
        tester.assertComponent(VOLUME_DATA_TAB_PATH + EXTENSION_PANEL3 + ":32cExchangeRate", TextInputField.class);
    }

    private void testForLoansOnlyTab() {
        tester.assertComponent(FOR_LOANS_ONLY_TAB_PATH + "44typeOfRepayment", DropDownField.class);
        tester.assertComponent(FOR_LOANS_ONLY_TAB_PATH + "45numberOfRepayments", DropDownField.class);
        tester.assertComponent(FOR_LOANS_ONLY_TAB_PATH + "46interestRate", TextInputField.class);
        tester.assertComponent(FOR_LOANS_ONLY_TAB_PATH + "47secondInterestRate", TextInputField.class);
        tester.assertComponent(FOR_LOANS_ONLY_TAB_PATH + "48firstRepaymentDate", DateInputField.class);
        tester.assertComponent(FOR_LOANS_ONLY_TAB_PATH + "49finalRepaymentDate", DateInputField.class);
        tester.assertComponent(FOR_LOANS_ONLY_TAB_PATH + "50interestReceived", TextInputField.class);
        tester.assertComponent(FOR_LOANS_ONLY_TAB_PATH + "51principalDisbursed", TextInputField.class);
        tester.assertComponent(FOR_LOANS_ONLY_TAB_PATH + "52arrearsOfPrincipals", TextInputField.class);
        tester.assertComponent(FOR_LOANS_ONLY_TAB_PATH + "53arrearsOfInterest", TextInputField.class);
    }

    private void testCustomForLoansOnlyTab() {
        testForLoansOnlyTab();
        tester.assertComponent(FOR_LOANS_ONLY_TAB_PATH + "54futureDebtPrincipal", TextInputField.class);
        tester.assertComponent(FOR_LOANS_ONLY_TAB_PATH + "54futureDebtInterest", TextInputField.class);
    }


    @Test
    public void testRenderPage() {
        testLoginAsUser();
        testMenuItem(true, "./custom?transactionType=bilateralOda.advanceQuestionnaire");
        tester.executeUrl("./custom?transactionType=bilateralOda.advanceQuestionnaire");
        tester.assertRenderedPage(CustomTransactionPage.class);
        testTransactionPage();
    }


}
