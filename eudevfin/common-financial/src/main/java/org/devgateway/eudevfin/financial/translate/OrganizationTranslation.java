/**
 * 
 */
package org.devgateway.eudevfin.financial.translate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.devgateway.eudevfin.financial.Organization;
import org.hibernate.envers.Audited;

/**
 * @author Alex
 *
 */
@Entity
@Audited
public class OrganizationTranslation extends AbstractTranslation<Organization> implements OrganizationTrnInterface {
	
	private String name;

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
	
	

	
	
}