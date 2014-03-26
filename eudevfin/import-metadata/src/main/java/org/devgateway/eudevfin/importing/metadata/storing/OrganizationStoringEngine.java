/**
 * 
 */
package org.devgateway.eudevfin.importing.metadata.storing;

import org.devgateway.eudevfin.financial.dao.OrganizationDaoImpl;
import org.devgateway.eudevfin.importing.metadata.mapping.MapperInterface;
import org.devgateway.eudevfin.importing.metadata.mapping.OrganizationMapper;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alex
 *
 */
@Component
public class OrganizationStoringEngine extends AbstractStoringEngine<Organization> {
	
	@Autowired
	OrganizationDaoImpl orgDaoImpl;

	@Override
	public Organization findEquivalentEntity(final Organization entityFromFile) {
		return this.orgDaoImpl.findByCodeAndDonorCode(entityFromFile.getCode(), entityFromFile.getDonorCode());
	}

	@Override
	public void save(final Organization entityFromFile) {
		this.orgDaoImpl.save(entityFromFile);
		
	}

	@Override
	public int importHashcode(final Organization org) {
		int result = 1;
		org.setLocale("en");
		result = HASH_PRIME * result + this.hashcodeFromObject(org.getDonorCode());
		result = HASH_PRIME * result + this.hashcodeFromObject(org.getDonorName());
		result = HASH_PRIME * result + this.hashcodeFromObject(org.getCode());
		result = HASH_PRIME * result + this.hashcodeFromObject(org.getName());
		result = HASH_PRIME * result + this.hashcodeFromObject(org.getAcronym());
		org.setLocale("fr");
		result = HASH_PRIME * result + this.hashcodeFromObject(org.getDonorName());
		result = HASH_PRIME * result + this.hashcodeFromObject(org.getName());
		return result;
	}

	@Override
	public Class<? extends MapperInterface> getRelatedMapper() {
		return OrganizationMapper.class;
	}

}
