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
import org.devgateway.eudevfin.financial.repository.AreaRepository;
import org.devgateway.eudevfin.metadata.common.domain.Area;
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
	public NullableWrapper<Area> findOne(final Long id) {
		return super.findOne(id);
	}

	@Override
	@ServiceActivator(inputChannel="saveAreaChannel")
	public NullableWrapper<Area> save(final Area e) {
		return super.save(e);
	}

	@ServiceActivator(inputChannel="findAreaByGeneralSearchChannel")
	public List<Area> findByGeneralSearch(@Header("locale") final String locale, final String searchString) {
		return this.getRepo().findByTranslationLocaleAndTranslationNameContaining(locale, searchString);
	}

	@Override
	@ServiceActivator(inputChannel = "findByGeneralSearchPageableAreaChannel")
	public Page<Area> findByGeneralSearch(final String searchString,
			@Header(value="locale",required=false) final String locale, @Header("pageable") final Pageable pageable) {
		if(searchString.isEmpty()) {
			return this.getRepo().findAll(pageable);
		}
		return this.getRepo().findByTranslationNameContaining(searchString.toLowerCase(), pageable);
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
			@Header("locale") final String locale, final String searchString,
			@Header("pageable") final Pageable page) {
		Page<Area> result = null;

		if (searchString.isEmpty()) {
			result = this.getRepo().findUsedArea(page);
		}
		else {
			result = this.getRepo().findUsedAreaByTranslationsNameIgnoreCase(locale, searchString.toLowerCase(), page);
		}

		return result;
	}

	@ServiceActivator(inputChannel="findAreaByCodeChannel")
	public NullableWrapper<Area> findByCode(final String code) {
		final Area a = this.getRepo().findByCode(code);
		return new NullableWrapper<Area>(a);
	}
        
        @ServiceActivator(inputChannel="findUsedAreaAsListChannel")
	public List<Area> findUsedAreaAsList(final String locale) {
		return this.getRepo().findUsedAreaAsList(locale);
	}
}
