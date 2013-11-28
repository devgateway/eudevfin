/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    aartimon
 */

package org.devgateway.eudevfin.dim.core;

import com.vaynberg.wicket.select2.ChoiceProvider;
import com.vaynberg.wicket.select2.Response;
import com.vaynberg.wicket.select2.StringTextChoiceProvider;

import java.util.Arrays;

/**
 * Temporary class to simulate binds with other modules
 * TODO: REMOVE :)
 *
 * @author aartimon@developmentgateway.org
 * @since 30 October 2013
 */
public class StaticBinds {
    public static final ChoiceProvider<String> countryProvider = new StringTextChoiceProvider() {
        @Override
        public void query(String term, int page, Response<String> response) {
        response.addAll(Arrays.asList("Bulgaria", "Romania", "Georgia", "Italia", "Slovacia", "Rusia"));
        }
    };

    public static final ChoiceProvider<String> yesNoProvider = new StringTextChoiceProvider() {
        @Override
        public void query(String term, int page, Response<String> response) {
            response.addAll(Arrays.asList("Yes", "No"));
        }
    };

    public static final String BILATERAL_ODA_ADVANCED_QUESTIONNAIRE = "bilateralOda.advancedQuestionnaire";
    public static final String BILATERAL_ODA_CRS = "bilateralOda.CRS";
    public static final String BILATERAL_ODA_FORWARD_SPENDING = "bilateralOda.forwardSpending";

    public static final String MULTILATERAL_ODA_ADVANCED_QUESTIONNAIRE = "multilateralOda.advancedQuestionnaire";
    public static final String MULTILATERAL_ODA_CRS = "multilateralOda.CRS";

    public static final String NON_ODA_OOF_NON_EXPORT = "nonOda.nonExport";
    public static final String NON_ODA_OOF_EXPORT = "nonOda.export";
    public static final String NON_ODA_PRIVATE_GRANTS = "nonOda.publicGrants";
    public static final String NON_ODA_PRIVATE_MARKET = "nonOda.publicMarket";
    public static final String NON_ODA_OTHER_FLOWS = "nonOda.otherFlows";

}

