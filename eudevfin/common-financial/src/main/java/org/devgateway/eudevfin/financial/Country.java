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
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Country extends Area {

	private static final long serialVersionUID = 4597228380360940999L;
	
	private BigDecimal GNI;
	private BigDecimal odaOfGNI;
	private BigDecimal totalFlowsOfGNI;
	private Integer population;
	
	public BigDecimal getGNI() {
		return GNI;
	}

	public void setGNI(BigDecimal gDI) {
		GNI = gDI;
	}

	public BigDecimal getOdaOfGNI() {
		return odaOfGNI;
	}

	public void setOdaOfGNI(BigDecimal odaOfGDI) {
		this.odaOfGNI = odaOfGDI;
	}

	public BigDecimal getTotalFlowsOfGNI() {
		return totalFlowsOfGNI;
	}

	public void setTotalFlowsOfGNI(BigDecimal totalFlowsOfGDI) {
		this.totalFlowsOfGNI = totalFlowsOfGDI;
	}

	public Integer getPopulation() {
		return population;
	}

	public void setPopulation(Integer population) {
		this.population = population;
	}


}
