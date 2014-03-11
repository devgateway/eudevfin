/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

/**
 *
 */
package org.devgateway.eudevfin.ui.common.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.eudevfin.ui.common.AttributePrepender;

/**
 * @author mihai
 */
public abstract class BootstrapDeleteButton extends IndicatingAjaxButton {

	private static final long serialVersionUID = -5517676563223368178L;

	/**
	 * @param id
	 * @param model
	 */
	public BootstrapDeleteButton(String id, IModel<String> model) {
		super(id, model);
		add(new AttributeAppender("class", new Model<String>("btn btn-danger"), " "));
		add(new AttributePrepender("onclick", new Model<String>("window.onbeforeunload = null;"), " "));
		setDefaultFormProcessing(false);
	}

	@Override
	protected abstract void onSubmit(AjaxRequestTarget target, Form<?> form);

	/**
	 * https://cwiki.apache.org/confluence/display/WICKET/Getting+user+
	 * confirmation
	 */
	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		// TODO Auto-generated method stub
		super.updateAjaxAttributes(attributes);

		AjaxCallListener ajaxCallListener = new AjaxCallListener();
		ajaxCallListener.onPrecondition("return confirm(' "
				+ new StringResourceModel("modal.delete", this.getPage(), null, null).getObject() + " ');");
		attributes.getAjaxCallListeners().add(ajaxCallListener);
	}

	@Override
	protected void onError(final AjaxRequestTarget target, Form<?> form) {

	}
}
