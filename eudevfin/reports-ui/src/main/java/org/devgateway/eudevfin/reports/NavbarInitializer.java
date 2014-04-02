package org.devgateway.eudevfin.reports;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.DropDownSubMenu;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarDropDownButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.button.DropDownAutoOpen;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.reports.ui.pages.CustomDashboardsCountrySector;
import org.devgateway.eudevfin.reports.ui.pages.ReportsExport;
import org.devgateway.eudevfin.reports.ui.pages.ReportsPage;
import org.devgateway.eudevfin.ui.common.WicketNavbarComponentInitializer;
import org.devgateway.eudevfin.ui.common.components.RepairedNavbarDropDownButton;

import java.util.ArrayList;
import java.util.List;

public final class NavbarInitializer {

//	@WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.LEFT)
//	public static Component newReportsPageNavbarButton(Page page) {
//		NavbarButton<ReportsPage> reportsPageNavbarButton = new NavbarButton<ReportsPage>(ReportsPage.class, new StringResourceModel("navbar.reports", page, null, null)).setIconType(IconType.thlist);
//        MetaDataRoleAuthorizationStrategy.authorize(reportsPageNavbarButton, Component.RENDER, AuthConstants.Roles.ROLE_USER);
//        return reportsPageNavbarButton;
//	}
//	
	
	public static class SaikuRedirectPage extends RedirectPage {
		private static final long serialVersionUID = -750983217518258464L;
		
		public SaikuRedirectPage() {
			super(WebApplication.get().getServletContext().getContextPath()+"/saikuui");
		}

	}
	
	
	@WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.LEFT,order=3)
	public static Component newReportsNavbarMenu(Page page) {
		NavbarDropDownButton navbarDropDownButton = new RepairedNavbarDropDownButton(
				new StringResourceModel("navbar.reports", page, null, null)) {
			@Override
			public boolean isActive(Component item) {
				return false;
			}

			@Override
			protected List<AbstractLink> newSubMenuButtons(String buttonMarkupId) {
				List<AbstractLink> list = new ArrayList<>();
				list.add(new MenuBookmarkablePageLink<ReportsPage>(
						ReportsPage.class, null, new StringResourceModel(
								"navbar.dashboard", this, null, null))
						.setIconType(IconType.picture));

				DropDownSubMenu exportReports = new DropDownSubMenu(new StringResourceModel("navbar.reports.export", this, null, null)) {
					@Override
					public boolean isActive(Component item) {
						return false;
					}

					@Override
					protected List<AbstractLink> newSubMenuButtons(String buttonMarkupId) {
						List<AbstractLink> list = new ArrayList<>();

						//TODO: Wrap creation of links with something similar to TransactionPage.getTransactionLinks()
						PageParameters paramsAQ = new PageParameters();
						paramsAQ.set("reportType", "aq");
						list.add(new MenuBookmarkablePageLink<ReportsExport>(ReportsExport.class, paramsAQ, new StringResourceModel("navbar.reports.export.aq", this, null, null)));

						PageParameters paramsDAC1 = new PageParameters();
						paramsDAC1.set("reportType", "dac1");
						list.add((AbstractLink) new MenuBookmarkablePageLink<ReportsExport>(ReportsExport.class, paramsDAC1, new StringResourceModel("navbar.reports.export.dac1", this, null, null)).setEnabled(false));

						PageParameters paramsDAC2 = new PageParameters();
						paramsDAC2.set("reportType", "dac2");
						list.add((AbstractLink) new MenuBookmarkablePageLink<ReportsExport>(ReportsExport.class, paramsDAC2, new StringResourceModel("navbar.reports.export.dac2", this, null, null)).setEnabled(false));

//						PageParameters paramsCRS = new PageParameters();
//						paramsCRS.set("reportType", "CRS");
//						list.add((AbstractLink) new MenuBookmarkablePageLink<ReportsExport>(ReportsExport.class, paramsCRS, new StringResourceModel("navbar.reports.export.crs", this, null, null)).setEnabled(false));
//
//						PageParameters paramsFSS = new PageParameters();
//						paramsFSS.set("reportType", "fss");
//						list.add((AbstractLink) new MenuBookmarkablePageLink<ReportsExport>(ExportS.class, paramsFSS, new StringResourceModel("navbar.reports.export.fss", this, null, null)).setEnabled(true));

			
						return list;
					}

				};
				exportReports.setIconType(IconType.resizehorizontal);
				list.add(exportReports);
				
				list.add((AbstractLink) new MenuBookmarkablePageLink<CustomDashboardsCountrySector>(
                        CustomDashboardsCountrySector.class, null, new StringResourceModel(
								"navbar.customreports", this, null, null))
						.setIconType(IconType.file).setEnabled(true));

				
				list.add((AbstractLink) new MenuBookmarkablePageLink<ReportsPage>(
						SaikuRedirectPage.class, null, new StringResourceModel(
								"navbar.customreports", this, null, null))
						.setIconType(IconType.play).setEnabled(true));
				
/*				
				list.add(new MenuBookmarkablePageLink<ReportsExport>(
						ReportsExport.class, null, new StringResourceModel(
								"navbar.reports.export", this, null, null))
						.setIconType(IconType.thlist));			*/	
				return list;
			}

		};
		navbarDropDownButton.setIconType(IconType.thlarge);
		navbarDropDownButton.add(new DropDownAutoOpen());
		MetaDataRoleAuthorizationStrategy.authorize(navbarDropDownButton,
				Component.RENDER, AuthConstants.Roles.ROLE_USER);

		return navbarDropDownButton;
	}

}
