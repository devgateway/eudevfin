package org.devgateway.eudevfin.ui.common;

import org.apache.wicket.Component;
import org.apache.wicket.model.ComponentDetachableModel;
import org.apache.wicket.model.IComponentAssignedModel;

/**
 * Simple model compliant with {@link IComponentAssignedModel} even if we dont
 * need to know the {@link Component}
 * 
 * @author mihai
 * 
 * @param <T>
 */
public class LocalComponentDetachableModel<T> extends ComponentDetachableModel<T> {

	protected transient T t;

	@Override
	protected T getObject(Component component) {
		return t;
	}

	/**
	 * no state between refreshes
	 */
	@Override
	protected void attach() {
		t = null;
	}

	/**
	 * we receive the component and we really don't care much...
	 */
	@Override
	protected void setObject(Component component, T object) {
		t = object;
	}
};