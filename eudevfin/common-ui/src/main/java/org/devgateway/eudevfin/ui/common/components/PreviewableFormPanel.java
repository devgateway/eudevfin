/**
 * 
 */
package org.devgateway.eudevfin.ui.common.components;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author mihai
 * 
 */
public abstract class PreviewableFormPanel extends Panel implements PreviewableFormPanelAware {

	private static final long serialVersionUID = -4413222821613083939L;

	/**
	 * @see Panel#Panel(String)
	 */
	public PreviewableFormPanel(String id) {
		super(id);
	}

	/**
	 * @see Panel#Panel(String, IModel)
	 */
	public PreviewableFormPanel(String id, IModel<?> model) {
		super(id, model);
	}

	@Override
	public boolean isFormInPreview() {
		MarkupContainer parent = this.getParent();

		while (parent != null && !(parent instanceof PreviewableFormPanelAware))
			parent = parent.getParent();

		if (parent instanceof PreviewableFormPanelAware)
			return ((PreviewableFormPanelAware) parent).isFormInPreview();
		else
			return false;
	}
}
