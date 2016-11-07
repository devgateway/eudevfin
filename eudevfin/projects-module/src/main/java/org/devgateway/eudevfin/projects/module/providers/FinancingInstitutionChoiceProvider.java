/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.providers;

import com.vaynberg.wicket.select2.Response;
import java.util.ArrayList;
import java.util.List;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.metadata.common.service.OrganizationService;
import org.devgateway.eudevfin.ui.common.providers.AbstractTranslatableProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author alcr
 */
@Component
public class FinancingInstitutionChoiceProvider extends AbstractTranslatableProvider<Organization> {

    @Autowired
    private OrganizationService organizationService;
    
    List<Organization> filteredOrganizations;

    public FinancingInstitutionChoiceProvider (List<Organization> filteredOrganizations, OrganizationService organizationService) {        
        this.filteredOrganizations = filteredOrganizations;
        this.organizationService = organizationService;
    }
    
    @Override
    public void query(final String term, final int page, final Response<Organization> response) {
        final List<Organization> ret = new ArrayList<>();
        List<Organization> values;
        if (this.filteredOrganizations != null && this.filteredOrganizations.size() > 0) {
            values = this.filteredOrganizations;
        } else {
            values = new ArrayList<>();
        }

        for (final Organization el : values) {
            if (getDisplayText(el).toLowerCase().contains(term)) {
                ret.add(el);
            }
        }
        response.addAll(ret);
    }

    @Override
    public String getDisplayText(Organization choice) {
        return (choice.getDonorName()==null?"":choice.getDonorName()+" - ")+choice.getCode() + " - " + choice.getAcronym()+ " - "+ choice.getName();
    }

    @Override
    protected BaseEntityService<Organization> getService() {
        return organizationService;
    }

    @Override
    public void detach() {
        //Spring component no need to detach if added into wicket components with @SpringBean
    }
}

