/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.ui.common.events;

import java.util.Calendar;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;

/**
 * @author mihai
 */
public class DisbursementCurrentYearUpdateBehavior extends UpdateEventBehavior<ReportingYearChangedEventPayload> {


	private static final long serialVersionUID = 7541498995034051252L;

	public DisbursementCurrentYearUpdateBehavior() {
		super(ReportingYearChangedEventPayload.class);
	}

	@Override
	protected void updateComponents(AjaxRequestTarget target) {
		if (parent.getParent().isVisibleInHierarchy())
			if(target!=null) target.add(parent);
	}

	@Override
	protected void onEventExtra(Component component, IEvent<?> event) {
		if (!component.getParent().isVisibleInHierarchy())
			return;
		ReportingYearChangedEventPayload eventPayload = getEventPayload(event);
		if (Calendar.getInstance().get(Calendar.YEAR)==eventPayload.getReportingYear()) {
			component.setEnabled(false);
			component.setDefaultModelObject(null);
		}
		else
			component.setEnabled(true);
	}

}
