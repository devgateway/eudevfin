package org.devgateway.eudevfin.financial.service;

import org.devgateway.eudevfin.financial.Organization;
import org.springframework.integration.annotation.Gateway;
import org.springframework.stereotype.Component;

@Component
public interface OrganizationService extends BaseEntityService<Organization> {
}
