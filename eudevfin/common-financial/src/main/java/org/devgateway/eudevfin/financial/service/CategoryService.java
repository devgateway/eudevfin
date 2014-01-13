/**
 * 
 */
package org.devgateway.eudevfin.financial.service;

import java.util.List;

import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.financial.Category;
import org.springframework.integration.annotation.Header;

/**
 * @author Alex
 *
 */
public interface CategoryService extends BaseEntityService<Category> {
	public List<Category> findByTagsCode(String labelCode);

	public Category findByCodeAndClass(String string, 
			@Header("clazz")Class<? extends Category> clazz, 
			@Header("initializeChildren") Boolean initializeChildren);
}
