/**
 * 
 */
package org.devgateway.eudevfin.importing.metadata.storing.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashSet;

import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
import org.devgateway.eudevfin.importing.metadata.storing.ChannelCategoryStoringEngine;
import org.devgateway.eudevfin.importing.metadata.test.helper.DbHelper;
import org.devgateway.eudevfin.importing.metadata.test.helper.DbHelper.PossibleCategClass;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
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
public class ChannelCategoryStoringEngineTest {
	
	@Autowired
	CategoryDaoImpl categoryDaoImpl;
	
	@Autowired
	CategoryDaoImpl categDaoImpl;
	
	@Autowired
	DbHelper dbHelper;
	
	@Autowired
	ChannelCategoryStoringEngine channelCategStoringEngine;
	
	
	/**
	 * Test method for {@link org.devgateway.eudevfin.importing.metadata.storing.AreaStoringEngine#findEquivalentEntity(org.devgateway.eudevfin.metadata.common.domain.Area)}.
	 */
	@Test
	@Transactional
	public void test() {
		final ChannelCategory parent1	= (ChannelCategory) this.dbHelper.createCategory("testParentCategoryCode1", "Test Parent Category Name 1", 
				null, PossibleCategClass.CHANNELCATEGORY, (String [])null);
		final Category tag1		= this.dbHelper.createCategory("testTagCategoryCode1", "Test Tag Category Name 1",
				null, PossibleCategClass.CATEGORY, (String [])null);
		
		final ChannelCategory c1	= (ChannelCategory) this.dbHelper.createCategory("testCategoryCode1", "Test Category Name 1", 
				"testParentCategoryCode1", PossibleCategClass.CHANNELCATEGORY, "testTagCategoryCode1");
		c1.setAcronym("Test Acronym 1");
		c1.setCoefficient(new BigDecimal(1));
		c1.setDac2a3a(1);
		c1.setMcd(true);
		
		final Category c2	= new Category();
		c2.setCode(c1.getCode());
		
		final Category c2FromDb	= this.channelCategStoringEngine.findEquivalentEntity(c2);
		assertNull(c2FromDb);
		
		final ChannelCategory c1Clone	= new ChannelCategory();
		c1Clone.setCode(c1.getCode());
		final Category c1FromDb	= this.channelCategStoringEngine.findEquivalentEntity(c1Clone);
		assertNotNull(c1FromDb);
		
		assertTrue( this.channelCategStoringEngine.checkSame(c1, c1FromDb) );
		assertFalse( this.channelCategStoringEngine.checkSame(c1, c1Clone) );
		
		c1Clone.setName(c1.getName());
		
		final Category parentClone	= new Category();
		parentClone.setCode("testParentCategoryCode1");
		c1Clone.setParentCategory(parentClone);
		
		final Category tagClone	= new Category();
		tagClone.setCode("testTagCategoryCode1");
		
		assertFalse( this.channelCategStoringEngine.checkSame(c1, c1Clone) );
		
		c1Clone.setTags(new HashSet<Category>());
		c1Clone.getTags().add(tagClone);
		
		c1Clone.setMcd(true);
		c1Clone.setDac2a3a(1);
		c1Clone.setCoefficient(new BigDecimal(1));
		c1Clone.setAcronym(c1.getAcronym());
		
		assertTrue( this.channelCategStoringEngine.checkSame(c1, c1Clone) );
		
		this.categDaoImpl.delete(c1);
		this.categDaoImpl.delete(tag1);
		this.categDaoImpl.delete(parent1);
		
	}

}
