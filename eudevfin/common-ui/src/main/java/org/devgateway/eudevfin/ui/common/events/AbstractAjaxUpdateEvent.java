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
 * Abstract Event class to be used for building specific event types
 * Will be used as payload for the events send using Wickets internal event system
 * Each event will store the AjaxRequestTarget of the ajax user action that generated the event
 *
 * @author aartimon
 * @since 03/12/13
 */
public abstract class AbstractAjaxUpdateEvent {
    private AjaxRequestTarget target;

    AbstractAjaxUpdateEvent(AjaxRequestTarget target) {
        this.target = target;
    }

    public AjaxRequestTarget getTarget() {
        return target;
    }
}
