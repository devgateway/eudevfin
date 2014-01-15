/**
 * 
 */
package org.devgateway.eudevfin.financial.dao;

import java.util.List;

import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.exception.NoDataFoundException;
import org.devgateway.eudevfin.financial.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Alex
 *
 */
@Component
@Lazy(value=false)
//@Transactional( readOnly=false, propagation=Propagation.REQUIRED)
public class CategoryDaoImpl extends AbstractDaoImpl<Category, Long, CategoryRepository> {

	@Autowired
	private CategoryRepository repo;
	
	@Override
	protected
	CategoryRepository getRepo() {
		return repo;
	}

	@Override
	@ServiceActivator(inputChannel="saveCategoryChannel")
	public NullableWrapper<Category> save(Category o) {
		return super.save(o);
	}
	
	@ServiceActivator(inputChannel="findCategoryByTagCodeChannel")
	public List<Category> findByTagsCode(String code) {
		return getRepo().findByTagsCode(code);
	}
	
//	@ServiceActivator(inputChannel="findCategoryByCodeChannel")
//	@Transactional
//	public Category findByCode(String code, @Header("initializeChildren") Boolean initializeChildren) {
//		Category category	= null;
//		if ( initializeChildren != null && initializeChildren) {
//			category	= findByCodeTransactional(code);
//		}
//		else {
//			category	= getRepo().findByCode(code);
//		}
//		
//		return category;
//	}
	
//	public Category findByCodeTransactional (String code) {
//		Category category	= getRepo().findByCode(code);
//		if ( category != null )
//			initializeChildren(category);
//		return category;
//	}
	
	public List<Category> findByCode(String code) {
		return getRepo().findByCode(code);
	}
	
	@ServiceActivator(inputChannel="findCategoryByCodeAndClassChannel")
	@Transactional
	public  NullableWrapper<Category> findByCodeAndClass(String code, @Header("clazz")Class<? extends Category> clazz, 
			@Header("initializeChildren") Boolean initializeChildren) {
		List<Category> categories	= this.findByCode(code);
		for ( Category category:categories ) {
			if ( category.getClass().equals(clazz) ) {
				this.initializeChildren(category);
				return newWrapper(category);
			}
		}
		throw new NoDataFoundException(
				String.format("No category found for code %s and class %s ", code,clazz.getName())
			);
	}
	
	@ServiceActivator(inputChannel="findCategoryByGeneralSearchAndTagsCodeChannel")
	public List<Category> findByGeneralSearchAndTagsCode(@Header("locale")String locale, 
			String searchString, @Header("tagsCode") String tagsCode) {
		return this.getRepo().
			findByTranslationsLocaleAndTranslationsNameIgnoreCaseContainsAndTagsCode(
					locale, searchString, tagsCode);
	}
	
	
	public void initializeChildren(Category category) {
		if ( category.getChildren() != null ) {
			for (Category childCateg : category.getChildren()) {
				if (childCateg != null)
					initializeChildren(childCateg);
			}
		}
		if ( category.getTags() != null ) {
			for (Category childCateg : category.getTags() ) {
				childCateg.getCode();
			}
		}
	}
	
}
