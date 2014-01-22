/**
 * 
 */
package org.devgateway.eudevfin.financial.translate;

import javax.persistence.Entity;

import org.devgateway.eudevfin.financial.Organization;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

/**
 * @author Alex
 *
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrganizationTranslation extends AbstractTranslation<Organization> implements OrganizationTrnInterface {
	
	private String name;
	
	private String donorName;

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.financial.translate.OrganizationTrnInterface#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.financial.translate.OrganizationTrnInterface#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	public String getDonorName() {
		return donorName;
	}

	public void setDonorName(String donorName) {
		this.donorName = donorName;
	}
	
	

	
	
}
