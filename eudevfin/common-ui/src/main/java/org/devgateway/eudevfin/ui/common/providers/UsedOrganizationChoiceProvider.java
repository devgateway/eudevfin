/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.ui.common.providers;

import org.apache.wicket.Session;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.metadata.common.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

/**
 * @author idobre
 * @since 4/7/14
 */
@Component
public class UsedOrganizationChoiceProvider extends AbstractTranslatableProvider<Organization> {
    @Autowired
    private OrganizationService organizationService;


    public UsedOrganizationChoiceProvider() {
        super();
    }

    @Override
    protected Page<Organization> getItemsByTerm(String term, int page) {
        Page<Organization> organization = organizationService.findUsedOrganizationPaginated(Session.get().getLocale()
                .getLanguage(), term, new PageRequest(page, pageSize, sort));

        return organization;
    }

    @Override
    public String getDisplayText(Organization choice) {
        return (choice.getDonorName() == null ? "" : choice.getDonorName() + " - ") + choice.getCode() + " - " + choice.getName();
    }

    @Override
    protected BaseEntityService<Organization> getService() {
        return organizationService;
    }

    @Override
    public void detach() {
        // Spring component no need to detach if added into wicket components with @SpringBean
    }
}
