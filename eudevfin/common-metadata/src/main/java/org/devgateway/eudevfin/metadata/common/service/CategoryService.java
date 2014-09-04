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
package org.devgateway.eudevfin.metadata.common.service;

import java.util.List;

import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;

/**
 * @author Alex
 *
 */
public interface CategoryService extends BaseEntityService<Category> {

	public List<Category> findByTagsCode(String labelCode);

	public <T extends Category>NullableWrapper<T> findByCodeAndClass(String string,
			@Header("clazz")Class<T> clazz,
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

	public Page<Category> findUsedGeographyPaginated(
			@Header("locale") String locale, String searchString,
			@Header("pageable") Pageable page,
			@Header("initializeChildren") Boolean initializeChildren);

	public Page<Category> findUsedSectorPaginated(
			@Header("locale") String locale, String searchString,
			@Header("pageable") Pageable page,
			@Header("initializeChildren") Boolean initializeChildren);

	public Page<Category> findUsedTypeOfAidPaginated(
			@Header("locale") String locale, String searchString,
			@Header("pageable") Pageable page,
			@Header("initializeChildren") Boolean initializeChildren);

	public Page<Category> findUsedTypeOfFlowBiMultiPaginated(
			@Header("locale") String locale, String searchString,
			@Header("pageable") Pageable page,
			@Header("initializeChildren") Boolean initializeChildren);

	public Page<Category> findUsedChannelPaginated(
			@Header("locale") String locale, String searchString,
			@Header("pageable") Pageable page);
}
