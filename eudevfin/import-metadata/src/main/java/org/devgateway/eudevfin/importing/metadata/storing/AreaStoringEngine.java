/**
 * 
 */
package org.devgateway.eudevfin.importing.metadata.storing;

import org.devgateway.eudevfin.financial.dao.AreaDaoImpl;
import org.devgateway.eudevfin.importing.metadata.mapping.AreaMapper;
import org.devgateway.eudevfin.importing.metadata.mapping.MapperInterface;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alex
 *
 */
@Component
public class AreaStoringEngine extends AbstractStoringEngine<Area> {
	
	@Autowired
	AreaDaoImpl areaDaoImpl;

	@Override
	public Area findEquivalentEntity(final Area entityFromFile) {
		return this.areaDaoImpl.findByCode(entityFromFile.getCode());
	}

	@Override
	public void save(final Area entityFromFile) {
		this.areaDaoImpl.save(entityFromFile);
		
	}
	
	@Override
	public int importHashcode(final Area area) {
		int result = 1;
		
		area.setLocale("en");
		result = HASH_PRIME * result + this.hashcodeFromObject(area.getCode());
		if (area.getIncomeGroup() != null) {
			result = HASH_PRIME * result + this.hashcodeFromObject(area.getIncomeGroup().getCode());
		}
		if ( area.getGeographyCategory() != null ) {
			result = HASH_PRIME * result + this.hashcodeFromObject(area.getGeographyCategory().getCode());
		}
		result = HASH_PRIME * result + this.hashcodeFromObject(area.getName());
		result = HASH_PRIME * result + this.hashcodeFromObject(area.getClass().getName());
		area.setLocale("fr");
		result = HASH_PRIME * result + this.hashcodeFromObject(area.getName());
		
		return result;
	}

	@Override
	public Class<? extends MapperInterface> getRelatedMapper() {
		return AreaMapper.class;
	}

}
