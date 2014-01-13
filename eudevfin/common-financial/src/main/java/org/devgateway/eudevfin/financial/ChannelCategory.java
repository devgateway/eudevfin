/**
 * 
 */
package org.devgateway.eudevfin.financial;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.devgateway.eudevfin.financial.translate.CategoryTranslation;
import org.devgateway.eudevfin.financial.translate.ChannelCategoryTranslation;
import org.devgateway.eudevfin.financial.translate.ChannelCategoryTrnInterface;
import org.hibernate.envers.Audited;

/**
 * @author Alex
 *
 */
@Entity @Audited
@DiscriminatorValue("Channel")
public class ChannelCategory extends Category implements ChannelCategoryTrnInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2465172777390623507L;
	
	@ManyToOne
	private Organization organization;
	
	private BigDecimal coefficient;
	
	private Integer dac2a3a;
	private Boolean mcd;
	
	
	
	@Override
	protected ChannelCategoryTranslation newTranslationInstance() {
		return new ChannelCategoryTranslation();
	}
	@Override
	public String getAcronym() {
		return (String)this.get("acronym");
	}
	@Override
	public void setAcronym(String acronym) {
		this.set("acronym",acronym);
	}
	
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
