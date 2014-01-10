package org.devgateway.eudevfin.financial;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.devgateway.eudevfin.financial.translate.OrganizationTranslation;
import org.devgateway.eudevfin.financial.translate.OrganizationTrnInterface;
import org.hibernate.envers.Audited;

@Entity @Audited
public class Organization extends AbstractTranslateable<OrganizationTranslation> 
							implements OrganizationTrnInterface {

	
	private String code;
	
	private String acronym;
	
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
		return String.format("Org name is %s, id %d",  this.getName(), this.id);
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

}
