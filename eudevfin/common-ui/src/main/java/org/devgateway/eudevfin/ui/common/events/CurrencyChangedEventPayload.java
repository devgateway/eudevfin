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
 * Event triggered when the user selects a currency in the transaction form
 * Each amount field that uses the current currency dropdown needs to update it's internal model
 *
 * @author aartimon
 * @since 03/12/13
 */
public class CurrencyChangedEventPayload extends AbstractAjaxUpdateEventPayload {
    public CurrencyChangedEventPayload(AjaxRequestTarget target) {
        super(target);
    }
}
