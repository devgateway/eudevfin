/**
 * 
 */
package org.devgateway.eudevfin.dim.core.components;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.ControlGroup;

/**
 * @author mihai
 *
 */
public abstract class BootstrapSubmitButton extends IndicatingAjaxButton {

	/**
	 * @param id
	 */
	public BootstrapSubmitButton(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 */
	public BootstrapSubmitButton(String id, IModel<String> model) {
		super(id, model);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param form
	 */
	public BootstrapSubmitButton(String id, Form<?> form) {
		super(id, form);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param form
	 */
	public BootstrapSubmitButton(String id, IModel<String> model, Form<?> form) {
		super(id, model, form);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected abstract void onSubmit(AjaxRequestTarget target, Form<?> form);
	
	/**
	 * Override this to implement customized visitation code for components of the form
	 * @param component
	 * @param visit
	 */
	public void componentVisitor(AjaxRequestTarget target,FormComponent component,
            IVisit<Void> visit) {	
		if (!component.isValid()) {
			Component parent = component
					.findParent(ControlGroup.class);
			target.add(parent);
		}
	}
	
	@Override
	protected void onError(final AjaxRequestTarget target, Form<?> form) {
		form.visitChildren(FormComponent.class,
				new IVisitor<FormComponent, Void>() {
					@Override
					public void component(FormComponent component,
							IVisit<Void> visit) {
						componentVisitor(target,component, visit);
					}
				});
	}
}
