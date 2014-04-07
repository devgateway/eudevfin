/**
 * 
 */
package org.devgateway.eudevfin.financial.dao;

import java.util.Collection;
import java.util.List;

import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.financial.exception.NoDataFoundException;
import org.devgateway.eudevfin.financial.repository.CategoryRepository;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
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
		return newWrapper(null);
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
			@Header("locale") String locale, String searchString,
			@Header("tagsCode") String tagsCode,
			@Header("pageable") Pageable page,
			@Header("initializeChildren") Boolean initializeChildren) {

		Page<Category> result	= null;
		if (searchString.isEmpty())
			result 	= this.getRepo().findByTagsCode(tagsCode, page);
		else
			result	= this.getRepo().findByTranslationsNameIgnoreCaseContainsAndTagsCode(
						searchString.toLowerCase(), tagsCode, page);

		this.initializeChildrenIfNeeded(result, initializeChildren);

		return result;

	}


	@ServiceActivator(inputChannel="findCategoryByGeneralSearchAndTagsCodeChannel")
	@Transactional
	public List<Category> findByGeneralSearchAndTagsCode(
			@Header("locale") String locale, String searchString,
			@Header("tagsCode") String tagsCode,
			@Header("initializeChildren") Boolean initializeChildren) {

		List<Category> result = null;
		if ( searchString == null || searchString.isEmpty() )
			result	= this.getRepo().findByTagsCode(tagsCode);
		else
			result 	= this.getRepo().
					findByTranslationsLocaleAndTranslationsNameIgnoreCaseContainsAndTagsCode(
						locale, searchString.toLowerCase(), tagsCode);

		this.initializeChildrenIfNeeded(result, initializeChildren);

		return result;
	}

	/**
	 * @see CategoryService#findOne(Long)
	 */
	@ServiceActivator(inputChannel="findCategoryByIdChannel")
	@Override
	@Transactional
	public NullableWrapper<Category> findOne(Long id) {
		Boolean initializeChildren			= true;
		NullableWrapper<Category> result	= super.findOne(id);
		if ( initializeChildren && !result.isNull() ) {
			this.initializeChildren(result.getEntity());
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
            @Header("locale") String locale, String searchString,
            @Header("pageable") Pageable page,
            @Header("initializeChildren") Boolean initializeChildren) {

        Page<Category> result;
        if (searchString.isEmpty()) {
            result = this.getRepo().findUsedGeography(page);
        }
        else {
            result = this.getRepo().findUsedGeographyByTranslationsNameIgnoreCase(locale, searchString.toLowerCase(), page);
        }

        this.initializeChildrenIfNeeded(result, initializeChildren);

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
            @Header("locale") String locale, String searchString,
            @Header("pageable") Pageable page,
            @Header("initializeChildren") Boolean initializeChildren) {

        Page<Category> result;
        if (searchString.isEmpty()) {
            result = this.getRepo().findUsedSector(page);
        }
        else {
            result = this.getRepo().findUsedSectorByTranslationsNameIgnoreCase(locale, searchString.toLowerCase(), page);
        }

        this.initializeChildrenIfNeeded(result, initializeChildren);

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
            @Header("locale") String locale, String searchString,
            @Header("pageable") Pageable page,
            @Header("initializeChildren") Boolean initializeChildren) {

        Page<Category> result;
        if (searchString.isEmpty()) {
            result = this.getRepo().findUsedTypeOfAid(page);
        }
        else {
            result = this.getRepo().findUsedTypeOfAidByTranslationsNameIgnoreCase(locale, searchString.toLowerCase(), page);
        }

        this.initializeChildrenIfNeeded(result, initializeChildren);

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
            @Header("locale") String locale, String searchString,
            @Header("pageable") Pageable page,
            @Header("initializeChildren") Boolean initializeChildren) {

        Page<Category> result;
        if (searchString.isEmpty()) {
            result = this.getRepo().findUsedTypeOfFlowBiMulti(page);
        }
        else {
            result = this.getRepo().findUsedTypeOfFlowBiMultiByTranslationsNameIgnoreCase(locale, searchString.toLowerCase(), page);
        }

        this.initializeChildrenIfNeeded(result, initializeChildren);

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
            @Header("locale") String locale, String searchString,
            @Header("pageable") Pageable page) {

        Page<Category> result;
        if (searchString.isEmpty()) {
            result = this.getRepo().findUsedChannel(page);
        }
        else {
            result = this.getRepo().findUsedChannelByTranslationsNameIgnoreCase(locale, searchString.toLowerCase(), page);
        }

        return result;
    }

    public void initializeChildrenIfNeeded(Iterable<Category> categories, Boolean initializeChildren) {
		initializeChildren	= initializeChildren == null ? false : initializeChildren;
		if ( initializeChildren && categories != null ) {
			for (Category category : categories) {
				this.initializeChildren(category);
			}
		}
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
