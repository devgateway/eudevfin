/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.ui.common.events;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * @author mihai
 * @see MarkersField13UpdateBehavior
 */
public class Field13ChangedEventPayload extends AbstractAjaxUpdateEventPayload {
	private String field13Code;
	
	public Field13ChangedEventPayload(AjaxRequestTarget target,String field13Code) {
		super(target);
		this.field13Code=field13Code;
	}

	/**
	 * @return the field13Code
	 */
	public String getField13Code() {
		return field13Code;
	}

	/**
	 * @param field13Code the field13Code to set
	 */
	public void setField13Code(String field13Code) {
		this.field13Code = field13Code;
	}
}
