/**
 * 
 */
package org.devgateway.eudevfin.financial;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

/**
 * @author Alex
 *
 */
@Entity
@Audited
@DiscriminatorValue("Recipient")
public class RecipientCategory extends Category {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7973525314051632466L;
	
	@ManyToOne
	Country country;

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
	
}
