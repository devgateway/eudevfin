/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.custom;

import org.devgateway.eudevfin.dim.pages.transaction.crs.CRSTransactionPermissionProvider;
import org.devgateway.eudevfin.dim.pages.transaction.crs.ForLoansOnlyTab;
import org.devgateway.eudevfin.dim.pages.transaction.crs.SupplementaryDataTab;
import org.devgateway.eudevfin.ui.common.permissions.RoleActionMapping;
import org.devgateway.eudevfin.ui.common.temporary.SB;

import java.util.HashMap;

/**
 * Different permission provider for the {@link CustomTransactionPage}
 *
 * @author aartimon
 * @since 12/12/13
 */
public class CustomTransactionPermissionProvider extends CRSTransactionPermissionProvider {
    @Override
    protected HashMap<String, RoleActionMapping> initPermissions() {
        HashMap<String, RoleActionMapping> permissions = super.initPermissions();

        /**
         * Identification Data
         */
        permissions.put("1aDataAsPerDate", new RoleActionMapping().required(SB.BILATERAL_ODA_FORWARD_SPENDING));
        //override permission for 1bCommitmentDate
        permissions.get("1bCommitmentDate").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE).notCollected(SB.MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("4crsId").required(SB.NON_ODA_OTHER_FLOWS).render(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE, true)
                .render(SB.MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE, true);
        permissions.get("5donorProjectNumber").render(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE, true)
                .render(SB.MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE, true);

        permissions.put("6aLevelOfCertainty", new RoleActionMapping().required(SB.BILATERAL_ODA_FORWARD_SPENDING));
        
        /**
         * Basic Data
         */

        permissions.put("7aRecipientCode", new RoleActionMapping().required(SB.BILATERAL_ODA_FORWARD_SPENDING));
        permissions.put("7bCPA", new RoleActionMapping().required(SB.BILATERAL_ODA_CRS));
        permissions.put("7cPriorityStatus", new RoleActionMapping().required(SB.BILATERAL_ODA_FORWARD_SPENDING));
        permissions.put("7dPhasingOutYear", new RoleActionMapping().required(SB.BILATERAL_ODA_FORWARD_SPENDING));

        //override permissions for
        permissions.get("8channelDelivery").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE).notCollected(SB.NON_ODA_OOF_EXPORT);
        permissions.get("9channelCode").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE).notCollected(SB.NON_ODA_OOF_EXPORT);
        permissions.put("10bilateralMultilateral",new RoleActionMapping().required(SB.all()));
        permissions.put("11typeOfFlow", new RoleActionMapping().required(SB.all()));
        permissions.get("14activityProjectTitle").notCollected(SB.NON_ODA_PRIVATE_GRANTS).render(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE, true)
                .render(SB.MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE, true);

        permissions.get("15sectorPurposeCode").notCollected(SB.NON_ODA_PRIVATE_GRANTS);


        permissions.put("14bisUploadDocumentation", new RoleActionMapping().required(SB.BILATERAL_ODA_CRS).required(SB.MULTILATERAL_ODA_CRS));
        permissions.put("14aProjectCoFinanced", new RoleActionMapping().required(SB.BILATERAL_ODA_CRS));
        permissions.put("14coFinancing", new RoleActionMapping().required(SB.BILATERAL_ODA_CRS));

        /**
         * Supplementary Data
         */
        //override permissions
        permissions.get(SupplementaryDataTab.KEY).notCollected(SB.NON_ODA_OOF_NON_EXPORT).render(SB.MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE);

        permissions.get("16geographicalTargetArea").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("17startingDate").render(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE, true)
                .render(SB.MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("18completionDate").render(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE, true)
                .render(SB.MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("19description").render(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE, true)
                .render(SB.MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("20genderEquality").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("21aidToEnvironment").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("22pdGg").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("23tradeDevelopment").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("24freestandingTechnicalCooperation").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("25programmeBasedApproach").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("26investmentProject").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("27associatedFinancing").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("28biodiversity").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("29ccMitigation").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("30ccAdaptation").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("31desertification").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        //end override
        permissions.put("96rmnch", new RoleActionMapping().required(SB.BILATERAL_ODA_CRS).required(SB.BILATERAL_ODA_FORWARD_SPENDING));
        /**
         * Volume Data
         */
        //override permissions
        permissions.get("33commitments").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE).notCollected(SB.MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.put("34amountsExtended", new RoleActionMapping().required(SB.all()).notCollected(SB.BILATERAL_ODA_FORWARD_SPENDING));
        permissions.get("35amountsReceived").render(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE, true)
                .render(SB.MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE, true);
        permissions.get("36amountUntied").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);

        permissions.get("37amountPartiallyUntied").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("38amountTied").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("39amountOfIRTC").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("40amountOfExpertsCommitments").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("41amountOfExpertsExtended").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("42amountOfExportCredit").notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        //end override

        permissions.put("34bBudgetCode", new RoleActionMapping().required(SB.BILATERAL_ODA_CRS).required(SB.MULTILATERAL_ODA_CRS));
        permissions.put("34cBudgetLine", new RoleActionMapping().required(SB.BILATERAL_ODA_CRS).required(SB.MULTILATERAL_ODA_CRS));
        permissions.put("34dBudgetActivity", new RoleActionMapping().required(SB.BILATERAL_ODA_CRS).required(SB.MULTILATERAL_ODA_CRS));
        permissions.put("budgetMTEFTable", new RoleActionMapping().required(SB.BILATERAL_ODA_FORWARD_SPENDING));

        /**
         * For Loans only
         */
        //override permissions
        permissions.get(ForLoansOnlyTab.KEY).notCollected(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE).notCollected(SB.MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        permissions.get("44typeOfRepayment").notCollected(SB.NON_ODA_OOF_NON_EXPORT);
        permissions.get("45numberOfRepayments").notCollected(SB.NON_ODA_OOF_NON_EXPORT);
        permissions.get("46interestRate").notCollected(SB.NON_ODA_OOF_NON_EXPORT);
        permissions.get("47secondInterestRate").notCollected(SB.NON_ODA_OOF_NON_EXPORT);
        permissions.get("48firstRepaymentDate").notCollected(SB.NON_ODA_OOF_NON_EXPORT);
        permissions.get("49finalRepaymentDate").notCollected(SB.NON_ODA_OOF_NON_EXPORT);
        //end override

        //hidden
        permissions.put("54futureDebtPrincipal", new RoleActionMapping());
        permissions.put("54futureDebtInterest", new RoleActionMapping());

        /**
         * Additional Info
         */
        permissions.put(AdditionalInfoTab.KEY, new RoleActionMapping().required(SB.BILATERAL_ODA_FORWARD_SPENDING));


        return permissions;
    }
}
