/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.providers;

import org.devgateway.eudevfin.dim.exceptions.CurrencyProviderExceptiom;
import org.devgateway.eudevfin.financial.service.CurrencyMetadataService;
import org.devgateway.eudevfin.financial.util.CurrencyConstants;
import org.devgateway.eudevfin.ui.common.providers.SpringCategoryProviderProxy;
import org.joda.money.CurrencyUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CurrencyUnitProviderFactory {

    public final static String NATIONAL_UNSORTED_CURRENCIES_PROVIDER = "national_unsorted_currencies_provider";
    public final static String ALL_SORTED_CURRENCIES_PROVIDER = "all_sorted_currencies_provider";

    @Autowired
    private CurrencyMetadataService service;

    @Value("#{commonProperties['sel.defaultSelectorPageSize']}")
    private Integer pageSize;

    private SpringCategoryProviderProxy<CurrencyUnit> nationalUnsorted;
    private SpringCategoryProviderProxy<CurrencyUnit> allSorted;

    @PostConstruct
    private void init() {
        this.nationalUnsorted = new SpringCategoryProviderProxy<>(new CurrencyUnitProvider(service, pageSize, false,
                CurrencyConstants.NATIONAL_CURRENCIES_LIST));
        this.allSorted = new SpringCategoryProviderProxy<>(new CurrencyUnitProvider(service, pageSize, true,
                CurrencyConstants.ALL_CURRENCIES_LIST));
    }

    public SpringCategoryProviderProxy<CurrencyUnit> getCurrencyUnitProviderInstance(String providerType) {
        if (NATIONAL_UNSORTED_CURRENCIES_PROVIDER.equals(providerType))
            return this.nationalUnsorted;
        else if (ALL_SORTED_CURRENCIES_PROVIDER.equals(providerType))
            return this.allSorted;
        throw new CurrencyProviderExceptiom("No such currency unit provider: " + providerType);
    }
}
