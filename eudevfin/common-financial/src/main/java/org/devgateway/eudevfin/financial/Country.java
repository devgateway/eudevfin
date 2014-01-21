/**
 * 
 */
package org.devgateway.eudevfin.financial;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

/**
 * @author Alex
 *
 */
@Entity
@Audited
@DiscriminatorValue("Country")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Country extends Area {

	private static final long serialVersionUID = 4597228380360940999L;
	
	private BigDecimal GDI;
	private BigDecimal odaOfGDI;
	private BigDecimal totalFlowsOfGDI;
	private Integer population;
	
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

	public Integer getPopulation() {
		return population;
	}

	public void setPopulation(Integer population) {
		this.population = population;
	}


}
