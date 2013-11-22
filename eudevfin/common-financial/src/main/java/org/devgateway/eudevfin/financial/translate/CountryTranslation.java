/**
 * 
 */
package org.devgateway.eudevfin.financial.translate;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.devgateway.eudevfin.financial.Country;
import org.hibernate.envers.Audited;

/**
 * @author Alex
 *
 */

@Entity
@Audited
public class CountryTranslation extends AbstractTranslation<Country> implements CountryTrnInterface {
	
	private String name;

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.financial.translate.CountryTrnInterface#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.financial.translate.CountryTrnInterface#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	

}
