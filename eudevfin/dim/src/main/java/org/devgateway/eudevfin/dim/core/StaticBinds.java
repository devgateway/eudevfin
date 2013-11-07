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

}
