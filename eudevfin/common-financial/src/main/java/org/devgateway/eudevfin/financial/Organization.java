package org.devgateway.eudevfin.financial;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.devgateway.eudevfin.financial.translate.OrganizationTranslation;
import org.devgateway.eudevfin.financial.translate.OrganizationTrnInterface;
import org.hibernate.envers.Audited;

@Entity @Audited
public class Organization extends AbstractTranslateable<OrganizationTranslation> 
							implements OrganizationTrnInterface {

	
	private String code;
	
	@ManyToOne
	private Country country;
	
	@ManyToOne
	private Category organizationType; 
	
	@Override
	public String getName() {
		return (String) this.get("name");
	}

	@Override
	public void setName(String name) {
		this.set("name", name);
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

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
	
}
