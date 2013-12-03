/**
 * 
 */
package org.devgateway.eudevfin.financial.dao;

import java.util.List;

import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Alex
 *
 */
@Component
//@Transactional( readOnly=false, propagation=Propagation.REQUIRED)
public class CategoryDaoImpl extends AbstractDaoImpl<Category, CategoryRepository> {

	@Autowired
	private CategoryRepository repo;
	
	@Override
	CategoryRepository getRepo() {
		return repo;
	}

	@Override
	@ServiceActivator(inputChannel="saveCategoryChannel")
	public Category save(Category o) {
		return super.save(o);
	}
	
	@ServiceActivator(inputChannel="findCategoryByLabelCodeChannel")
	public List<Category> findByTagsCode(String code) {
		return getRepo().findByTagsCode(code);
	}
	
	@ServiceActivator(inputChannel="findCategoryByCodeChannel")
	@Transactional
	public Category findByCode(String code, @Header("initializeChildren") Boolean initializeChildren) {
		Category category	= null;
		if ( initializeChildren != null && initializeChildren) {
			category	= findByCodeTransactional(code);
		}
		else {
			category	= getRepo().findByCode(code);
		}
		
		return category;
	}
	
	public Category findByCodeTransactional (String code) {
		Category category	= getRepo().findByCode(code);
		if ( category != null )
			initializeChildren(category);
		return category;
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
