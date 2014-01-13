/**
 * 
 */
package org.devgateway.eudevfin.financial.service;

import java.util.List;

import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.ChannelCategory;
import org.springframework.integration.annotation.Header;

/**
 * @author Alex
 *
 */
public interface ChannelCategoryService extends BaseEntityService<ChannelCategory> {
	public List<ChannelCategory> findByTagsCode(String labelCode);

	public ChannelCategory findByCode(String string, @Header("initializeChildren") Boolean initializeChildren);
}
