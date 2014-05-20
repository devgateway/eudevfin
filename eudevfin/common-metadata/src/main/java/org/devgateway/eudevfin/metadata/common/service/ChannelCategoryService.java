/**
 * 
 */
package org.devgateway.eudevfin.metadata.common.service;

import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
import org.springframework.integration.annotation.Header;

/**
 * @author Alex
 *
 */
public interface ChannelCategoryService extends BaseEntityService<ChannelCategory> {

	public NullableWrapper<ChannelCategory> findByCode(String string, @Header("initializeChildren") Boolean initializeChildren);
}
