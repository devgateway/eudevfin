/**
 * 
 */
package org.devgateway.eudevfin.financial.dao;

import java.util.List;

import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.financial.Area;
import org.devgateway.eudevfin.financial.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

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
	}
