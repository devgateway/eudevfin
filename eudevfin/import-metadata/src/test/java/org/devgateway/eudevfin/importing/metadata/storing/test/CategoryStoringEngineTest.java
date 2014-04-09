/**
 * 
 */
package org.devgateway.eudevfin.importing.metadata.storing.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
import org.devgateway.eudevfin.importing.metadata.storing.CategoryStoringEngine;
import org.devgateway.eudevfin.importing.metadata.test.helper.DbHelper;
import org.devgateway.eudevfin.importing.metadata.test.helper.DbHelper.PossibleCategClass;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class CategoryStoringEngineTest {
	
	@Autowired
	CategoryDaoImpl categoryDaoImpl;
	
	@Autowired
	CategoryDaoImpl categDaoImpl;
	
	@Autowired
	DbHelper dbHelper;
	
	@Autowired
	@Qualifier("categoryStoringEngine")
	CategoryStoringEngine categStoringEngine;
	
	
	/**
	 * Test method for {@link org.devgateway.eudevfin.importing.metadata.storing.AreaStoringEngine#findEquivalentEntity(org.devgateway.eudevfin.metadata.common.domain.Area)}.
	 */
	@Test
	@Transactional
	public void test() {
		final Category parent1	= this.dbHelper.createCategory("testParentCategoryCode1", "Test Parent Category Name 1", null, PossibleCategClass.CATEGORY, (String [])null);
		final Category tag1		= this.dbHelper.createCategory("testTagCategoryCode1", "Test Tag Category Name 1", null, PossibleCategClass.CATEGORY, (String [])null);
		
		final Category c1	= this.dbHelper.createCategory("testCategoryCode1", "Test Category Name 1", "testParentCategoryCode1", PossibleCategClass.CATEGORY, "testTagCategoryCode1");
		final Category c2	= new ChannelCategory();
		c2.setCode(c1.getCode());
		
		final Category c2FromDb	= this.categStoringEngine.findEquivalentEntity(c2);
		assertNull(c2FromDb);
		
		final Category c1Clone	= new Category();
		c1Clone.setCode(c1.getCode());
		final Category c1FromDb	= this.categStoringEngine.findEquivalentEntity(c1Clone);
		assertNotNull(c1FromDb);
		
		assertTrue( this.categStoringEngine.checkSame(c1, c1FromDb) );
		assertFalse( this.categStoringEngine.checkSame(c1, c1Clone) );
		
		c1Clone.setName(c1.getName());
		
		final Category parentClone	= new Category();
		parentClone.setCode("testParentCategoryCode1");
		c1Clone.setParentCategory(parentClone);
		
		final Category tagClone	= new Category();
		tagClone.setCode("testTagCategoryCode1");
		
		assertFalse( this.categStoringEngine.checkSame(c1, c1Clone) );
		
		c1Clone.setTags(new HashSet<Category>());
		c1Clone.getTags().add(tagClone);
		
		
		assertTrue( this.categStoringEngine.checkSame(c1, c1Clone) );
		
		this.categDaoImpl.delete(c1);
		this.categDaoImpl.delete(tag1);
		this.categDaoImpl.delete(parent1);
		
	}

}
