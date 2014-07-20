/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.ui.common.models;

import org.apache.wicket.model.IModel;

/**
 * Simple proxy model that passes the requests to the base model
 * Use {@link ProxyModel#replaceModel(org.apache.wicket.model.IModel)} to change the underlying model
 *
 * @author aartimon
 * @since 16/12/13
 */
public class ProxyModel<T> implements IModel<T> {
    private IModel<T> model;

    public ProxyModel(IModel<T> model) {
        this.model = model;
    }

    @Override
    public T getObject() {
        return model.getObject();
    }

    @Override
    public void setObject(T object) {
        model.setObject(object);
    }

    @Override
    public void detach() {
        model.detach();
    }

    public void replaceModel(IModel<T> model) {
        this.model = model;
    }
}
