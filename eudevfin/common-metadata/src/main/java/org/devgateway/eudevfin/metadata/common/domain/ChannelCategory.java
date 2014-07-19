/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
/**
 * 
 */
package org.devgateway.eudevfin.metadata.common.domain;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.devgateway.eudevfin.metadata.common.domain.translation.ChannelCategoryTranslation;
import org.devgateway.eudevfin.metadata.common.domain.translation.ChannelCategoryTrnInterface;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

/**
 * @author Alex
 *
 */
@Entity @Audited
@DiscriminatorValue("Channel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
