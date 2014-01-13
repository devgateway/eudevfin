/**
 * 
 */
package org.devgateway.eudevfin.financial.dao;

import java.util.List;

import org.devgateway.eudevfin.financial.Area;
import org.devgateway.eudevfin.financial.repository.AreaRepository;
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
public class AreaDaoImpl extends AbstractDaoImpl<Area, AreaRepository> {

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
	public Area findOne(Long id) {
		return super.findOne(id);
	}

	@Override
	@ServiceActivator(inputChannel="saveAreaChannel")
	public Area save(Area e) {
		return super.save(e);
	}
	
	
	

}
