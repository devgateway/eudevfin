/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.ui.common.components.util;

import java.io.Serializable;

import org.devgateway.eudevfin.common.service.PagingHelper;

public interface ListGeneratorInterface<T> extends Serializable {
	
	public PagingHelper<T> getResultsList(int pageNumber, int pageSize); 

}
