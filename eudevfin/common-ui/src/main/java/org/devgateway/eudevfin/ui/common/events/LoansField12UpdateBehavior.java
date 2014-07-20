/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.ui.common.events;

import java.util.regex.Pattern;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;

/**
 * Bilateral and Multilateral ODA CRS++ and FSS forms all fields related to the
 * section 'For Loans only' they need to be grayed IF in the field12. Type of
 * Finance the following code and its subcode is chosen: 100 Grant
 * 
 * @author mihai
 */
public class LoansField12UpdateBehavior extends UpdateEventBehavior<Field12ChangedEventPayload> {

	private static final long serialVersionUID = 8972493821857942252L;
	private Pattern pattern = Pattern.compile("1[0-9]{2}");

	public LoansField12UpdateBehavior() {
		super(Field12ChangedEventPayload.class);
	}

	@Override
	protected void updateComponents(AjaxRequestTarget target) {
		if (parent.getParent().getParent().getParent().getParent().isVisibleInHierarchy())
			if(target!=null) target.add(parent);
	}

	@Override
	protected void onEventExtra(Component component, IEvent<?> event) {
		if (!component.getParent().getParent().getParent().getParent().isVisibleInHierarchy())
			return;
		Field12ChangedEventPayload eventPayload = getEventPayload(event);
		if (pattern.matcher(eventPayload.getField12Code()).matches()) {
			component.setEnabled(false);
			component.setDefaultModelObject(null);
		}
		else
			component.setEnabled(true);
	}

}
