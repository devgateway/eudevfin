package org.devgateway.eudevfin.ui.common.components;

import org.apache.wicket.markup.html.basic.Label;

/**
 * Interface defining a mode for
 * {@link org.apache.wicket.markup.html.panel.Panel} descendants that can have a
 * mode where they don't show editable form components but only read only
 * {@link Label} views of the attached models
 * 
 * @author mihai
 * 
 */
public interface PreviewableFormPanelAware {

	/**
	 * @return True if the underlying panel form is in preview mode
	 */
	public boolean isFormInPreview();
}
