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
	private Organization organization;
	
	private BigDecimal coefficient;
	
	private String acronym;
	private Integer dac2a3a;
	private Boolean mcd;
	
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
	public String getAcronym() {
		return acronym;
	}
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}
	public Integer getDac2a3a() {
		return dac2a3a;
	}
	public void setDac2a3a(Integer dac2a3a) {
		this.dac2a3a = dac2a3a;
	}
	public Boolean getMcd() {
		return mcd;
	}
	public void setMcd(Boolean mcd) {
		this.mcd = mcd;
	}
		
	
	
	
}
