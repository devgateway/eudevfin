/**
 * 
 */
package org.devgateway.eudevfin.financial.dao;

import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Alex
 *
 */
@Component
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
}
