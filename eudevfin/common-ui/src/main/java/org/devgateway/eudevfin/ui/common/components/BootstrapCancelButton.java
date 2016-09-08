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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author mihai
 */
public abstract class BootstrapCancelButton extends IndicatingAjaxButton {

	private static final long serialVersionUID = 2364680773757310635L;

	/**
	 * @param id
	 * @param model
	 */
	public BootstrapCancelButton(String id, IModel<String> model) {
		super(id, model);
		add(new AttributeAppender("class", new Model<String>("btn"), " "));
		setDefaultFormProcessing(false);
	}

	@Override
	protected abstract void onSubmit(AjaxRequestTarget target, Form<?> form);

	@Override
	protected void onError(final AjaxRequestTarget target, Form<?> form) {
	}
}
