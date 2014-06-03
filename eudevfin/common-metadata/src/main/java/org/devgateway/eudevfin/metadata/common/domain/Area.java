package org.devgateway.eudevfin.metadata.common.domain;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import org.devgateway.eudevfin.common.dao.translation.AbstractTranslateable;
import org.devgateway.eudevfin.metadata.common.domain.translation.AreaTranslation;
import org.devgateway.eudevfin.metadata.common.domain.translation.AreaTrnInterface;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;


@Entity
@Audited
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name="area_type",
    discriminatorType= DiscriminatorType.STRING)
@DiscriminatorValue("Area")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Area extends AbstractTranslateable<AreaTranslation> 
					implements AreaTrnInterface {
	
	private static final long serialVersionUID = 4477382814048134799L;

	private String code;
	
	private String iso;
	
	@ManyToOne
	private Category geographyCategory;
	
	/**
	 * This is a category since it needs both a code and a translateable description of it
	 */
	@ManyToOne
	private Category incomeGroup;
	
	@Override
	protected AreaTranslation newTranslationInstance() {
		return new AreaTranslation();
	}
	
	@Override
	public String getName() {
		return (String) this.get("name");
	}

	@Override
	public void setName(final String name) {
		this.set("name",name);
	}
	

	public String getCode() {
		return this.code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public Category getIncomeGroup() {
		return this.incomeGroup;
	}

	public void setIncomeGroup(final Category incomeGroup) {
		this.incomeGroup = incomeGroup;
	}
	/**
	 * @return the geographyCategory
	 */
	public Category getGeographyCategory() {
		return this.geographyCategory;
	}

	/**
	 * @param geographyCategory the geographyCategory to set
	 */
	public void setGeographyCategory(final Category geographyCategory) {
		this.geographyCategory = geographyCategory;
	}

	

	@Override
	public String toString() {
		return getCode()+" - "+getName();
	}

}
