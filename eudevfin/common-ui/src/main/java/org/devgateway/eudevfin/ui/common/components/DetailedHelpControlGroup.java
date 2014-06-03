/**
 * 
 */
package org.devgateway.eudevfin.ui.common.components;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.ControlGroup;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.util.Components;

/**
 * @author mihai Extends the {@link ControlGroup} to add a detailed help message
 *         and a plus {@link Icon} When clicked, the plus icon shows the
 *         detailed help
 */
public class DetailedHelpControlGroup extends ControlGroup implements PreviewableFormPanelAware  {

	private static final long serialVersionUID = 8319422629214281685L;
	protected final Component detailedHelp;
	protected final Icon detailedHelpIcon;
	protected boolean preview;

	/**
	 * @param id
	 * @param label
	 * @param help
	 * @param detailedHelp
	 */
	public DetailedHelpControlGroup(String id, boolean preview, IModel<String> label, IModel<String> help, IModel<String> detailedHelp) {
		super(id, label, help);
		this.detailedHelp = newHelpLabel("detailedHelp", detailedHelp).setOutputMarkupId(true).setVisible(false);
		this.detailedHelpIcon = (Icon) new Icon("detailedHelpIcon", IconType.plussign);
		this.preview=preview;
		this.detailedHelpIcon.add(new AjaxEventBehavior("onclick") {
			private static final long serialVersionUID = 8263043282265678656L;

			protected void onEvent(AjaxRequestTarget target) {				
				DetailedHelpControlGroup.this.detailedHelp.setVisible(!DetailedHelpControlGroup.this.detailedHelp
						.isVisible());
				DetailedHelpControlGroup.this.detailedHelpIcon.setType(DetailedHelpControlGroup.this.detailedHelp
						.isVisible() ? IconType.minussign : IconType.plussign);
				target.add(DetailedHelpControlGroup.this);
			}
		});

		addToBorder(this.detailedHelp);
		addToBorder(this.detailedHelpIcon);
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		Components.hideIfModelIsEmpty(detailedHelp);
		if(isFormInPreview()) {
			detailedHelp.setVisible(false);
			
		}
		detailedHelpIcon.setVisible((isFormInPreview() || Strings.isEmpty((String) detailedHelp.getDefaultModelObject())) ? false : true);
	}

	@Override
	public boolean isFormInPreview() {
		return preview;
	}

}
