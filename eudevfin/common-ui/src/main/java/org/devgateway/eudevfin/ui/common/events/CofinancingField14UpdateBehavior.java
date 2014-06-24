/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.events;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;

/**
 * Cofinancing Section should be greyed out if i choose 14.a=no
 * @author mihai
 */
public class CofinancingField14UpdateBehavior extends UpdateEventBehavior<Field14aChangedEventPayload> {

	private static final long serialVersionUID = 7439599381383513518L;

	public CofinancingField14UpdateBehavior() {
		super(Field14aChangedEventPayload.class);
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
		Field14aChangedEventPayload eventPayload = getEventPayload(event);
		if(!eventPayload.getField14Code()) {
			component.setEnabled(false);
			component.setDefaultModelObject(null);
		}
		else
			component.setEnabled(true);
	}

}
