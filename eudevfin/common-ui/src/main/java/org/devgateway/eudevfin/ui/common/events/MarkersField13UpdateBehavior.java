/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.events;

import java.util.regex.Pattern;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;

/**
 * 		CRS++ and FSS forms all policy and rio
 *      markers from field 20 to 31 they need to be grayed IF in the field13.
 *      Type of Aid the follwing codes and their subcode are chosen: E, G, H
 * 
 * @author mihai
 */
public class MarkersField13UpdateBehavior extends UpdateEventBehavior<Field13ChangedEventPayload> {

	private static final long serialVersionUID = 8972493821857942252L;
	private Pattern pattern = Pattern.compile("(E|G|H)([0-9]{2})*");

	public MarkersField13UpdateBehavior() {
		super(Field13ChangedEventPayload.class);
	}

	@Override
	protected void updateComponents(AjaxRequestTarget target) {
		if(parent.getParent().getParent().getParent().getParent().isVisibleInHierarchy()) 
			if(target!=null) target.add(parent);
	}

	@Override
	protected void onEventExtra(Component component, IEvent<?> event) {
		if(!component.getParent().getParent().getParent().getParent().isVisibleInHierarchy()) return;
		Field13ChangedEventPayload eventPayload = getEventPayload(event);		
		if (pattern.matcher(eventPayload.getField13Code()).matches()) {
			component.setEnabled(false);
			component.setDefaultModelObject(null);
		}
			
		else component.setEnabled(true);		  
	}

}
