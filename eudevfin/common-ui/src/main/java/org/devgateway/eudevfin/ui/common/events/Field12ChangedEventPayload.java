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
 * @see LoansField12UpdateBehavior
 */
public class Field12ChangedEventPayload extends AbstractAjaxUpdateEventPayload {
	private String field12Code;
	
	public Field12ChangedEventPayload(AjaxRequestTarget target,String field12Code) {
		super(target);
		this.field12Code=field12Code;
	}

	/**
	 * @return the field12Code
	 */
	public String getField12Code() {
		return field12Code;
	}

	/**
	 * @param field12Code the field12Code to set
	 */
	public void setField12Code(String field12Code) {
		this.field12Code = field12Code;
	}


}
