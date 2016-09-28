/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.metadata.common.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.devgateway.eudevfin.common.dao.translation.AbstractTranslateable;
import org.devgateway.eudevfin.metadata.common.domain.translation.OrganizationTranslation;
import org.devgateway.eudevfin.metadata.common.domain.translation.OrganizationTrnInterface;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

@Entity @Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Organization extends AbstractTranslateable<OrganizationTranslation>
							implements OrganizationTrnInterface {

	private static final long serialVersionUID = -7886444652559589256L;

	private String code;

	@Index(name="organization_acronym_idx")
	private String acronym;

	private Boolean dac;

	@ManyToOne
	private Area area;

	@ManyToOne
	private Category organizationType;

	/**
	 * This is the code of the parent Donor (group/country)
	 */
	private String donorCode;

	@Override
	public String getName() {
		return (String) this.get("name");
	}

	@Override
	public void setName(String name) {
		this.set("name", name);
	}

	@Override
	public String getDonorName() {
		return (String) get("donorName");
	}

	@Override
	public void setDonorName(String donorName) {
		this.set("donorName", donorName);

	}


	@Override
	public String toString() {
		return String.format("%d - %s",  this.id, this.getName());
	}

	@Override
	protected OrganizationTranslation newTranslationInstance() {
		return new OrganizationTranslation();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public Category getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(Category organizationType) {
		this.organizationType = organizationType;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getDonorCode() {
		return donorCode;
	}

	public void setDonorCode(String donorCode) {
		this.donorCode = donorCode;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	/**
	 * @return the dac
	 */
	public Boolean getDac() {
		return dac;
	}

	/**
	 * @param dac the dac to set
	 */
	public void setDac(Boolean dac) {
		this.dac = dac;
	}


}
