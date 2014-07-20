/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.ui.common.temporary;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.ArrayUtils;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.RecipientCategory;
import org.joda.money.CurrencyUnit;

import com.vaynberg.wicket.select2.ChoiceProvider;
import com.vaynberg.wicket.select2.Response;
import com.vaynberg.wicket.select2.TextChoiceProvider;

/**
 * Temporary class to simulate binds with other modules
 * TODO: REMOVE :)
 *
 * @author aartimon@developmentgateway.org
 * @since 30 October 2013
 */
public class SB {

    private static final Category[] categs = {
            newCategory(1L, "First Category"),
            newCategory(2L, "Second Category")
    };

    public static final CurrencyUnit[] currencies = {
            CurrencyUnit.EUR,
            CurrencyUnit.GBP,
            CurrencyUnit.USD
    };

    private static Category newCategory(Long id, String name) {
        Category categ = new Category();
        categ.setId(id);
        categ.setName(name);
        return categ;
    }

    private static final RecipientCategory[] recipientCateg = {
            newRCategory(1L, "Recipient 1", "Romania"),
            newRCategory(2L, "Recipient 2", "Germany")
    };

    private static RecipientCategory newRCategory(Long id, String name, String country) {
        RecipientCategory recipientCategory = new RecipientCategory();
        recipientCategory.setId(id);
        recipientCategory.setName(name);
        Area c = new Area();
        c.setName(country);
        recipientCategory.setArea(c);
        return recipientCategory;
    }


    public static final ChoiceProvider<Category> categoryProvider = new TempCP<Category>(categs) {
        @Override
        protected String getDisplayText(Category choice) {
            return choice.getName();
        }
    };

    public static final ChoiceProvider<CurrencyUnit> currencyProvider = new TextChoiceProvider<CurrencyUnit>() {
        @Override
        protected String getDisplayText(CurrencyUnit choice) {
            return choice.getCode();
        }

        @Override
        protected Object getId(CurrencyUnit choice) {
            return choice.getCode();
        }

        @Override
        public void query(String term, int page, Response<CurrencyUnit> response) {
            for (CurrencyUnit cu : currencies) {
                response.add(cu);
            }
        }

        @Override
        public Collection<CurrencyUnit> toChoices(Collection<String> ids) {
            ArrayList<CurrencyUnit> ret = new ArrayList<>();
            for (String id : ids) {
                for (CurrencyUnit cu : currencies) {
                    if (cu.getCode().equals(id))
                        ret.add(cu);
                }
            }
            return ret;
        }
    };

    public static ChoiceProvider<RecipientCategory> recipientCategoryProvider = new TempCP<RecipientCategory>(recipientCateg) {
        @Override
        protected String getDisplayText(RecipientCategory choice) {
            return choice.getArea().getName();
        }
    };

    public static ChoiceProvider<Boolean> boolProvider = new TextChoiceProvider<Boolean>() {

        @Override
        protected String getDisplayText(Boolean choice) {
            if (choice)
                return "Yes";
            return "No";
        }

        @Override
        protected Object getId(Boolean choice) {
            if (choice)
                return 1L;
            return 0L;
        }

        @Override
        public void query(String term, int page, Response<Boolean> response) {
            response.add(Boolean.TRUE);
            response.add(Boolean.FALSE);
        }

        @Override
        public Collection<Boolean> toChoices(Collection<String> ids) {
            ArrayList<Boolean> ret = new ArrayList<>();
            for (String id : ids) {
                if (id.equals("1"))
                    ret.add(Boolean.TRUE);
                else
                    ret.add(Boolean.FALSE);
            }
            return ret;
        }
    };
   
    public static final String BILATERAL_ODA_ADVANCE_QUESTIONNAIRE = "bilateralOda.advanceQuestionnaire";
    public static final String BILATERAL_ODA_CRS = "bilateralOda.CRS";
    public static final String BILATERAL_ODA_FORWARD_SPENDING = "bilateralOda.forwardSpending";

    public static final String MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE = "multilateralOda.advanceQuestionnaire";
    public static final String MULTILATERAL_ODA_CRS = "multilateralOda.CRS";

    public static final String NON_ODA_OOF_NON_EXPORT = "nonOda.nonExport";
    public static final String NON_ODA_OOF_EXPORT = "nonOda.export";
    public static final String NON_ODA_PRIVATE_GRANTS = "nonOda.privateGrants";
    public static final String NON_ODA_PRIVATE_MARKET = "nonOda.privateMarket";
    public static final String NON_ODA_OTHER_FLOWS = "nonOda.otherFlows";

    public static String[] biODA() {
        return new String[]{BILATERAL_ODA_ADVANCE_QUESTIONNAIRE, BILATERAL_ODA_CRS, BILATERAL_ODA_FORWARD_SPENDING};
    }

    public static String[] mulODA() {
        return new String[]{MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE, MULTILATERAL_ODA_CRS};
    }

    public static String[] allODA() {
        return ArrayUtils.addAll(biODA(), mulODA());
    }

    public static String[] allOOF() {
        return new String[]{NON_ODA_OOF_EXPORT, NON_ODA_OOF_NON_EXPORT};
    }

    public static String[] allPriv() {
        return new String[]{NON_ODA_PRIVATE_GRANTS, NON_ODA_PRIVATE_MARKET};
    }

    public static String[] allNonODA() {
        return new String[]{NON_ODA_OOF_EXPORT, NON_ODA_OOF_NON_EXPORT, NON_ODA_PRIVATE_GRANTS, NON_ODA_PRIVATE_MARKET, NON_ODA_OTHER_FLOWS};
    }

    public static String[] all() {
        return ArrayUtils.addAll(allODA(), allNonODA());
    }

}

