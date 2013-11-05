package org.devgateway.eudevfin.financial.service;

import org.devgateway.eudevfin.financial.Organization;
import org.springframework.integration.annotation.Gateway;

public interface OrganizationService {
	
	@Gateway(requestChannel="createOrganizationChannel",replyChannel="replyCreateOrganizationChannel")
	Organization createOrganization(Organization t);
}
