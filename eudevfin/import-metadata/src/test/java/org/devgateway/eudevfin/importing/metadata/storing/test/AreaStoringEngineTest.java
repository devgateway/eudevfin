/**
 * 
 */
package org.devgateway.eudevfin.importing.metadata.storing.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.devgateway.eudevfin.financial.dao.AreaDaoImpl;
import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
import org.devgateway.eudevfin.importing.metadata.storing.AreaStoringEngine;
import org.devgateway.eudevfin.importing.metadata.test.helper.DbHelper;
import org.devgateway.eudevfin.importing.metadata.test.helper.DbHelper.PossibleCategClass;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alex
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/commonContext.xml",
		"classpath:/META-INF/financialContext.xml",
		"classpath:META-INF/commonFinancialContext.xml","classpath:testImportMetadataContext.xml" })
@Component  
public class AreaStoringEngineTest {
	
	@Autowired
	CategoryDaoImpl categoryDaoImpl;
	
	@Autowired
	AreaDaoImpl areaDaoImpl;
	
	@Autowired
	DbHelper dbHelper;
	
	@Autowired
	AreaStoringEngine areaStoringEngine;
	
	private Area createArea(final int index) {
		final Area area	= new Area();
		
		area.setCode("testAreaCode" + index);
		area.setName("Test Area " + index);
		
		final Category incGroup	= 
				this.dbHelper.createCategory("incomeGroup" + index, "Income Group " + index, null,
						PossibleCategClass.CATEGORY, (String[])null);
		area.setIncomeGroup(incGroup);
		
		
		final Category geographyCategory	= 
				this.dbHelper.createCategory("geographyCategory" + index, "Geography Category " + index, null,
						PossibleCategClass.CATEGORY, (String[]) null);
		area.setGeographyCategory(geographyCategory);
		
		return this.areaDaoImpl.save(area).getEntity();
	}
	
	private void cleanDb() {
		final List<Area> areas	= this.areaDaoImpl.findAllAsList();
		if ( areas != null ) {
			for (final Area area : areas) {
				final Category geography		= area.getGeographyCategory();
				final Category incomeGroup	= area.getIncomeGroup();
				area.setGeographyCategory(null);
				area.setIncomeGroup(null);
				this.areaDaoImpl.delete(area);;
				this.categoryDaoImpl.delete(geography);
				this.categoryDaoImpl.delete(incomeGroup);
			}
		}
	}
	
	/**
	 * Test method for {@link org.devgateway.eudevfin.importing.metadata.storing.AreaStoringEngine#findEquivalentEntity(org.devgateway.eudevfin.metadata.common.domain.Area)}.
	 */
	@Test
	@Transactional
	public void test() {
		final Area a1	= this.createArea(1);
		final Area a2	= new Area();
		a2.setCode("a2Code");
		
		final Area a2FromDb	= this.areaStoringEngine.findEquivalentEntity(a2);
		assertNull(a2FromDb);
		
		final Area a1Clone	= new Area();
		a1Clone.setCode(a1.getCode());
		final Area a1FromDb	= this.areaStoringEngine.findEquivalentEntity(a1Clone);
		assertNotNull(a1FromDb);
		
		assertTrue( this.areaStoringEngine.checkSame(a1, a1FromDb) );
		assertFalse( this.areaStoringEngine.checkSame(a1, a1Clone) );
		
		a1Clone.setName(a1.getName());
		
		final Category geographyCategory	= new Category();
		geographyCategory.setCode(a1.getGeographyCategory().getCode());
		a1Clone.setGeographyCategory(geographyCategory);
		
		final Category incomeGroupCategory		= new Category();
		incomeGroupCategory.setCode(a1.getIncomeGroup().getCode());
		a1Clone.setIncomeGroup(incomeGroupCategory);
		
		
		assertTrue( this.areaStoringEngine.checkSame(a1, a1Clone) );
		
		this.cleanDb();
		
	}

}
