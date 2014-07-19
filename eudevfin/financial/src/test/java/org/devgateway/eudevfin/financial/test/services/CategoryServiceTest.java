/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
/**
 * 
 */
package org.devgateway.eudevfin.financial.test.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;

import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.SectorCategory;
import org.devgateway.eudevfin.metadata.common.service.CategoryService;
import org.hibernate.LazyInitializationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alex
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/financialContext.xml", 
		"classpath:/META-INF/commonContext.xml", "classpath:/META-INF/commonMetadataContext.xml",
		"classpath:META-INF/commonFinancialContext.xml","classpath:testFinancialContext.xml" })
@Component 
//@TransactionConfiguration(defaultRollback=false, transactionManager="transactionManager")
@Transactional
public class CategoryServiceTest {

	public static final String SECTORS_ROOT_TEST = "sectors_root_test";

	public static final String SUBSECTORS_LABEL_TEST = "subsectors_label_test";

	public static final String SECTORS_LABEL_TEST = "sectors_label_test";
	
	@Autowired
	CategoryService categoryService;
	
//	@Resource(name="si.defaultReplyTimeout")
	@Value("${si.defaultReplyTimeout}")
	String defaultReplyTimeout;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	
	@Test
	public void testSave() {
		final Category c 	= new Category();
		c.setName("Test parent category");
		c.setCode("TPC-test");
		c.setChildren(new HashSet<Category>());
		
		final Category c1 = new Category();
		c1.setName("First child");
		c1.setCode("FC-test");
		c.getChildren().add(c1);
		
		final Category c2 = new Category();
		c2.setName("Second child");
		c2.setCode("SC-test");
		c.getChildren().add(c2);
		
		final Category result 	= this.categoryService.save(c).getEntity();
		assertNotNull(result.getChildren());
		assertEquals(result.getChildren().size(), 2);
		
	}
	
	@Test
	public void testFindByLabelCode() {
		this.createLabels();
		
		this.createFakeSectors();
		
		final List<Category> allSectors				= this.categoryService.findByTagsCode(SECTORS_LABEL_TEST);
		assertEquals(allSectors.size(), 3);
		
		final List<Category> allSubSectors			= this.categoryService.findByTagsCode(SUBSECTORS_LABEL_TEST);
		assertEquals(allSubSectors.size(), 2);
		
		final Category rootSectorsInitialized			= this.categoryService.findByCodeAndClass(SECTORS_ROOT_TEST, 
													SectorCategory.class,true).getEntity();
		assertNotNull(rootSectorsInitialized);
		assertNotNull(rootSectorsInitialized.getChildren());
		assertNotNull(rootSectorsInitialized.getChildren().iterator().next());
		assertNotNull(rootSectorsInitialized.getTags().iterator().next());
		
		final Category rootSectorsNonInitialized		= this.categoryService.findByCodeAndClass(SECTORS_ROOT_TEST, 
													SectorCategory.class, false).getEntity();
		try {
			rootSectorsNonInitialized.getChildren().iterator().next();
		}
		catch(final LazyInitializationException e){
			assertTrue("Chilren not fetched by hibernate", true);
		}
		try {
			rootSectorsNonInitialized.getTags().iterator().next();
		}
		catch(final LazyInitializationException e){
			assertTrue("Tags not fetched by hibernate", true);
		}
		
		
	}
	
	@Test
	public void testFindByGeneralSearchAndTagsCode() {
		
		final String testTagCode			= "Test Tag Category For General Search";
		
		final Category testTagCategory	= new Category();
		testTagCategory.setName("Test Tag Category For General Search");
		testTagCategory.setCode(testTagCode);
		this.categoryService.save(testTagCategory);
		
		final Category testCategory1 = new Category();
		testCategory1.setLocale("en");
		testCategory1.setName("Test category for general search: x1234");
		testCategory1.setCode("Test category for general search1");
		testCategory1.setTags(new HashSet<Category>());
		testCategory1.getTags().add(testTagCategory);
		
		this.categoryService.save(testCategory1);
		
		final Category testCategory2 = new Category();
		testCategory2.setLocale("xx");
		testCategory2.setName("Test category for general search: x1234");
		testCategory2.setCode("Test category for general search2");
		testCategory2.setTags(new HashSet<Category>());
		testCategory2.getTags().add(testTagCategory);
		
		this.categoryService.save(testCategory2);
		
		final List<Category> responseList	
				= this.categoryService.findByGeneralSearchAndTagsCode("xx", "x1234", testTagCode, true);
		
		assertTrue(responseList.size()  == 1);
		
	}

	@Transactional
	private void createFakeSectors() {
		final Category sectorLabel	= 
				this.categoryService.findByCodeAndClass(SECTORS_LABEL_TEST, Category.class, false).getEntity();
		final Category subSectorLabel	= 
				this.categoryService.findByCodeAndClass(SUBSECTORS_LABEL_TEST, Category.class, false).getEntity();
		
		final Category sectorsRoot = new SectorCategory();
		sectorsRoot.setName("Sectors Root");
		sectorsRoot.setCode(SECTORS_ROOT_TEST);
		sectorsRoot.setTags(new HashSet<Category>());
		sectorsRoot.getTags().add(sectorLabel);
		
		final Category sector1	= new SectorCategory();
		sector1.setName("Sector 1");
		sector1.setCode("sector_1_test");
		sector1.setParentCategory(sectorsRoot);
		sector1.setTags(new HashSet<Category>());
		sector1.getTags().add(sectorLabel);
		sector1.getTags().add(subSectorLabel);
		
		final Category sector2	= new SectorCategory();
		sector2.setName("Sector 2");
		sector2.setCode("sector_2_test");
		sector2.setParentCategory(sectorsRoot);
		sector2.setTags(new HashSet<Category>());
		sector2.getTags().add(sectorLabel);
		sector2.getTags().add(subSectorLabel);
		
		this.categoryService.save(sectorsRoot);
	}


	/**
	 * creates some fake labels for the test
	 */
	@Transactional
	private void createLabels() {
		final Category labelRoot	= new Category();
		labelRoot.setName("Label Root - test");
		labelRoot.setCode("label_root_test");
		
		final Category sectors = new Category();
		sectors.setName("Sectors Label - test");
		sectors.setCode(SECTORS_LABEL_TEST);
		sectors.setParentCategory(labelRoot);
		
		final Category subsectors = new Category();
		subsectors.setName("Sub-Sectors Label - test");
		subsectors.setCode(SUBSECTORS_LABEL_TEST);
		subsectors.setParentCategory(sectors);
		
		final Category fakeLabel = new Category();
		fakeLabel.setName("Fake label - test");
		fakeLabel.setCode("fake_label_test"); 
		fakeLabel.setParentCategory(labelRoot);
		
		this.categoryService.save(labelRoot);
	}

}
