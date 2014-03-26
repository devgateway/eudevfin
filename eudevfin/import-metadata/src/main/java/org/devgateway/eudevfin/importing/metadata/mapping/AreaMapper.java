/**
 * 
 */
package org.devgateway.eudevfin.importing.metadata.mapping;

import liquibase.exception.SetupException;

import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
import org.devgateway.eudevfin.importing.metadata.exception.InvalidDataException;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.Country;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Alex
 *
 */
public class AreaMapper extends AbstractMapper<Area> {
	
	@Autowired
	CategoryDaoImpl categDao;
	
	
	
	public AreaMapper() {
		super();
		try {
			this.setUp();
		} catch (final SetupException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Area instantiate() {
		return new Area();
	}
	
	public Area __factory(final String type){
		if ("country".equals(type)) {
			return new Country();
		} else {
			return new Area();
		}
	}
	
	public void __incomeGroup(final Area newArea, final String incomeGroupCode) {
		if (incomeGroupCode != null && incomeGroupCode.length() > 0) {
			final Category incomeGroup	= this.categDao.findByCodeAndClass(incomeGroupCode, Category.class, false).getEntity();
			if ( incomeGroup != null ) {
				newArea.setIncomeGroup(incomeGroup);
			} else {
				throw new InvalidDataException(
						String.format("Found null income group category for code %s for area with code %s", 
								incomeGroupCode, newArea.getCode() )
				);
			}
		}
	}
	
}
