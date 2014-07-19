/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.ui.common.components;

import org.apache.wicket.markup.html.basic.Label;

/**
 * Interface defining a mode for
 * {@link org.apache.wicket.markup.html.panel.Panel} descendants that can have a
 * mode where they don't show editable form components but only read only
 * {@link Label} views of the attached models
 * 
 * @author mihai
 * 
 */
public interface PreviewableFormPanelAware {

	/**
	 * @return True if the underlying panel form is in preview mode
	 */
	public boolean isFormInPreview();
}
