/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

/**
 *
 */
package org.devgateway.eudevfin.ui.common.providers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.financial.service.CurrencyMetadataService;
import org.devgateway.eudevfin.financial.util.CurrencyConstants;
import org.joda.money.CurrencyUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.vaynberg.wicket.select2.Response;
import com.vaynberg.wicket.select2.TextChoiceProvider;

/**
 * @author Alex
 */
public class CurrencyUnitProvider extends TextChoiceProvider<CurrencyUnit> {

    private static final long serialVersionUID = -112087305890179315L;

    @Autowired
    private CurrencyMetadataService service;

    private Integer pageSize;

    private boolean alphaSort;

    private String type;
    
    private CurrencyUnit usdEur[] = {CurrencyUnit.EUR,CurrencyUnit.USD};


    public CurrencyUnitProvider(CurrencyMetadataService service,
                                Integer pageSize, boolean alphaSort, String type) {
        super();
        this.service = service;
        this.pageSize = pageSize;
        this.alphaSort = alphaSort;
        this.type = type;
    }

    @Override
    public String getDisplayText(CurrencyUnit choice) {
        return choice.getCode();
    }

    @Override
    public Object getId(CurrencyUnit choice) {
        return choice.getCode();
    }

    @Override
    public void query(String term, int page, Response<CurrencyUnit> response) {
        Pageable pageable = new PageRequest(page, pageSize);
        Page<CurrencyUnit> results = null;
       if(type.equals(CurrencyConstants.USD_EUR_LIST))
        	results= new PageImpl<CurrencyUnit>(Arrays.asList(usdEur),null,2);
        	else results=this.service.findBySearch(term, pageable, this.type);
        List<CurrencyUnit> resultsList = results.getContent();
        if (this.alphaSort) {
            resultsList = new ArrayList<CurrencyUnit>(results.getContent());
            Collections.sort(resultsList);
        }
        if (results != null) {
            for (CurrencyUnit currencyUnit : resultsList) {
                response.add(currencyUnit);
            }
            response.setHasMore(results.hasNextPage());

        }

    }

    @Override
    public Collection<CurrencyUnit> toChoices(Collection<String> ids) {
        List<CurrencyUnit> result = new ArrayList<CurrencyUnit>();
        if (ids != null) {
            for (String code : ids) {
                if (code != null && !code.isEmpty()) {
                    NullableWrapper<CurrencyUnit> nwUnit
                            = this.service.findByCode(code);
                    if (!nwUnit.isNull()) {
                        result.add(nwUnit.getEntity());
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void detach() {
        service = null;
    }
}
