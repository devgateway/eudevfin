/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.core.events;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * @author aartimon
 * @since 03/12/13
 */
public class CurrencyChangedEvent extends AbstractAjaxUpdateEvent {
    public CurrencyChangedEvent(AjaxRequestTarget target) {
        super(target);
    }
}