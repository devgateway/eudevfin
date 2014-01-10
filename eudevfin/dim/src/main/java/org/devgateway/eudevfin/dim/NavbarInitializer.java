package org.devgateway.eudevfin.dim;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.dim.pages.HomePage;
import org.devgateway.eudevfin.dim.pages.admin.EditUserPage;
import org.devgateway.eudevfin.dim.pages.admin.ListUsersPage;
import org.devgateway.eudevfin.dim.pages.transaction.crs.TransactionPage;
import org.devgateway.eudevfin.dim.pages.transaction.custom.CustomTransactionPage;
import org.devgateway.eudevfin.ui.common.Constants;
import org.devgateway.eudevfin.ui.common.WicketNavbarComponentInitializer;
import org.devgateway.eudevfin.ui.common.pages.LogoutPage;
import org.devgateway.eudevfin.ui.common.temporary.SB;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.DropDownSubMenu;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuDivider;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuHeader;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarDropDownButton;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.core.settings.ITheme;
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

	

	@WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.LEFT)
	public static Component newHomePageNavbarButton(Page page) {
		NavbarButton<HomePage> homePageNavbarButton = new NavbarButton<HomePage>(page.getApplication().getHomePage(), new StringResourceModel("navbar.home", page, null, null)).setIconType(IconType.home);
		MetaDataRoleAuthorizationStrategy.authorize(homePageNavbarButton, Component.RENDER, AuthConstants.Roles.ROLE_USER);
		return homePageNavbarButton;
	}
	
	
	
	@WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.LEFT)
	public static Component newTransactionNavbarButton(final Page page) {
		   NavbarDropDownButton navbarDropDownButton = new NavbarDropDownButton(new StringResourceModel("navbar.newTransaction", page, null, null)) {
	            @Override
	            public boolean isActive(Component item) {
	                return false;
	            }

	            @Override
	            @SuppressWarnings("Convert2Diamond")
	            protected List<AbstractLink> newSubMenuButtons(String buttonMarkupId) {
	                List<AbstractLink> list = new ArrayList<>();
	                DropDownSubMenu bilateralOda = new DropDownSubMenu(new StringResourceModel("navbar.newTransaction.bilateralOda", this, null, null)) {
	                    @Override
	                    public boolean isActive(Component item) {
	                        return false;
	                    }

	                    @Override
	                    protected List<AbstractLink> newSubMenuButtons(String buttonMarkupId) {
	                        List<String> values = new ArrayList<>();
	                        values.add(SB.BILATERAL_ODA_ADVANCED_QUESTIONNAIRE);
	                        values.add(SB.BILATERAL_ODA_CRS);
	                        values.add(SB.BILATERAL_ODA_FORWARD_SPENDING);

	                        return getTransactionLinks(values,page);
	                    }

	                };
	                bilateralOda.setIconType(IconType.resizehorizontal);
	                list.add(bilateralOda);

	                DropDownSubMenu multilateralOda = new DropDownSubMenu(Model.of("Multilateral ODA")) {
	                    @Override
	                    public boolean isActive(Component item) {
	                        return false;
	                    }

	                    @Override
	                    protected List<AbstractLink> newSubMenuButtons(String buttonMarkupId) {
	                        List<String> values = new ArrayList<>();
	                        values.add(SB.MULTILATERAL_ODA_ADVANCED_QUESTIONNAIRE);
	                        values.add(SB.MULTILATERAL_ODA_CRS);

	                        return getTransactionLinks(values,page);
	                    }
	                };
	                multilateralOda.setIconType(IconType.fullscreen);
	                list.add(multilateralOda);

	                DropDownSubMenu nonOda = new DropDownSubMenu(Model.of("non-ODA")) {
	                    @Override
	                    protected List<AbstractLink> newSubMenuButtons(String buttonMarkupId) {
	                        List<String> values = new ArrayList<>();
	                        values.add(SB.NON_ODA_OOF_NON_EXPORT);
	                        values.add(SB.NON_ODA_OOF_EXPORT);
	                        values.add(SB.NON_ODA_PRIVATE_GRANTS);
	                        values.add(SB.NON_ODA_PRIVATE_MARKET);
	                        values.add(SB.NON_ODA_OTHER_FLOWS);

	                        return getTransactionLinks(values,page);
	                    }
	                };
	                nonOda.setIconType(IconType.random);
	                list.add(nonOda);

	                return list;
	            }
	        };
	        navbarDropDownButton.setIconType(IconType.plus);
	        navbarDropDownButton.add(new DropDownAutoOpen());				
        MetaDataRoleAuthorizationStrategy.authorize(navbarDropDownButton, Component.RENDER, AuthConstants.Roles.ROLE_USER);
        return navbarDropDownButton;
	}
	
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
	            list.add(new MenuBookmarkablePageLink<TransactionPage>(ListUsersPage.class, null, new StringResourceModel("navbar.admin.users", this, null, null)));
	            return list;
			}

  
        };
        navbarDropDownButton.setIconType(IconType.plus);
        navbarDropDownButton.add(new DropDownAutoOpen());        
        MetaDataRoleAuthorizationStrategy.authorize(navbarDropDownButton, Component.RENDER, AuthConstants.Roles.ROLE_SUPERVISOR);
        
        return navbarDropDownButton;
    }


	@WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.RIGHT)
	public static Component newThemesNavbarButton(final Page page) {
	        return new NavbarDropDownButton(Model.of("Themes")) {
	            @Override
	            public boolean isActive(Component item) {
	                return false;
	            }

	            @SuppressWarnings("Convert2Diamond")
	            @Override
	            protected List<AbstractLink> newSubMenuButtons(final String buttonMarkupId) {
	                final List<AbstractLink> subMenu = new ArrayList<AbstractLink>();
	                subMenu.add(new MenuHeader(Model.of("all available themes:")));
	                subMenu.add(new MenuDivider());

	                final IBootstrapSettings settings = Bootstrap.getSettings(getApplication());
	                final List<ITheme> themes = settings.getThemeProvider().available();

	                for (final ITheme theme : themes) {
	                    PageParameters params = new PageParameters();
	                    params.set("theme", theme.name());

	                    subMenu.add(new MenuBookmarkablePageLink<Page>(page.getPageClass(), params, Model.of(theme.name())));
	                }

	                return subMenu;
	            }
	        }.setIconType(IconType.book);
	    }
	

	@WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.RIGHT)
	public static Component newLanguageNavbarButton(final Page page) {
	        NavbarDropDownButton languageDropDown = new NavbarDropDownButton(new StringResourceModel("navbar.lang", page, null, null)) {
	            private static final long serialVersionUID = 2866997914075956070L;

	            @Override
	            public boolean isActive(Component item) {
	                return false;
	            }

	            @SuppressWarnings("Convert2Diamond")
	            @Override
	            protected List<AbstractLink> newSubMenuButtons(String buttonMarkupId) {
	                List<AbstractLink> list = new ArrayList<>();
	                list.add(new MenuHeader(new StringResourceModel("navbar.lang.header", this, null, null)));
	                list.add(new MenuDivider());


	                //TODO: get available languages
	                final List<Locale> langs = new ArrayList<>();
	                langs.add(new Locale("en"));
	                langs.add(new Locale("ro"));

	                for (Locale l : langs) {
	                    PageParameters params = new PageParameters();
	                    params.set(Constants.LANGUAGE_PAGE_PARAM, l.getLanguage());
	                    list.add(new MenuBookmarkablePageLink<Page>(page.getPageClass(), params, Model.of(l.getDisplayName())));
	                }

	                return list;
	            }
	        };
	        languageDropDown.setIconType(IconType.flag);
	        languageDropDown.add(new DropDownAutoOpen());
	        return languageDropDown;
	    }
	
	
    @SuppressWarnings("Convert2Diamond")
    public static List<AbstractLink> getTransactionLinks(List<String> values, Page page) {
        List<AbstractLink> list = new ArrayList<>();
        for (String item : values) {
            PageParameters params = new PageParameters();
            params.set(Constants.TRANSACTION_TYPE, item);
            list.add(new MenuBookmarkablePageLink<TransactionPage>(CustomTransactionPage.class, params, new StringResourceModel("navbar.newTransaction." + item, page, null, null)));
        }
        return list;
    }

    
	@WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.RIGHT)
	public static Component logoutPageNavbarButton(final Page page) {
		 NavbarButton<LogoutPage> logoutPageNavbarButton = new NavbarButton<LogoutPage>(LogoutPage.class, new StringResourceModel("navbar.logout", page, null, null)).setIconType(IconType.off);
	        MetaDataRoleAuthorizationStrategy.authorize(logoutPageNavbarButton, Component.RENDER, AuthConstants.Roles.ROLE_USER);
	     return logoutPageNavbarButton;        
	}
	

}
