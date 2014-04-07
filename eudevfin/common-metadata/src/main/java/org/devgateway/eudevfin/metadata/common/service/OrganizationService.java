package org.devgateway.eudevfin.metadata.common.service;

import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public interface OrganizationService extends BaseEntityService<Organization> {
    public Page<Organization> findUsedOrganizationPaginated(
            @Header("locale") String locale, String searchString,
            @Header("pageable") Pageable page);
}
