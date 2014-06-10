/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.crs;

import org.devgateway.eudevfin.ui.common.permissions.PermissionProvider;
import org.devgateway.eudevfin.ui.common.permissions.RoleActionMapping;
import org.devgateway.eudevfin.ui.common.temporary.SB;

import java.util.HashMap;

/**
 * Basic class that will be used to get the permissions
 *
 * @author aartimon
 * @since 12/12/13
 */
public class CRSTransactionPermissionProvider extends PermissionProvider {

    @Override
    protected HashMap<String, RoleActionMapping> initPermissions() {
        HashMap<String, RoleActionMapping> permissions = new HashMap<>();
        /**
         * Identification Data
         */
        permissions.put("1bCommitmentDate", new RoleActionMapping().required(SB.allODA()));
        permissions.put("3extendingAgency", new RoleActionMapping().required(SB.allODA()).required(SB.allOOF()));
        permissions.put("4crsId", new RoleActionMapping().required(SB.allODA()).required(SB.allOOF()).required(SB.allPriv()));
        permissions.put("5donorProjectNumber", new RoleActionMapping().required(SB.allODA()).required(SB.allOOF()).required(SB.NON_ODA_PRIVATE_GRANTS));
        permissions.put("6natureSubmission", new RoleActionMapping().required(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE).
        		required(SB.BILATERAL_ODA_CRS).required(SB.mulODA()).required(SB.allOOF()).required(SB.allPriv()));
        /**
         * Basic Data
         */
        permissions.put("7recipient", new RoleActionMapping().required(SB.biODA()).required(SB.allOOF()).required(SB.allPriv()));
        permissions.put("8channelDelivery", new RoleActionMapping().required(SB.allODA()).required(SB.NON_ODA_OOF_NON_EXPORT).render(SB.NON_ODA_OOF_EXPORT));
        permissions.put("9channelCode", new RoleActionMapping().required(SB.allODA()).required(SB.NON_ODA_OOF_NON_EXPORT).render(SB.NON_ODA_OOF_EXPORT));
        permissions.put("10bilateralMultilateral", new RoleActionMapping().required(SB.allODA()).required(SB.allOOF()).required(SB.allPriv()));
        permissions.put("13typeOfAid", new RoleActionMapping().required(SB.allODA()));
        permissions.put("14activityProjectTitle", new RoleActionMapping().required(SB.allODA()).required(SB.NON_ODA_OOF_NON_EXPORT).required(SB.NON_ODA_PRIVATE_GRANTS).required(SB.NON_ODA_OTHER_FLOWS));
        permissions.put("15sectorPurposeCode", new RoleActionMapping().required(SB.biODA()).required(SB.allOOF()).render(SB.NON_ODA_PRIVATE_GRANTS).required(SB.NON_ODA_OTHER_FLOWS));
        /**
         * Supplementary Data
         */
        //render the tab just for bilateral ODA and OOF non-export
        permissions.put(SupplementaryDataTab.KEY, new RoleActionMapping().render(SB.biODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        //set field permissions for when the tab is rendered
        permissions.put("16geographicalTargetArea", new RoleActionMapping().required(SB.biODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("17startingDate", new RoleActionMapping().required(SB.biODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("18completionDate", new RoleActionMapping().required(SB.biODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("19description", new RoleActionMapping().required(SB.biODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("20genderEquality", new RoleActionMapping().required(SB.biODA()).required(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("21aidToEnvironment", new RoleActionMapping().required(SB.biODA()).required(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("22pdGg", new RoleActionMapping().required(SB.biODA()).required(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("23tradeDevelopment", new RoleActionMapping().required(SB.biODA()));
        permissions.put("24freestandingTechnicalCooperation", new RoleActionMapping().render(SB.biODA()));
        permissions.put("25programmeBasedApproach", new RoleActionMapping().render(SB.biODA()));
        permissions.put("26investmentProject", new RoleActionMapping().render(SB.biODA()));
        permissions.put("27associatedFinancing", new RoleActionMapping().render(SB.biODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("28biodiversity", new RoleActionMapping().required(SB.biODA()).required(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("29ccMitigation", new RoleActionMapping().required(SB.biODA()).required(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("30ccAdaptation", new RoleActionMapping().required(SB.biODA()).required(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("31desertification", new RoleActionMapping().required(SB.biODA()).required(SB.NON_ODA_OOF_NON_EXPORT));
      
        /**
         * Volume Data
         */
        permissions.put("33commitments", new RoleActionMapping().render(SB.allODA()).render(SB.allOOF()));
        permissions.put("35amountsReceived", new RoleActionMapping().render(SB.allODA()).render(SB.allOOF()).render(SB.NON_ODA_PRIVATE_MARKET));
        permissions.put("36amountUntied", new RoleActionMapping().render(SB.biODA()));
        permissions.put("37amountPartiallyUntied", new RoleActionMapping().render(SB.biODA()));
        permissions.put("38amountTied", new RoleActionMapping().render(SB.biODA()));
        permissions.put("39amountOfIRTC", new RoleActionMapping().render(SB.biODA()));
        permissions.put("40amountOfExpertsCommitments", new RoleActionMapping().render(SB.biODA()));
        permissions.put("41amountOfExpertsExtended", new RoleActionMapping().render(SB.biODA()));
        permissions.put("42amountOfExportCredit", new RoleActionMapping().render(SB.biODA()));
        /**
         * For Loans only
         */
        //render the tab for only the ODA and OOF
        permissions.put(ForLoansOnlyTab.KEY, new RoleActionMapping().render(SB.allODA()).render(SB.allOOF()));
        //set field permissions for when tab is rendered
        permissions.put("44typeOfRepayment", new RoleActionMapping().render(SB.allODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("45numberOfRepayments", new RoleActionMapping().render(SB.allODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("46interestRate", new RoleActionMapping().render(SB.allODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("47secondInterestRate", new RoleActionMapping().render(SB.allODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("48firstRepaymentDate", new RoleActionMapping().render(SB.allODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("49finalRepaymentDate", new RoleActionMapping().render(SB.allODA()).render(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("50interestReceived", new RoleActionMapping().render(SB.allODA()).render(SB.allOOF()));
        permissions.put("51principalDisbursed", new RoleActionMapping().render(SB.biODA()).required(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("52arrearsOfPrincipals", new RoleActionMapping().render(SB.biODA()).required(SB.NON_ODA_OOF_NON_EXPORT));
        permissions.put("53arrearsOfInterest", new RoleActionMapping().render(SB.biODA()).required(SB.NON_ODA_OOF_NON_EXPORT));

        return permissions;
    }
}
