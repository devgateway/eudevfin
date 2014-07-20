/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.ui.common;

/**
 * A read-write model that references a property by name on the current or inherited model
 * @see org.apache.wicket.model.ComponentPropertyModel
 * @author aartimon
 * @since 12/11/13
 */

import org.apache.wicket.Component;
import org.apache.wicket.core.util.lang.PropertyResolver;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;

public class RWComponentPropertyModel<T>
        implements
        IComponentAssignedModel<T>, IWrapModel<T> {
    private static final long serialVersionUID = 1L;

    /**
     * Name of property to read
     */
    protected final String propertyName;

    /**
     * @param propertyName The name of the property to reference
     */
    public RWComponentPropertyModel(final String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * {@inheritDoc AbstractReadOnlyModel#getObject}
     *
     * @see org.apache.wicket.model.AbstractReadOnlyModel#getObject()
     */
    @Override
    public T getObject() {
        throw new IllegalStateException("Wrapper should have been called");
    }

    /**
     * {@inheritDoc AbstractReadOnlyModel#setObject}
     */
    @Override
    public void setObject(T object) {
        throw new IllegalStateException("Wrapper should have been called");
    }

    /**
     * {@inheritDoc AbstractReadOnlyModel#wrapOnAssignment}
     */
    @Override
    public IWrapModel<T> wrapOnAssignment(final Component component) {
        return new AssignmentWrapper<T>(component, propertyName);
    }

    @Override
    public void detach() {
        //throw new IllegalStateException("Wrapper should have been called3");
    }

    @Override
    public IModel<?> getWrappedModel() {
        throw new IllegalStateException("Wrapper should have been called");
    }

    /**
     * Implement a read/write {@link IWrapModel}
     *
     * @param <P> The Model Object
     */
    protected class AssignmentWrapper<P> implements IWrapModel<P> {
        private static final long serialVersionUID = 1L;

        private final Component component;

        private final String propertyName;

        AssignmentWrapper(final Component component, final String propertyName) {
            this.component = component;
            this.propertyName = propertyName;
        }

        /**
         * @see org.apache.wicket.model.IWrapModel#getWrappedModel()
         */
        @Override
        public IModel<T> getWrappedModel() {
            return RWComponentPropertyModel.this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public P getObject() {
            return (P) PropertyResolver.getValue(propertyName, component.getParent()
                    .getInnermostModel()
                    .getObject());
        }

        @Override
        public void setObject(P object) {
            PropertyResolver.<P>setValue(propertyName, component.getParent()
                    .getInnermostModel()
                    .getObject(), object, null);
        }

        @Override
        public void detach() {
            RWComponentPropertyModel.this.detach();
        }
    }
}
