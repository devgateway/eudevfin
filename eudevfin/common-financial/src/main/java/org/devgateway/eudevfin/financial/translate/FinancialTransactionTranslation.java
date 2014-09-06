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
package org.devgateway.eudevfin.financial.translate;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.devgateway.eudevfin.common.dao.translation.AbstractTranslation;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

/**
 * @author Alex
 *
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FinancialTransactionTranslation extends AbstractTranslation<FinancialTransaction> implements FinancialTransactionTrnInterface {

	private static final long serialVersionUID = -657182398545780927L;

	@Column(length=20000)
	@Index(name="FinancialTransactionTranslation_description_idx")
	private String description;


	@Column(length=150)
	@Index(name="FinancialTransactionTranslation_channelInstitutionName_idx")
	private String channelInstitutionName;

	@Index(name="FinancialTransactionTranslation_shortdescription_idx")
	private String shortDescription;		
    
	@Column(length=1024)
    private String firstCoFinancingAgency;
    
	@Column(length=1024)
    private String secondCoFinancingAgency;
    
	@Column(length=1024)
    private String thirdCoFinancingAgency;
	
	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.financial.translate.FinancialTransactionTrnInterface#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.financial.translate.FinancialTransactionTrnInterface#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getShortDescription() {
		return shortDescription;
	}

	@Override
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * @return the firstCoFinancingAgency
	 */
	public String getFirstCoFinancingAgency() {
		return firstCoFinancingAgency;
	}

	/**
	 * @param firstCoFinancingAgency the firstCoFinancingAgency to set
	 */
	public void setFirstCoFinancingAgency(String firstCoFinancingAgency) {
		this.firstCoFinancingAgency = firstCoFinancingAgency;
	}

	/**
	 * @return the secondCoFinancingAgency
	 */
	public String getSecondCoFinancingAgency() {
		return secondCoFinancingAgency;
	}

	/**
	 * @param secondCoFinancingAgency the secondCoFinancingAgency to set
	 */
	public void setSecondCoFinancingAgency(String secondCoFinancingAgency) {
		this.secondCoFinancingAgency = secondCoFinancingAgency;
	}

	/**
	 * @return the thirdCoFinancingAgency
	 */
	public String getThirdCoFinancingAgency() {
		return thirdCoFinancingAgency;
	}

	/**
	 * @param thirdCoFinancingAgency the thirdCoFinancingAgency to set
	 */
	public void setThirdCoFinancingAgency(String thirdCoFinancingAgency) {
		this.thirdCoFinancingAgency = thirdCoFinancingAgency;
	}
	

	public String getChannelInstitutionName() {
		return channelInstitutionName;
	}

	public void setChannelInstitutionName(String channelInstitutionName) {
		this.channelInstitutionName = channelInstitutionName;
	}	
	
}
