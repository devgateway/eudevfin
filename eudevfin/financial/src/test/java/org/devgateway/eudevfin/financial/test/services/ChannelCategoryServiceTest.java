package org.devgateway.eudevfin.financial.test.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;

import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
import org.devgateway.eudevfin.metadata.common.service.CategoryService;
import org.devgateway.eudevfin.metadata.common.service.ChannelCategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@Transactional
public class ChannelCategoryServiceTest {

	private static final String FAKE_TAG_CODE = "fake_tag_code";

	@Autowired
	ChannelCategoryService channelCategoryService;
	
	@Autowired
	CategoryService categoryService;
	
	
	@Test
	public void testFindByGeneralSearchAndTagsCodePaginated() {
		
		this.createLabels();
		final Category fakeTag	= 
				this.categoryService.findByCodeAndClass(FAKE_TAG_CODE, Category.class, false).getEntity();
		
		Page<ChannelCategory> result = this.channelCategoryService.findByGeneralSearchAndTagsCodePaginated("en", "tcc", 
				FAKE_TAG_CODE, new PageRequest(0, 10), true);
		
		assertEquals(0, result.getTotalElements());
		
		result = this.channelCategoryService.findByGeneralSearchAndTagsCodePaginated("en", "test", 
				FAKE_TAG_CODE, new PageRequest(0, 10), true);
		
		assertEquals(0, result.getTotalElements());
		
		final ChannelCategory testChannelCategory	= new ChannelCategory();
		testChannelCategory.setName("Test Tag Category For General Search");
		testChannelCategory.setCode("Test code");
		testChannelCategory.setAcronym("TCCFGS");
		testChannelCategory.setTags(new HashSet<Category>());
		testChannelCategory.getTags().add(fakeTag);
		this.channelCategoryService.save(testChannelCategory);
		
		final List<ChannelCategory> allChannelCategories = this.channelCategoryService.findAll();
		assertTrue(allChannelCategories != null);
		assertTrue(allChannelCategories.size() > 0 );
		
		result = this.channelCategoryService.findByGeneralSearchAndTagsCodePaginated("en", "test", 
				FAKE_TAG_CODE, new PageRequest(0, 10), true);
		
		assertEquals(1, result.getTotalElements());
		
		result = this.channelCategoryService.findByGeneralSearchAndTagsCodePaginated("en", "tcc", 
				FAKE_TAG_CODE, new PageRequest(0, 10), true);
		
		assertEquals(1, result.getTotalElements());
		
	}
	
	/**
	 * creates some fake labels for the test
	 */
	@Transactional
	private void createLabels() {
		final Category labelRoot	= new Category();
		labelRoot.setName("Label Root - test");
		labelRoot.setCode("label_root_test");
		
		final Category fakeLabel = new Category();
		fakeLabel.setName("Fake label - test");
		fakeLabel.setCode(FAKE_TAG_CODE); 
		fakeLabel.setParentCategory(labelRoot);
		
		this.categoryService.save(labelRoot);
	}

}
