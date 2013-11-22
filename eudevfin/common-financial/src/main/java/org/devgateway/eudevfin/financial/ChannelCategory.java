/**
 * 
 */
package org.devgateway.eudevfin.financial;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

/**
 * @author Alex
 *
 */
@Entity @Audited
@DiscriminatorValue("Channel")
public class ChannelCategory extends Category {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2465172777390623507L;
	
	@ManyToOne
	Organization organization;
	
	BigDecimal coefficient;
	
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	public BigDecimal getCoefficient() {
		return coefficient;
	}
	public void setCoefficient(BigDecimal coefficient) {
		this.coefficient = coefficient;
	}
		
	
	
}
