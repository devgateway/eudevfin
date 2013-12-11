/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.core.models;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;

/**
 * @author aartimon
 * @since 29/11/13
 */
abstract class WrappingModel<T, V> implements IModel<T>, IComponentAssignedModel<T>, IWrapModel<T> {

    IWrapModel<V> originalModel;
    private IWrapModel<V> originalModelNotWrapped;

    WrappingModel(IWrapModel<V> originalModel) {
        this.originalModelNotWrapped = originalModel;
        this.originalModel = originalModel;
    }

    @Override
    public void detach() {
        originalModel.detach();
    }

    @SuppressWarnings("unchecked")
    @Override
    public IWrapModel<T> wrapOnAssignment(Component component) {
        originalModel = ((IComponentAssignedModel<V>) originalModelNotWrapped).wrapOnAssignment(component);
        return WrappingModel.this;
    }

    @Override
    public IModel<?> getWrappedModel() {
        return originalModel;
    }
}
