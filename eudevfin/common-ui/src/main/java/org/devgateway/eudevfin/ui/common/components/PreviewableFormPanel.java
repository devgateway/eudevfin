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
package org.devgateway.eudevfin.ui.common.components;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;

/**
 * @author mihai
 *
 */
public abstract class PreviewableFormPanel extends Panel implements PreviewableFormPanelAware {

	private static final long serialVersionUID = -4413222821613083939L;

	/**
	 * @see Panel#Panel(String)
	 */
	public PreviewableFormPanel(String id) {
		super(id);
	}

	/**
	 * @see Panel#Panel(String, IModel)
	 */
	public PreviewableFormPanel(String id, IModel<?> model) {
		super(id, model);
	}

	@Override
	public boolean isFormInPreview() {
		MarkupContainer parent = this.getParent();

		while (parent != null && !(parent instanceof PreviewableFormPanelAware))
			parent = parent.getParent();

		if (parent instanceof PreviewableFormPanelAware)
			return ((PreviewableFormPanelAware) parent).isFormInPreview();
		else
			return false;
	}
}
