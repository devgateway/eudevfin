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
package org.devgateway.eudevfin.financial.dao;

import java.util.List;

import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.financial.repository.CategoryRepository;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
		return this.repo;
	}

	@Override
	@ServiceActivator(inputChannel="saveCategoryChannel")
	public NullableWrapper<Category> save(final Category o) {
		return super.save(o);
	}

	@ServiceActivator(inputChannel="findCategoryByTagCodeChannel")
	public List<Category> findByTagsCode(final String code) {
		return this.getRepo().findByTagsCode(code);
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

	public List<Category> findByCode(final String code) {
		return this.getRepo().findByCode(code);
	}

	@ServiceActivator(inputChannel="findCategoryByCodeAndClassChannel")
	@Transactional
	public  NullableWrapper<Category> findByCodeAndClass(final String code, @Header("clazz") final Class<? extends Category> clazz,
			@Header("initializeChildren") final Boolean initializeChildren) {
		final List<Category> categories	= this.findByCode(code);
		for ( final Category category:categories ) {
			if ( category.getClass().equals(clazz) ) {
				initializeChildren(category);
				return this.newWrapper(category);
			}
		}
		return this.newWrapper(null);
//		throw new NoDataFoundException(
//				String.format("No category found for code %s and class %s ", code,clazz.getName())
//			);
	}

	/**
	 *
	 * @see CategoryService#findByGeneralSearchAndTagsCodePaginated(String, String, String, Pageable)
	 * @param locale
	 * @param searchString
	 * @param tagsCode
	 * @param page
	 * @return
	 */
	@ServiceActivator(inputChannel = "findCategoryByGeneralSearchAndTagsCodePaginatedChannel")
	@Transactional
	public Page<Category> findByGeneralSearchAndTagsCodePaginated(
			@Header("locale") final String locale, final String searchString,
			@Header("tagsCode") final String tagsCode,
			@Header("pageable") final Pageable page,
			@Header("initializeChildren") final Boolean initializeChildren) {

		Page<Category> result	= null;
		if (searchString.isEmpty()) {
			result 	= this.getRepo().findByTagsCode(tagsCode, page);
		} else {
			result	= this.getRepo().findByTranslationsNameIgnoreCaseContainsAndTagsCode(
						searchString.toLowerCase(), tagsCode, page);
		}

		initializeChildrenIfNeeded(result, initializeChildren);

		return result;

	}


	@ServiceActivator(inputChannel="findCategoryByGeneralSearchAndTagsCodeChannel")
	@Transactional
	public List<Category> findByGeneralSearchAndTagsCode(
			@Header("locale") final String locale, final String searchString,
			@Header("tagsCode") final String tagsCode,
			@Header("initializeChildren") final Boolean initializeChildren) {

		List<Category> result = null;
		if ( searchString == null || searchString.isEmpty() ) {
			result	= this.getRepo().findByTagsCode(tagsCode);
		} else {
			result 	= this.getRepo().
					findByTranslationsLocaleAndTranslationsNameIgnoreCaseContainsAndTagsCode(
						locale, searchString.toLowerCase(), tagsCode);
		}

		initializeChildrenIfNeeded(result, initializeChildren);

		return result;
	}

	/**
	 * @see CategoryService#findOne(Long)
	 */
	@ServiceActivator(inputChannel="findCategoryByIdChannel")
	@Override
	@Transactional
	public NullableWrapper<Category> findOne(final Long id) {
		final Boolean initializeChildren			= true;
		final NullableWrapper<Category> result	= super.findOne(id);
		if ( initializeChildren && !result.isNull() ) {
			initializeChildren(result.getEntity());
		}
		return result;
	}

    /**
     *
     * @see CategoryService#findUsedGeographyPaginated (String, String, Pageable)
     * @param locale
     * @param searchString
     * @param page
     * @return
     */
    @ServiceActivator(inputChannel = "findUsedGeographyPaginatedChannel")
    @Transactional
    public Page<Category> findUsedGeographyPaginated(
            @Header("locale") final String locale, final String searchString,
            @Header("pageable") final Pageable page,
            @Header("initializeChildren") final Boolean initializeChildren) {

        Page<Category> result;
        if (searchString.isEmpty()) {
            result = this.getRepo().findUsedGeography(page);
        }
        else {
            result = this.getRepo().findUsedGeographyByTranslationsNameIgnoreCase(locale, searchString.toLowerCase(), page);
        }

        initializeChildrenIfNeeded(result, initializeChildren);

        return result;
    }

    /**
     *
     * @see CategoryService#findUsedSectorPaginated (String, String, Pageable)
     * @param locale
     * @param searchString
     * @param page
     * @return
     */
    @ServiceActivator(inputChannel = "findUsedSectorPaginatedChannel")
    @Transactional
    public Page<Category> findUsedSectorPaginated(
            @Header("locale") final String locale, final String searchString,
            @Header("pageable") final Pageable page,
            @Header("initializeChildren") final Boolean initializeChildren) {

        Page<Category> result;
        if (searchString.isEmpty()) {
            result = this.getRepo().findUsedSector(page);
        }
        else {
            result = this.getRepo().findUsedSectorByTranslationsNameIgnoreCase(locale, searchString.toLowerCase(), page);
        }

        initializeChildrenIfNeeded(result, initializeChildren);

        return result;
    }

    /**
     *
     * @see CategoryService#findUsedTypeOfAidPaginated (String, String, Pageable)
     * @param locale
     * @param searchString
     * @param page
     * @return
     */
    @ServiceActivator(inputChannel = "findUsedTypeOfAidPaginatedChannel")
    @Transactional
    public Page<Category> findUsedTypeOfAidPaginated(
            @Header("locale") final String locale, final String searchString,
            @Header("pageable") final Pageable page,
            @Header("initializeChildren") final Boolean initializeChildren) {

        Page<Category> result;
        if (searchString.isEmpty()) {
            result = this.getRepo().findUsedTypeOfAid(page);
        }
        else {
            result = this.getRepo().findUsedTypeOfAidByTranslationsNameIgnoreCase(locale, searchString.toLowerCase(), page);
        }

        initializeChildrenIfNeeded(result, initializeChildren);

        return result;
    }

    /**
     *
     * @see CategoryService#findUsedTypeOfFlowBiMultiPaginated (String, String, Pageable)
     * @param locale
     * @param searchString
     * @param page
     * @return
     */
    @ServiceActivator(inputChannel = "findUsedTypeOfFlowBiMultiPaginatedChannel")
    @Transactional
    public Page<Category> findUsedTypeOfFlowBiMultiPaginated(
            @Header("locale") final String locale, final String searchString,
            @Header("pageable") final Pageable page,
            @Header("initializeChildren") final Boolean initializeChildren) {

        Page<Category> result;
        if (searchString.isEmpty()) {
            result = this.getRepo().findUsedTypeOfFlowBiMulti(page);
        }
        else {
            result = this.getRepo().findUsedTypeOfFlowBiMultiByTranslationsNameIgnoreCase(locale, searchString.toLowerCase(), page);
        }

        initializeChildrenIfNeeded(result, initializeChildren);

        return result;
    }

    /**
     *
     * @see CategoryService#findUsedChannelPaginated (String, String, Pageable)
     * @param locale
     * @param searchString
     * @param page
     * @return
     */
    @ServiceActivator(inputChannel = "findUsedChannelPaginatedChannel")
    @Transactional
    public Page<Category> findUsedChannelPaginated(
            @Header("locale") final String locale, final String searchString,
            @Header("pageable") final Pageable page) {

        Page<Category> result;
        if (searchString.isEmpty()) {
            result = this.getRepo().findUsedChannel(page);
        }
        else {
            result = this.getRepo().findUsedChannelByTranslationsNameIgnoreCase(locale, searchString.toLowerCase(), page);
        }

        return result;
    }

    public static void initializeChildrenIfNeeded(final Iterable<? extends Category> categories, Boolean initializeChildren) {
		initializeChildren	= initializeChildren == null ? false : initializeChildren;
		if ( initializeChildren && categories != null ) {
			for (final Category category : categories) {
				initializeChildren(category);
			}
		}
	}
	
	public static void initializeChildren(final Category category) {
		if ( category.getChildren() != null ) {
			for (final Category childCateg : category.getChildren()) {
				if (childCateg != null) {
					initializeChildren(childCateg);
				}
			}
		}
		if ( category.getTags() != null ) {
			for (final Category childCateg : category.getTags() ) {
				childCateg.getCode();
			}
		}
	}
	
}
