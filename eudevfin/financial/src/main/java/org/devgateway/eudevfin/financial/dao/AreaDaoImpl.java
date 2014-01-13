/**
 * 
 */
package org.devgateway.eudevfin.financial.dao;

import org.devgateway.eudevfin.financial.Area;
import org.devgateway.eudevfin.financial.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

}
