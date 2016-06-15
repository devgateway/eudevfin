/**
 * *****************************************************************************
 * Copyright (c) 2014 Development Gateway. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the GNU
 * Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************
 */
package org.devgateway.eudevfin.metadata.common.util;

public class CategoryConstants {

    /**
     * codes for categories that are used as tags
     */
    public static final String TAG_OF_TAG = "TAG_OF_TAG";
    public static final String ROOT_TAG = "ROOT_TAG";
    public static final String ALL_SECTOR_TAG = "ALL_SECTOR_TAG";
    public static final String SECTOR_TAG = "SECTOR_TAG";
    public static final String SUB_SECTOR_TAG = "SUB_SECTOR_TAG";
    public static final String SUB_SUB_SECTOR_TAG = "SUB_SUB_SECTOR_TAG";
    public static final String TYPE_OF_FLOW_TAG = "TYPE_OF_FLOW_TAG";
    public static final String ALL_TYPE_OF_AID_TAG = "ALL_TYPE_OF_AID_TAG";
    public static final String TYPE_OF_AID_TAG = "TYPE_OF_AID_TAG";
    public static final String SUB_TYPE_OF_AID_TAG = "SUB_TYPE_OF_AID_TAG";
    public static final String ALL_TYPE_OF_FINANCE_TAG = "ALL_TYPE_OF_FINANCE_TAG";
    public static final String TYPE_OF_FINANCE_TAG = "TYPE_OF_FINANCE_TAG";
    public static final String SUB_TYPE_OF_FINANCE_TAG = "SUB_TYPE_OF_FINANCE_TAG";
    public static final String MARKER_TAG = "MARKER_TAG";
    public static final String BI_MULTILATERAL_TAG = "BI_MULTILATERAL_TAG";
    public static final String CHANNEL_TAG = "CHANNEL_TAG";
    public static final String RECIPIENT_TAG = "RECIPIENT_TAG";
    public static final String GEOGRAPHY_TAG = "GEOGRAPHY_TAG";
    public static final String NATURE_OF_SUBMISSION_TAG = "NATURE_OF_SUBMISSION_TAG";
    public static final String LEVEL_OF_CERTAINTY_TAG = "LEVEL_OF_CERTAINTY_TAG";
    public static final String INCOME_GROUP_TAG = "INCOME_GROUP_TAG";
    public static final String TYPE_OF_REPAYMENT_TAG = "TYPE_OF_REPAYMENT_TAG";
    public static final String NUM_OF_REPAYMENTS_PER_ANNUM_TAG = "NUM_OF_REPAYMENTS_PER_ANNUM_TAG";
    public static final String SIGNIFICANCE_TAG = "SIGNIFICANCE_TAG";
    public static final String SIGNIFICANCE_FOR_PROGRAMME_TAG = "SIGNIFICANCE_FOR_PROGRAMME_TAG";
    public static final String CURRENCY_CATEGORY_TAG = "CURRENCY_CATEGORY_TAG";
    public static final String OTHER_CURRENCY_TAG = "OTHER_CURRENCY_TAG";
    public static final String MAIN_CURRENCY_TAG = "MAIN_CURRENCY_TAG";
    public static final String PRIORITY_STATUS_TAG = "PRIORITY_STATUS_TAG";
    public static final String RMNCH_TAG = "RMNCH_TAG";

    /**
     * category prefixes
     */
    public static final String NATURE_OF_SUBMISSION_PREFIX = "NATURE_OF_SUBMISSION##";
    public static final String BI_MULTILATERAL_PREFIX = "BI_MULTILATERAL##";
    public static final String TYPE_OF_FLOW_PREFIX = "TYPE_OF_FLOW##";
    public static final String TYPE_OF_FINANCE_PREFIX = "TYPE_OF_FINANCE##";
    public static final String TYPE_OF_REPAYMENT_PREFIX = "TYPE_OF_REPAYMENT##";
    public static final String NUM_OF_REPAYMENTS_PER_ANNUM_PREFIX = "NUM_OF_REPAYMENTS_PER_ANNUM##";
    public static final String RMNCH_PREFIX = "RMNCH##";
    public static final String MARKER_PREFIX = "MARKER##";

    /**
     * codes for ROOT categories
     */
    public static final String ROOT_MARKER_CATEGORY = "ROOT_MARKER_CATEGORY";
    public static final String ROOT_TYPE_OF_AID_CATEGORY = "ROOT_TYPE_OF_AID_CATEGORY";
    public static final String ROOT_TYPE_OF_FLOW_CATEGORY = "ROOT_TYPE_OF_FLOW_CATEGORY";
    public static final String ROOT_TYPE_OF_FINANCE_CATEGORY = "ROOT_TYPE_OF_FINANCE_CATEGORY";
    public static final String ROOT_BI_MULTILATERAL_CATEGORY = "ROOT_BI_MULTILATERAL_CATEGORY";
    public static final String ROOT_NATURE_OF_SUBMISSION_CATEGORY = "ROOT_NATURE_OF_SUBMISSION_CATEGORY";
    public static final String ROOT_SECTOR_CATEGORY = "ROOT_SECTOR_CATEGORY";
    public static final String ROOT_CHANNEL_CATEGORY = "ROOT_CHANNEL_CATEGORY";
    public static final String ROOT_TYPE_OF_REPAYMENT_CATEGORY = "ROOT_TYPE_OF_REPAYMENT_CATEGORY";
    public static final String ROOT_NUM_OF_REPAYMENTS_PER_ANNUM_CATEGORY = "ROOT_NUM_OF_REPAYMENTS_PER_ANNUM_CATEGORY";
    public static final String ROOT_SIGNIFICANCE_CATEGORY = "ROOT_SIGNIFICANCE_CATEGORY";
    public static final String ROOT_SIGNIFICANCE_FOR_PROGRAMME_CATEGORY = "ROOT_SIGNIFICANCE_FOR_PROGRAMME_CATEGORY";
    public static final String ROOT_CURRENCY_CATEGORY = "ROOT_CURRENCY_CATEGORY";

    public static final class TypeOfFlow {
        public static final String TYPE_OF_PROJECT_PREFIX				= "TYPE_OF_PROJECT##";

        public static final String NON_FLOW = "TYPE_OF_FLOW##40";
        public static final String ODA = "TYPE_OF_FLOW##10";
    }

    public static final class TypeOfAid {
        public static final String ROOT_TYPE_OF_PROJECT_CATEGORY						= "ROOT_TYPE_OF_PROJECT_CATEGORY";

        public static final String B01 = "B01";
        public static final String B02 = "B02";
        public static final String F01 = "F01";
    }

    public static final class Markers {

        public static final String MARKER_3 = "MARKER##3";
    }

    public static final class BiMultilateral {

        public static final String BI_MULTILATERAL_1 = "BI_MULTILATERAL##1";
        public static final String BI_MULTILATERAL_2 = "BI_MULTILATERAL##2";
        public static final String BI_MULTILATERAL_3 = "BI_MULTILATERAL##3";
        public static final String BI_MULTILATERAL_4 = "BI_MULTILATERAL##3";
        public static final String BI_MULTILATERAL_7 = "BI_MULTILATERAL##7";
    }

    public static final class TypeOfFinance {

        public static final String ROOT_NON_FLOW = "TYPE_OF_FINANCE##0";

        public static final class NonFlow {

            public static final String GNI = "TYPE_OF_FINANCE##1";
            public static final String ODA_PERCENT_GNI = "TYPE_OF_FINANCE##2";
            public static final String TOTAL_FLOWS_PERCENT_GNI = "TYPE_OF_FINANCE##3";
            public static final String POPULATION = "TYPE_OF_FINANCE##4";
        }
    }

    public static final class NatureOfSubmission {

        public static final String NEW_ACTIVITY_REPORTED = "NATURE_OF_SUBMISSION##1";
        public static final String REVISION = "NATURE_OF_SUBMISSION##2";
        public static final String PREVIOUSLY_REPORTED_ACTIVITY = "NATURE_OF_SUBMISSION##3";
        public static final String PROVISIONAL_DATA = "NATURE_OF_SUBMISSION##5";
        public static final String COMMITMENT_EQ_DISBURSEMENT = "NATURE_OF_SUBMISSION##8";
    }

}
