package org.devgateway.eudevfin.importing.transaction.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.ui.common.WicketNavbarComponentInitializer;
import org.devgateway.eudevfin.ui.common.components.RepairedNavbarDropDownButton;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuDivider;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuHeader;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarDropDownButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.button.DropDownAutoOpen;

public class NavbarInitializer {
	@WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.LEFT,order=5)
	public static Component newImporttButton(final Page page) {

		final NavbarDropDownButton exportMenu = new RepairedNavbarDropDownButton(new StringResourceModel(
				"navbar.transactions.import", page, null, null)) {

			@Override
			public boolean isActive(final Component item) {
				return false;
			}

			@Override
			protected List<AbstractLink> newSubMenuButtons(final String buttonMarkupId) {
				final List<AbstractLink> list = new ArrayList<>();

				list.add(new MenuHeader(new StringResourceModel("navbar.import.header", this, null, null)));
				list.add(new MenuDivider());

				list.add((AbstractLink) new MenuBookmarkablePageLink<ImportTransactionPage>(
						ImportTransactionPage.class, new StringResourceModel("navbar.transactions.import-transactions",
								this, null, null)).setEnabled(true));

				return list;
			}

		};
		exportMenu.setIconType(IconType.arrowup);
		exportMenu.add(new DropDownAutoOpen());
		MetaDataRoleAuthorizationStrategy.authorize(exportMenu, Component.RENDER, AuthConstants.Roles.ROLE_USER);
		return exportMenu;
	}
}
