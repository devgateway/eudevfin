/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.ui;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.ui.common.WicketNavbarComponentInitializer;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;

/**
 * @author Alex
 *
 */
public final class NavbarInitializer {
	@WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.RIGHT)
	public static Component newExportSpreadSheetButton(final Page page) {
		final NavbarButton<ExportSpreadsheetsPage> exportSpreadsheetButton = new NavbarButton<ExportSpreadsheetsPage>(
				ExportSpreadsheetsPage.class, new StringResourceModel(
						"navbar.spreadsheets.export", page, null, null))
				.setIconType(IconType.file);
		
		
		
		MetaDataRoleAuthorizationStrategy.authorize(exportSpreadsheetButton,
				Component.RENDER, AuthConstants.Roles.ROLE_USER);
		return exportSpreadsheetButton;
	}
}
