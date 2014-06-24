/**
 * 
 */
package org.devgateway.eudevfin.financial.dao;

import java.util.List;

import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.financial.repository.ChannelCategoryRepository;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alex
 *
 */
@Component
@Lazy(value=false)
public class ChannelCategoryDao extends AbstractDaoImpl<ChannelCategory, Long, ChannelCategoryRepository> {
	@Autowired
	private ChannelCategoryRepository repo;

	@Override
	protected ChannelCategoryRepository getRepo() {
		return this.repo;
	}

	
	@Override
	@ServiceActivator(inputChannel="findAllAsListChannelCategoryChannel")
	public List<ChannelCategory> findAllAsList() {
		return super.findAllAsList();
	}
	
	@ServiceActivator(inputChannel="findChannelCategoryByCodeChannel")
	public NullableWrapper<ChannelCategory> findByCode(final String code) {
		return this.newWrapper(this.getRepo().findByCode(code));
	}
	
	
//	@ServiceActivator(inputChannel="findChannelCategoryByGeneralSearchPageable")
//	@Override
//	public Page<ChannelCategory> findByGeneralSearch(final String searchString,
//			@Header(value="locale",required=false) final String locale, @Header("pageable") final Pageable pageable) { 
//		if(searchString.isEmpty()) {
//			return this.getRepo().findByParentCategoryNotNull(pageable);
//		}
//		return this.repo.findByTranslationNameContaining(searchString, pageable);
//	}
	
	
	
	@ServiceActivator(inputChannel="findOneChannelCategory")
	@Override
	public NullableWrapper<ChannelCategory> findOne(final Long id) {
		return super.findOne(id);
	}
	
	@ServiceActivator(inputChannel = "findChannelCategoryByGeneralSearchAndTagsCodePaginatedChannel")
	@Transactional
	public Page<ChannelCategory> findByGeneralSearchAndTagsCodePaginated(
			@Header("locale") final String locale, final String searchString,
			@Header("tagsCode") final String tagsCode,
			@Header("pageable") final Pageable page,
			@Header("initializeChildren") final Boolean initializeChildren) {

		Page<ChannelCategory> result	= null;
		if (searchString.isEmpty()) {
			result 	= this.getRepo().findByTagsCode(tagsCode, page);
		} else {
			result	= this.getRepo().findByTranslationsNameIgnoreCaseContainsAndTagsCode(
						searchString.toLowerCase(), tagsCode, page);
		}

		CategoryDaoImpl.initializeChildrenIfNeeded(result, initializeChildren);

		return result;

	}

}
