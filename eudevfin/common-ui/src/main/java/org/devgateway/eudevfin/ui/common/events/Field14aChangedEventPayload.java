/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.events;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * @author mihai
 * @see CofinancingField14UpdateBehavior
 */
public class Field14aChangedEventPayload extends AbstractAjaxUpdateEventPayload {
	private Boolean field14Code;
	
	public Field14aChangedEventPayload(AjaxRequestTarget target,Boolean field14Code) {
		super(target);
		this.field14Code=field14Code;
	}

	/**
	 * @return the field14Code
	 */
	public Boolean getField14Code() {
		return field14Code;
	}

	/**
	 * @param field14Code the field14Code to set
	 */
	public void setField14Code(Boolean field14Code) {
		this.field14Code = field14Code;
	}


}
