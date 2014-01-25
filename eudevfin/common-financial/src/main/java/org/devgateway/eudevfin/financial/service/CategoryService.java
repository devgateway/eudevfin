/**
 * 
 */
package org.devgateway.eudevfin.financial.service;

import java.util.List;

import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.financial.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;

/**
 * @author Alex
 *
 */
public interface CategoryService extends BaseEntityService<Category> {

	public List<Category> findByTagsCode(String labelCode);

	public NullableWrapper<Category> findByCodeAndClass(String string, 
			@Header("clazz")Class<? extends Category> clazz, 
			@Header("initializeChildren") Boolean initializeChildren);
	
	public Page<Category> findByGeneralSearchAndTagsCodePaginated(
			@Header("locale") String locale, String searchString,
			@Header("tagsCode") String tagsCode,
			@Header("pageable") Pageable page,
			@Header("initializeChildren") Boolean initializeChildren);

	public List<Category> findByGeneralSearchAndTagsCode(
			@Header("locale") String locale, String searchString,
			@Header("tagsCode") String tagsCode,
			@Header("initializeChildren") Boolean initializeChildren);
}
