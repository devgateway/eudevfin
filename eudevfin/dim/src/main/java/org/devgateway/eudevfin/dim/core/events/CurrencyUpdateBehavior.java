/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.core.events;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.model.IModel;

/**
 * Extension of the {@link org.devgateway.eudevfin.dim.core.events.UpdateEventBehavior} that doesn't refresh
 * the component on which the event was triggered, but forces the component to update it's internal model
 * Used mainly to update the currency field inside the {@link org.joda.money.BigMoney} entities
 *
 * @author aartimon
 * @since 03/12/13
 */
public class CurrencyUpdateBehavior extends UpdateEventBehavior<CurrencyChangedEvent> {
    private static Logger logger = Logger.getLogger(CurrencyUpdateBehavior.class);

    public CurrencyUpdateBehavior() {
        super(CurrencyChangedEvent.class);
    }

    @Override
    protected void updateComponents(AjaxRequestTarget target) {
        //inhibit updating the component, we just need the models updated
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onEventExtra(Component component, IEvent<?> event) {
        //force the model to update
        IModel<Object> model = (IModel<Object>) component.getDefaultModel();
        model.setObject(model.getObject());
    }


}
