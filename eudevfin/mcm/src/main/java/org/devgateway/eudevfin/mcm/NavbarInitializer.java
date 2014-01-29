package org.devgateway.eudevfin.mcm;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.mcm.pages.ListPersistedUserGroupsPage;
import org.devgateway.eudevfin.mcm.pages.ListPersistedUsersPage;
import org.devgateway.eudevfin.ui.common.WicketNavbarComponentInitializer;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarDropDownButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.button.DropDownAutoOpen;

/**
 * Class holding static methods that initialize the wicket {@link Navbar} components.
 * 
 * @see WicketNavbarComponentInitializer
 * @see org.devgateway.eudevfin.ui.common.pages.HeaderFooter
 * @author mihai
 *
 */
public final class NavbarInitializer {
	
	@WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.RIGHT)
	public static Component newAdminNavbarButton(Page page) {
        NavbarDropDownButton navbarDropDownButton = new NavbarDropDownButton(new StringResourceModel("navbar.admin", page, null, null)) {
            @Override
            public boolean isActive(Component item) {
                return false;
            }

			@Override
			protected List<AbstractLink> newSubMenuButtons(String buttonMarkupId) {
	            List<AbstractLink> list = new ArrayList<>();
	            list.add(new MenuBookmarkablePageLink<ListPersistedUsersPage>(ListPersistedUsersPage.class, null, new StringResourceModel("navbar.admin.users", this, null, null)));
	            list.add(new MenuBookmarkablePageLink<ListPersistedUserGroupsPage>(ListPersistedUserGroupsPage.class, null, new StringResourceModel("navbar.admin.groups", this, null, null)));
	            return list;
			}

  
        };
        navbarDropDownButton.setIconType(IconType.plus);
        navbarDropDownButton.add(new DropDownAutoOpen());        
        MetaDataRoleAuthorizationStrategy.authorize(navbarDropDownButton, Component.RENDER, AuthConstants.Roles.ROLE_SUPERVISOR);
        
        return navbarDropDownButton;
    }


	

}
