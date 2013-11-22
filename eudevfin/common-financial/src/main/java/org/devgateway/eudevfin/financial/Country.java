package org.devgateway.eudevfin.financial;

import java.math.BigDecimal;

import javax.persistence.Entity;

import org.devgateway.eudevfin.financial.translate.CountryTranslation;
import org.devgateway.eudevfin.financial.translate.CountryTrnInterface;
import org.hibernate.envers.Audited;


@Entity
@Audited
public class Country extends AbstractTranslateable<CountryTranslation> 
					implements CountryTrnInterface {
	
	
	private String code;
	
	private BigDecimal GDI;
	private BigDecimal odaOfGDI;
	private BigDecimal totalFlowsOfGDI;
	private BigDecimal population;
	
	@Override
	protected CountryTranslation newTranslationInstance() {
		return new CountryTranslation();
	}
	
	@Override
	public String getName() {
		return (String) get("name");
	}

	@Override
	public void setName(String name) {
		set("name",name);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getGDI() {
		return GDI;
	}

	public void setGDI(BigDecimal gDI) {
		GDI = gDI;
	}

	public BigDecimal getOdaOfGDI() {
		return odaOfGDI;
	}

	public void setOdaOfGDI(BigDecimal odaOfGDI) {
		this.odaOfGDI = odaOfGDI;
	}

	public BigDecimal getTotalFlowsOfGDI() {
		return totalFlowsOfGDI;
	}

	public void setTotalFlowsOfGDI(BigDecimal totalFlowsOfGDI) {
		this.totalFlowsOfGDI = totalFlowsOfGDI;
	}

	public BigDecimal getPopulation() {
		return population;
	}

	public void setPopulation(BigDecimal population) {
		this.population = population;
	}

}
