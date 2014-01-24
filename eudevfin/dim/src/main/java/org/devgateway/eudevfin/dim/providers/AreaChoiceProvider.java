/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.providers;

import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.financial.Area;
import org.devgateway.eudevfin.financial.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

/**
 * @author aartimon
 * @since 14/01/14
 */
@Component
public class AreaChoiceProvider extends AbstractTranslatableProvider<Area> {

	public AreaChoiceProvider() {
		//this.sort=new Sort(Direction.ASC,"code");
	}
	
    @Autowired
    private AreaService areaService;


    @Override
    protected BaseEntityService<Area> getService() {
        return areaService;
    }

    @Override
    public String getDisplayText(Area choice) {
        return choice.getCode()+ " - "+ choice.getName();
    }
}
