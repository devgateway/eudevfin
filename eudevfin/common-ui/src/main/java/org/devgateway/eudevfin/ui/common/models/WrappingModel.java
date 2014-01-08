/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.models;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;

/**
 * Abstract model that allows the custom converting models (eg: {@link org.devgateway.eudevfin.dim.core.models.BigMoneyModel}, etc)
 * to proxy a {@link RWComponentPropertyModel} without intercepting the wrapOnAssignment call,
 * but passing it through. This allows the {@link org.apache.wicket.model.CompoundPropertyModel} to do it's work and bind the parent model
 * to the RWComponentPropertyModel
 *
 * @author aartimon
 * @since 29/11/13
 */
public abstract class WrappingModel<T, V> implements IModel<T>, IComponentAssignedModel<T>, IWrapModel<T> {

    protected IWrapModel<V> originalModel;
    private IWrapModel<V> originalModelNotWrapped;

    public WrappingModel(IWrapModel<V> originalModel) {
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
