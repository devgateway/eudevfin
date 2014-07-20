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
package org.devgateway.eudevfin.dim.desktop.components.util;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.eudevfin.ui.common.models.ProxyModel;

/**
 * @author Alex
 *
 */
public class ComponentsUtil {

	public static Label generateLabel(String trnKey, String wicketId, Component component) {
		return ComponentsUtil.generateLabel(trnKey, wicketId, component, null);
	}
	public static Label generateLabel(String trnKey, String wicketId, Component component, IModel<?> model) {
		ProxyModel<String> labelTxNameModel = new ProxyModel<String>(new StringResourceModel(trnKey, component, model, null));
		Label txNameLabel = new Label(wicketId, labelTxNameModel);
		return txNameLabel;
	}

}
