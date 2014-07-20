/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.ui.common.events;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.event.IEvent;

/**
 * Generic behavior that adds the current component to the ajax target for refreshing when a
 * certain event type reaches the current component
 *
 * @author aartimon
 * @since 03/12/13
 */
public class UpdateEventBehavior<T> extends Behavior {

	private static final long serialVersionUID = 631185570134878397L;
	protected Class<T> triggerEvent;
    protected Component parent;

    protected UpdateEventBehavior(Class<T> triggerEvent) {
        this.triggerEvent = triggerEvent;
    }

    @Override
    public void onEvent(Component component, IEvent<?> event) {
        if (event.getPayload().getClass().isAssignableFrom(triggerEvent)) {
            AbstractAjaxUpdateEventPayload ajaxUpdateEvent = (AbstractAjaxUpdateEventPayload) event.getPayload();
            AjaxRequestTarget target = ajaxUpdateEvent.getTarget();
            updateComponents(target);
            onEventExtra(component, event);
        }
    }

    /**
     * Override this method to inhibit updating the parent component or to add other components that need updating
     *
     * @param target the target for the current ajax request
     */
    protected void updateComponents(AjaxRequestTarget target) {
        target.add(parent);
    }

    /**
     * Override this method to extend the behavior of onEvent without the extra checkss
     *
     * @param component current component
     * @param event     caught event
     */
    protected void onEventExtra(Component component, IEvent<?> event) {
    }
    
    /**
     * Helper method for {@link #onEventExtra(Component, IEvent)} descendants
     * @param event
     * @return
     */
    @SuppressWarnings("unchecked")
	protected T getEventPayload(IEvent<?> event) {
    	return (T) event.getPayload();
    }

    @Override
    public void bind(Component component) {
        synchronized (this) { //make it thread safe
            if (parent == null) {
                parent = component;
                parent.setOutputMarkupId(true);
            } else {
                throw new AssertionError("Don't use the same behavior on different components");
            }
        }

    }

    public static <T> UpdateEventBehavior of(Class<T> triggerEvent) {
        return new UpdateEventBehavior<>(triggerEvent);
    }
}
