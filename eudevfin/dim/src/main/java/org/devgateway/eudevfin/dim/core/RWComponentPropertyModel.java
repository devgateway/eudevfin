package org.devgateway.eudevfin.dim.core;

/**
 * @author aartimon
 * @date 11/12/13
 */

import org.apache.wicket.Component;
import org.apache.wicket.core.util.lang.PropertyResolver;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;

public class RWComponentPropertyModel<T>
        implements
        IComponentAssignedModel<T>, IModel<T> {
    private static final long serialVersionUID = 1L;

    /**
     * Name of property to read
     */
    private final String propertyName;

    /**
     * Constructor
     *
     * @param propertyName The name of the property to reference
     */
    public RWComponentPropertyModel(final String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * @see org.apache.wicket.model.AbstractReadOnlyModel#getObject()
     */
    @Override
    public T getObject() {
        throw new IllegalStateException("Wrapper should have been called");
    }

    @Override
    public void setObject(T object) {
        throw new IllegalStateException("Wrapper should have been called2");
    }

    /**
     * @see org.apache.wicket.model.IComponentAssignedModel#wrapOnAssignment(org.apache.wicket.Component)
     */
    @Override
    public IWrapModel<T> wrapOnAssignment(final Component component) {
        return new AssignmentWrapper<T>(component, propertyName);
    }

    @Override
    public void detach() {
        //throw new IllegalStateException("Wrapper should have been called3");
    }

    /**
     * Wrapper used when assigning a ComponentPropertyModel to a component.
     *
     * @param <P> The Model Object
     */
    private class AssignmentWrapper<P> implements IWrapModel<P> {
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

        protected String propertyExpression() {
            return propertyName;
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
