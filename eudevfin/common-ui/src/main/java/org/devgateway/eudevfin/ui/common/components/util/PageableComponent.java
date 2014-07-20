/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.ui.common.components.util;

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.devgateway.eudevfin.common.service.PagingItem;

public interface PageableComponent {

	public AjaxLink<PagingItem> createLink(PagingItem modelObj);

}