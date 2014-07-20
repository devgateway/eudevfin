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

import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;

/**
 * @author Alex
 *
 */
public interface ChannelCategoryService extends BaseEntityService<ChannelCategory> {

	public NullableWrapper<ChannelCategory> findByCode(String string, @Header("initializeChildren") Boolean initializeChildren);
	
	public Page<ChannelCategory> findByGeneralSearchAndTagsCodePaginated(
			@Header("locale") String locale, String searchString,
			@Header("tagsCode") String tagsCode,
			@Header("pageable") Pageable page,
			@Header("initializeChildren") Boolean initializeChildren);
}
