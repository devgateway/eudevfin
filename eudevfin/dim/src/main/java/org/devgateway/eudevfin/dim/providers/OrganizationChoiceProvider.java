/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

/**
 *
 */
package org.devgateway.eudevfin.dim.providers;

import com.vaynberg.wicket.select2.Response;
import com.vaynberg.wicket.select2.TextChoiceProvider;
import org.apache.commons.collections4.CollectionUtils;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author mihai
 */
@Component
public class OrganizationChoiceProvider extends TextChoiceProvider<Organization> {

    @Autowired
    private OrganizationService organizationService;


    public OrganizationChoiceProvider() {
        super();
    }

    /*
     * @see com.vaynberg.wicket.select2.ChoiceProvider#query(java.lang.String,
     * int, com.vaynberg.wicket.select2.Response)
     */
    @Override
    public void query(String term, int page, Response<Organization> response) {
        Iterable<Organization> findAll = organizationService.findAll();
        CollectionUtils.<Organization>addAll(response.getResults(), findAll);
    }

    @Override
    public String getDisplayText(Organization choice) {
        return choice.getName();
    }

    @Override
    public Object getId(Organization choice) {
        return choice.getId();
    }

    @Override
    public Collection<Organization> toChoices(Collection<String> ids) {
        List<Organization> returnable = new ArrayList<>();
        for (String string : ids) {
            Long id = Long.parseLong(string);
            returnable.add(organizationService.findById(id).getEntity());
        }
        return returnable;
    }

}
