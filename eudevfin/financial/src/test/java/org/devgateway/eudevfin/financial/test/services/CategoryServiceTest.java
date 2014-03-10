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
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alex
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/financialContext.xml", "classpath:/META-INF/commonContext.xml",
		"classpath:META-INF/commonFinancialContext.xml","classpath:testFinancialContext.xml" })
@Component 
@TransactionConfiguration(defaultRollback=false, transactionManager="transactionManager")
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
		Category c 	= new Category();
		c.setName("Test parent category");
		c.setCode("TPC-test");
		c.setChildren(new HashSet<Category>());
		
		Category c1 = new Category();
		c1.setName("First child");
		c1.setCode("FC-test");
		c.getChildren().add(c1);
		
		Category c2 = new Category();
		c2.setName("Second child");
		c2.setCode("SC-test");
		c.getChildren().add(c2);
		
		Category result 	= categoryService.save(c).getEntity();
		assertNotNull(result.getChildren());
		assertEquals(result.getChildren().size(), 2);
		
	}
	
	@Test
	public void testFindByLabelCode() {
		createLabels();
		
		createFakeSectors();
		
		List<Category> allSectors				= categoryService.findByTagsCode(SECTORS_LABEL_TEST);
		assertEquals(allSectors.size(), 3);
		
		List<Category> allSubSectors			= categoryService.findByTagsCode(SUBSECTORS_LABEL_TEST);
		assertEquals(allSubSectors.size(), 2);
		
		Category rootSectorsInitialized			= categoryService.findByCodeAndClass(SECTORS_ROOT_TEST, 
													SectorCategory.class,true).getEntity();
		assertNotNull(rootSectorsInitialized);
		assertNotNull(rootSectorsInitialized.getChildren());
		assertNotNull(rootSectorsInitialized.getChildren().iterator().next());
		assertNotNull(rootSectorsInitialized.getTags().iterator().next());
		
		Category rootSectorsNonInitialized		= categoryService.findByCodeAndClass(SECTORS_ROOT_TEST, 
													SectorCategory.class, false).getEntity();
		try {
			rootSectorsNonInitialized.getChildren().iterator().next();
		}
		catch(LazyInitializationException e){
			assertTrue("Chilren not fetched by hibernate", true);
		}
		try {
			rootSectorsNonInitialized.getTags().iterator().next();
		}
		catch(LazyInitializationException e){
			assertTrue("Tags not fetched by hibernate", true);
		}
		
		
	}
	
	@Test
	public void testFindByGeneralSearchAndTagsCode() {
		
		String testTagCode			= "Test Tag Category For General Search";
		
		Category testTagCategory	= new Category();
		testTagCategory.setName("Test Tag Category For General Search");
		testTagCategory.setCode(testTagCode);
		categoryService.save(testTagCategory);
		
		Category testCategory1 = new Category();
		testCategory1.setLocale("en");
		testCategory1.setName("Test category for general search: x1234");
		testCategory1.setCode("Test category for general search1");
		testCategory1.setTags(new HashSet<Category>());
		testCategory1.getTags().add(testTagCategory);
		
		categoryService.save(testCategory1);
		
		Category testCategory2 = new Category();
		testCategory2.setLocale("xx");
		testCategory2.setName("Test category for general search: x1234");
		testCategory2.setCode("Test category for general search2");
		testCategory2.setTags(new HashSet<Category>());
		testCategory2.getTags().add(testTagCategory);
		
		categoryService.save(testCategory2);
		
		List<Category> responseList	
				= categoryService.findByGeneralSearchAndTagsCode("xx", "x1234", testTagCode, true);
		
		assertTrue(responseList.size()  == 1);
		
	}

	@Transactional
	private void createFakeSectors() {
		Category sectorLabel	= 
				categoryService.findByCodeAndClass(SECTORS_LABEL_TEST, Category.class, false).getEntity();
		Category subSectorLabel	= 
				categoryService.findByCodeAndClass(SUBSECTORS_LABEL_TEST, Category.class, false).getEntity();
		
		Category sectorsRoot = new SectorCategory();
		sectorsRoot.setName("Sectors Root");
		sectorsRoot.setCode(SECTORS_ROOT_TEST);
		sectorsRoot.setTags(new HashSet<Category>());
		sectorsRoot.getTags().add(sectorLabel);
		
		Category sector1	= new SectorCategory();
		sector1.setName("Sector 1");
		sector1.setCode("sector_1_test");
		sector1.setParentCategory(sectorsRoot);
		sector1.setTags(new HashSet<Category>());
		sector1.getTags().add(sectorLabel);
		sector1.getTags().add(subSectorLabel);
		
		Category sector2	= new SectorCategory();
		sector2.setName("Sector 2");
		sector2.setCode("sector_2_test");
		sector2.setParentCategory(sectorsRoot);
		sector2.setTags(new HashSet<Category>());
		sector2.getTags().add(sectorLabel);
		sector2.getTags().add(subSectorLabel);
		
		categoryService.save(sectorsRoot);
	}


	/**
	 * creates some fake labels for the test
	 */
	@Transactional
	private void createLabels() {
		Category labelRoot	= new Category();
		labelRoot.setName("Label Root - test");
		labelRoot.setCode("label_root_test");
		
		Category sectors = new Category();
		sectors.setName("Sectors Label - test");
		sectors.setCode(SECTORS_LABEL_TEST);
		sectors.setParentCategory(labelRoot);
		
		Category subsectors = new Category();
		subsectors.setName("Sub-Sectors Label - test");
		subsectors.setCode(SUBSECTORS_LABEL_TEST);
		subsectors.setParentCategory(sectors);
		
		Category fakeLabel = new Category();
		fakeLabel.setName("Fake label - test");
		fakeLabel.setCode("fake_label_test"); 
		fakeLabel.setParentCategory(labelRoot);
		
		categoryService.save(labelRoot);
	}

}
