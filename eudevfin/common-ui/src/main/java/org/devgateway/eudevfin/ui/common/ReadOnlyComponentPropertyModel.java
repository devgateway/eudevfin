/**
 * 
 */
package org.devgateway.eudevfin.ui.common;

import org.apache.wicket.Component;
import org.apache.wicket.model.IWrapModel;

/**
 * @author mihai
 * Dumber {@link RWComponentPropertyModel} that only reads stuff, never writes
 */
public class ReadOnlyComponentPropertyModel<T> extends RWComponentPropertyModel<T> {

	private static final long serialVersionUID = 5292642542369587312L;

	protected class ReadOnlyAssignmentWrapper<P> extends RWComponentPropertyModel<T>.AssignmentWrapper<P> {

		private static final long serialVersionUID = 1450198447041490306L;

		ReadOnlyAssignmentWrapper(Component component,
				String propertyName) {
			super(component, propertyName);
		}
		
		@Override
		public void setObject(P object) {		
		}
		
	}
	
	public ReadOnlyComponentPropertyModel(String propertyName) {
		super(propertyName);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public IWrapModel<T> wrapOnAssignment(Component component) {
		return new ReadOnlyAssignmentWrapper<T>(component, propertyName);
	}
	

}
