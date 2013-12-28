/**
 * 
 */
package org.devgateway.eudevfin.financial.translate;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.devgateway.eudevfin.financial.Area;
import org.hibernate.envers.Audited;

/**
 * @author Alex
 *
 */

@Entity
@Audited
public class AreaTranslation extends AbstractTranslation<Area> implements CountryTrnInterface {
	
	private String name;
	
	/**
	 * Africa (North of Sahara), Africa (South of Sahara), Europe, etc
	 */
	private String geography;

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

	public String getGeography() {
		return geography;
	}

	public void setGeography(String geography) {
		this.geography = geography;
	}
	
	

}
