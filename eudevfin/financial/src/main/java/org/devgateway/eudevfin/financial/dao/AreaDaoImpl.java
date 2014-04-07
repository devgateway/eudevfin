/**
 * 
 */
package org.devgateway.eudevfin.financial.dao;

import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.financial.repository.AreaRepository;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Alex
 *
 */

@Component
@Lazy(value=false)
public class AreaDaoImpl extends AbstractDaoImpl<Area,Long, AreaRepository> {

	@Autowired
	private AreaRepository areaRepository;
	
	@Override
	protected AreaRepository getRepo() {
		return this.areaRepository;
	}

	@Override
	@ServiceActivator(inputChannel="findAllAsListAreaChannel")
	public List<Area> findAllAsList() {
		return super.findAllAsList();
	}

	@Override
	@ServiceActivator(inputChannel="findAreaByIdChannel")
	public NullableWrapper<Area> findOne(Long id) {
		return super.findOne(id);
	}

	@Override
	@ServiceActivator(inputChannel="saveAreaChannel")
	public NullableWrapper<Area> save(Area e) {
		return super.save(e);
	}
	
	@ServiceActivator(inputChannel="findAreaByGeneralSearchChannel")
	public List<Area> findByGeneralSearch(@Header("locale")String locale, String searchString) {
		return this.getRepo().findByTranslationLocaleAndTranslationNameContaining(locale, searchString);
	}

	@Override
	@ServiceActivator(inputChannel = "findByGeneralSearchPageableAreaChannel")
	public Page<Area> findByGeneralSearch(String searchString,
			@Header(value="locale",required=false) String locale, @Header("pageable") Pageable pageable) {
		if(searchString.isEmpty()) return getRepo().findAll(pageable);
		return getRepo().findByTranslationNameContaining(searchString.toLowerCase(), pageable);
	}

    /**
     * @see org.devgateway.eudevfin.metadata.common.service.AreaService#findUsedAreaPaginated(String, String, Pageable)
     *
     * @param locale
     * @param searchString
     * @param page
     *
     * @return Page<Area>
     */
    @ServiceActivator(inputChannel = "findUsedAreaPaginatedAreaChannel")
    @Transactional
    public Page<Area> findUsedAreaPaginated(
            @Header("locale") String locale, String searchString,
            @Header("pageable") Pageable page) {
        Page<Area> result = null;

        if (searchString.isEmpty()) {
            result = this.getRepo().findUsedArea(page);
        }
        else {
            result = this.getRepo().findUsedAreaByTranslationsNameIgnoreCase(locale, searchString.toLowerCase(), page);
        }

        return result;
    }
	
	public Area findByCode(String code) {
		return this.getRepo().findByCode(code);
	}
}
