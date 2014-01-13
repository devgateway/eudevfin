/**
 * 
 */
package org.devgateway.eudevfin.financial.dao;

import java.util.List;

import org.devgateway.eudevfin.financial.ChannelCategory;
import org.devgateway.eudevfin.financial.repository.ChannelCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

/**
 * @author Alex
 *
 */
@Component
@Lazy(value=false)
public class ChannelCategoryDao extends AbstractDaoImpl<ChannelCategory, ChannelCategoryRepository> {
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

}
