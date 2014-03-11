/**
 * 
 */
package org.devgateway.eudevfin.ui.common.components;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Args;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.AlignmentBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.ButtonList;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.DropDownSubMenu;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarDropDownButton;

/**
 * @author Alex
 *
 */
public abstract class RepairedNavbarDropDownButton extends NavbarDropDownButton {

	public RepairedNavbarDropDownButton(IModel<String> model) {
		super(model);
		this.remove("dropdown-menu");
		WebMarkupContainer dropdownMenu = new WebMarkupContainer("dropdown-menu");
        dropdownMenu.add(new AlignmentBehavior(Model.of(AlignmentBehavior.Alignment.NONE)));
        add(dropdownMenu);
        dropdownMenu.add(this.repairedButtonList("buttons"));
//		try {
//			ButtonList repairedButtonList	= this.repairedButtonList(this.getMarkupId());
//			this.modifyPrivateField("buttonListView", repairedButtonList);
//		} catch (NoSuchFieldException | SecurityException
//				| IllegalArgumentException | IllegalAccessException e) {
//			e.printStackTrace();
//		}
	}
//	
//	private void modifyPrivateField(String fieldName, Object value) throws NoSuchFieldException, SecurityException,
//					IllegalArgumentException, IllegalAccessException {
//		Field f 	= DropDownButton.class.getDeclaredField(fieldName); //NoSuchFieldException
//		f.setAccessible(true);
//		f.set(this,value);
//	}

	private ButtonList repairedButtonList(final String markupId) {
        final ButtonList buttonList = new ButtonList(markupId, newSubMenuButtons(ButtonList.getButtonMarkupId())) {

			@Override
			protected void populateItem(ListItem<AbstractLink> item) {
				final AbstractLink link = item.getModelObject();

		        Args.isTrue(getButtonMarkupId().equals(link.getId()), "component id is invalid, please use ButtonList.getButtonMarkupId()");
		        if (link instanceof DropDownSubMenu) {
		            item.setRenderBodyOnly(true);
		        }
		        item.add(link);
			}
        	
        };
        buttonList.setRenderBodyOnly(true);

        return buttonList;
    }



}
