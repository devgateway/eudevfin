/**
 * 
 */
package org.devgateway.eudevfin.metadata.common.domain.translation;

import javax.persistence.Entity;

import org.devgateway.eudevfin.common.dao.translation.AbstractTranslation;
import org.devgateway.eudevfin.metadata.common.domain.Area;
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
public class AreaTranslation extends AbstractTranslation<Area> implements AreaTrnInterface {
	
	private String name;
	
	/**
	 * Africa (North of Sahara), Africa (South of Sahara), Europe, etc
	 */
	private String geography;

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.financial.translate.AreaTrnInterface#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.financial.translate.AreaTrnInterface#setName(java.lang.String)
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
