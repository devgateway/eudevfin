/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.dim.desktop.components.MessageNavbarButton;
import org.devgateway.eudevfin.dim.pages.AggregateTransactionsPage;
import org.devgateway.eudevfin.dim.pages.HomePage;
import org.devgateway.eudevfin.dim.pages.Messages;
import org.devgateway.eudevfin.dim.pages.transaction.crs.TransactionPage;
import org.devgateway.eudevfin.dim.pages.transaction.custom.CustomTransactionPage;
import org.devgateway.eudevfin.financial.service.MessageService;
import org.devgateway.eudevfin.ui.common.Constants;
import org.devgateway.eudevfin.ui.common.WicketNavbarComponentInitializer;
import org.devgateway.eudevfin.ui.common.components.RepairedNavbarDropDownButton;
import org.devgateway.eudevfin.ui.common.pages.HelpPage;
import org.devgateway.eudevfin.ui.common.pages.LogoutPage;
import org.devgateway.eudevfin.ui.common.temporary.SB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 * Class holding static methods that initialize the wicket {@link Navbar}
 * components.
 *
 * @author mihai
 * @see WicketNavbarComponentInitializer
 * @see org.devgateway.eudevfin.ui.common.pages.HeaderFooter
 */
@Service
public final class NavbarInitializer {

	public static class RecipientCountriesPdf extends RedirectPage {

		private static final long serialVersionUID = 8040944162385060540L;

		public RecipientCountriesPdf() {
			super(WebApplication.get().getServletContext().getContextPath()+"/files/RecipientCountries.pdf");
		}

	}


	public static class LitOfInternationalOrgs extends RedirectPage {

		private static final long serialVersionUID = -5373256603362316650L;

		public LitOfInternationalOrgs() {
			super(WebApplication.get().getServletContext().getContextPath()+"/files/2014-07-07-List-of-International-Organisations.xls");
		}

	}

	public static class DacCrsListCodes extends RedirectPage {
		private static final long serialVersionUID = 199848134105091614L;

		public DacCrsListCodes() {
			super(WebApplication.get().getServletContext().getContextPath()+"/files/2014-07-10-List-of-DAC-and-CRS-codes.xls");
		}

	}

	public static class OecdDacDirectives extends RedirectPage {
		private static final long serialVersionUID = -5930422636240394851L;
		public OecdDacDirectives() {
			super("http://www.oecd.org/dac/stats/documentupload/DCD-DAC(2013)15-FINAL-ENG.pdf ");
		}

	}

	public static class DacGlossary extends RedirectPage {
		private static final long serialVersionUID = -5548303652694029119L;
		public DacGlossary() {
			super("http://www.oecd.org/dac/dac-glossary.htm");
		}

	}


	@Autowired(required = true)
	public static MessageService mxService;

	public static class FeedbackUrl extends RedirectPage {
		private static final long serialVersionUID = -750983217518258464L;

		public FeedbackUrl() {
			super(
					"mailto:support-eudevfin@developmentgateway.org?subject=EU-DEVFIN%20Support%20Request&body="
							+ "Please write your feedback here and then press SEND. You may also change the email subject. The email will be automatically registered as a support ticket and our team will get back to you."
							+ "%0D%0AThanks"
							+ "%0D%0ADG Support%0D%0A"
							+ RequestCycle.get().getUrlRenderer()
							.renderFullUrl(((WebRequest) RequestCycle.get().getRequest()).getUrl()));
		}

	}

	@WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.LEFT, order = 0)
	public static Component newHomePageNavbarButton(final Page page) {
		final NavbarButton<HomePage> homePageNavbarButton = new NavbarButton<HomePage>(page.getApplication().getHomePage(),
				new StringResourceModel("navbar.home", page, null, null)).setIconType(IconType.home);
		MetaDataRoleAuthorizationStrategy.authorize(homePageNavbarButton, Component.RENDER,
				AuthConstants.Roles.ROLE_USER);
		return homePageNavbarButton;
	}

	@WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.RIGHT, order = 6)
	public static Component newMessagesNavbarButton(final Page page) {
		final NavbarButton<Messages> homePageNavbarButton = new MessageNavbarButton(Messages.class,
				Model.of(""));
		homePageNavbarButton.setIconType(IconType.comment);
		MetaDataRoleAuthorizationStrategy.authorize(homePageNavbarButton, Component.RENDER,
				AuthConstants.Roles.ROLE_USER);
		return homePageNavbarButton;
	}

	@WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.LEFT, order = 2)
	public static Component newTransactionNavbarButton(final Page page) {
		final NavbarDropDownButton navbarDropDownButton = new RepairedNavbarDropDownButton(new StringResourceModel(
				"navbar.newTransaction", page, null, null)) {
			@Override
			public boolean isActive(final Component item) {
				return false;
			}

			@Override
			@SuppressWarnings("Convert2Diamond")
			protected List<AbstractLink> newSubMenuButtons(final String buttonMarkupId) {
				final List<AbstractLink> list = new ArrayList<>();
				list.add(new MenuHeader(new StringResourceModel("navbar.newTransaction.header", this, null, null)));
				list.add(new MenuDivider());

				final DropDownSubMenu bilateralOda = new DropDownSubMenu(new StringResourceModel(
						"navbar.newTransaction.bilateralOda", this, null, null)) {
					@Override
					public boolean isActive(final Component item) {
						return false;
					}

					@Override
					protected List<AbstractLink> newSubMenuButtons(final String buttonMarkupId) {
						final List<String> values = new ArrayList<>();
						values.add(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
						values.add(SB.BILATERAL_ODA_CRS);
						values.add(SB.BILATERAL_ODA_FORWARD_SPENDING);

						return getTransactionLinks(values, page);
					}

				};
				bilateralOda.setIconType(IconType.resizehorizontal);
				list.add(bilateralOda);

				final DropDownSubMenu multilateralOda = new DropDownSubMenu(Model.of("Multilateral ODA")) {
					@Override
					public boolean isActive(final Component item) {
						return false;
					}

					@Override
					protected List<AbstractLink> newSubMenuButtons(final String buttonMarkupId) {
						final List<String> values = new ArrayList<>();
						values.add(SB.MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
						values.add(SB.MULTILATERAL_ODA_CRS);

						return getTransactionLinks(values, page);
					}
				};
				multilateralOda.setIconType(IconType.fullscreen);
				list.add(multilateralOda);

				final DropDownSubMenu nonOda = new DropDownSubMenu(new StringResourceModel("navbar.newTransaction.nonOda",
						this, null, null)) {
					@Override
					protected List<AbstractLink> newSubMenuButtons(final String buttonMarkupId) {
						final List<String> values = new ArrayList<>();
						values.add(SB.NON_ODA_OOF_NON_EXPORT);
						values.add(SB.NON_ODA_OOF_EXPORT);
						values.add(SB.NON_ODA_PRIVATE_GRANTS);
						values.add(SB.NON_ODA_PRIVATE_MARKET);
						values.add(SB.NON_ODA_OTHER_FLOWS);

						return getTransactionLinks(values, page);
					}
				};
				nonOda.setIconType(IconType.random);
				list.add(nonOda);

				final MenuBookmarkablePageLink<AggregateTransactionsPage> aggregateTransactions =
						new MenuBookmarkablePageLink<AggregateTransactionsPage>(AggregateTransactionsPage.class, null,new StringResourceModel("navbar.aggregate", page, null));
				aggregateTransactions.setIconType(IconType.resizesmall);
				MetaDataRoleAuthorizationStrategy.authorize(aggregateTransactions, Component.RENDER,
						AuthConstants.Roles.ROLE_USER);


				list.add(aggregateTransactions);

				return list;
			}
		};
		navbarDropDownButton.setIconType(IconType.plus);
		navbarDropDownButton.add(new DropDownAutoOpen());
		MetaDataRoleAuthorizationStrategy.authorize(navbarDropDownButton, Component.RENDER,
				AuthConstants.Roles.ROLE_USER);
		return navbarDropDownButton;
	}

	@WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.RIGHT, disabled = true, order = 97)
	public static Component newThemesNavbarButton(final Page page) {
		return new NavbarDropDownButton(Model.of("Themes")) {
			@Override
			public boolean isActive(final Component item) {
				return false;
			}

			@SuppressWarnings("Convert2Diamond")
			@Override
			protected List<AbstractLink> newSubMenuButtons(final String buttonMarkupId) {
				final List<AbstractLink> subMenu = new ArrayList<AbstractLink>();
				subMenu.add(new MenuHeader(Model.of("all available themes:")));
				subMenu.add(new MenuDivider());

				final IBootstrapSettings settings = Bootstrap.getSettings(this.getApplication());
				final List<ITheme> themes = settings.getThemeProvider().available();

				for (final ITheme theme : themes) {
					final PageParameters params = new PageParameters();
					params.set("theme", theme.name());

					subMenu.add(new MenuBookmarkablePageLink<Page>(page.getPageClass(), params, Model.of(theme.name())));
				}

				return subMenu;
			}
		}.setIconType(IconType.book);
	}

	@WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.RIGHT, order = 10)
	public static Component newLanguageNavbarButton(final Page page) {
		final NavbarDropDownButton languageDropDown = new NavbarDropDownButton(new StringResourceModel("navbar.lang", page,
				null, null)) {
			private static final long serialVersionUID = 2866997914075956070L;

			@Override
			public boolean isActive(final Component item) {
				return false;
			}

			@SuppressWarnings("Convert2Diamond")
			@Override
			protected List<AbstractLink> newSubMenuButtons(final String buttonMarkupId) {
				final List<AbstractLink> list = new ArrayList<>();
				list.add(new MenuHeader(new StringResourceModel("navbar.lang.header", this, null, null)));
				list.add(new MenuDivider());

				// TODO: get available languages
				final List<Locale> langs = new ArrayList<>();
                langs.add(new Locale("en"));
                langs.add(new Locale("hr"));
                langs.add(new Locale("cs"));
                langs.add(new Locale("bg"));
                langs.add(new Locale("hu"));
                langs.add(new Locale("lv"));
                langs.add(new Locale("lt"));
                langs.add(new Locale("pl"));
                langs.add(new Locale("ro"));
                langs.add(new Locale("sl"));

				for (final Locale l : langs) {
					final PageParameters params = new PageParameters(page.getPageParameters());
					params.set(Constants.LANGUAGE_PAGE_PARAM, l.getLanguage());
					list.add(new MenuBookmarkablePageLink<Page>(page.getPageClass(), params, Model.of(l
							.getDisplayName())));
				}

				return list;
			}
		};
		languageDropDown.setIconType(IconType.flag);
		languageDropDown.add(new DropDownAutoOpen());
		return languageDropDown;
	}

	@SuppressWarnings("Convert2Diamond")
	public static List<AbstractLink> getTransactionLinks(final List<String> values, final Page page) {
		final List<AbstractLink> list = new ArrayList<>();
		for (final String item : values) {
			final PageParameters params = new PageParameters();
			params.set(Constants.PARAM_TRANSACTION_TYPE, item);
			list.add(new MenuBookmarkablePageLink<TransactionPage>(CustomTransactionPage.class, params,
					new StringResourceModel("navbar.newTransaction." + item, page, null, null)));
		}
		return list;
	}


	@WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.RIGHT, order = 20)
	public static Component newHelpMenu(final Page page) {
		final NavbarDropDownButton navbarDropDownButton = new NavbarDropDownButton(new StringResourceModel("navbar.help",
				page, null, null)) {
			@Override
			public boolean isActive(final Component item) {
				return false;
			}

			@Override
			protected List<AbstractLink> newSubMenuButtons(final String buttonMarkupId) {
				final List<AbstractLink> list = new ArrayList<>();
				list.add(new MenuHeader(new StringResourceModel("navbar.help.header", this, null)));
				list.add(new MenuDivider());

				final MenuBookmarkablePageLink<RecipientCountriesPdf> recipientCountriesLink = new MenuBookmarkablePageLink<RecipientCountriesPdf>(
						RecipientCountriesPdf.class, null, new StringResourceModel("navbar.recipientcountries", this,
								null)) {
					private static final long serialVersionUID = 1L;
					@Override
					protected void onComponentTag(final ComponentTag tag) {
						super.onComponentTag(tag);
						tag.put("target", "_blank");
					}
				};
				recipientCountriesLink.setIconType(IconType.download);
				list.add(recipientCountriesLink);

				final MenuBookmarkablePageLink<LitOfInternationalOrgs> listOfInternationalOrgs = new MenuBookmarkablePageLink<LitOfInternationalOrgs>(
						LitOfInternationalOrgs.class, null, new StringResourceModel("navbar.listofinternationalorgs", this,
								null)) {
					private static final long serialVersionUID = 1L;
					@Override
					protected void onComponentTag(final ComponentTag tag) {
						super.onComponentTag(tag);
						tag.put("target", "_blank");
					}
				};
				listOfInternationalOrgs.setIconType(IconType.download);
				list.add(listOfInternationalOrgs);

				

				final MenuBookmarkablePageLink<DacCrsListCodes> dacCrslistCodes = new MenuBookmarkablePageLink<DacCrsListCodes>(
						DacCrsListCodes.class, null, new StringResourceModel("navbar.daccrslistcodes", this,
								null)) {
					private static final long serialVersionUID = 1L;
					@Override
					protected void onComponentTag(final ComponentTag tag) {
						super.onComponentTag(tag);
						tag.put("target", "_blank");
					}
				};
				dacCrslistCodes.setIconType(IconType.download);
				list.add(dacCrslistCodes);

				final MenuBookmarkablePageLink<OecdDacDirectives> oecdDacDirectives = new MenuBookmarkablePageLink<OecdDacDirectives>(
						OecdDacDirectives.class, null, new StringResourceModel("navbar.oecdacdirectives", this,
								null)) {
					private static final long serialVersionUID = 1L;
					@Override
					protected void onComponentTag(final ComponentTag tag) {
						super.onComponentTag(tag);
						tag.put("target", "_blank");
					}
				};
				oecdDacDirectives.setIconType(IconType.download);
				list.add(oecdDacDirectives);

				final MenuBookmarkablePageLink<DacGlossary> dacGlossary = new MenuBookmarkablePageLink<DacGlossary>(
						DacGlossary.class, null, new StringResourceModel("navbar.dacglossary", this,
								null)) {
					private static final long serialVersionUID = 1L;
					@Override
					protected void onComponentTag(final ComponentTag tag) {
						super.onComponentTag(tag);
						tag.put("target", "_blank");
					}
				};
				dacGlossary.setIconType(IconType.download);
				list.add(dacGlossary);
				
				final MenuBookmarkablePageLink<HelpPage> helpTutorialsPage =
						new MenuBookmarkablePageLink<HelpPage>(HelpPage.class, null,new StringResourceModel("navbar.help.videotut", page, null));
				helpTutorialsPage.setIconType(IconType.download);

				list.add(helpTutorialsPage);


				return list;
			}

		};
		navbarDropDownButton.setIconType(IconType.book);
		navbarDropDownButton.add(new DropDownAutoOpen());
		MetaDataRoleAuthorizationStrategy.authorize(navbarDropDownButton, Component.RENDER,
				AuthConstants.Roles.ROLE_USER);

		return navbarDropDownButton;
	}


	@WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.RIGHT, order = 98, disabled = true)
	public static Component feedbackNavbarButton(final Page page) {
		final NavbarButton<LogoutPage> accountNavbarButton = new NavbarButton<LogoutPage>(FeedbackUrl.class,
				new StringResourceModel("navbar.feedback", page, null, null)) {
			@Override
			protected void onComponentTag(final ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("target", "_blank");
			}

		}.setIconType(IconType.share);
		return accountNavbarButton;
	}

}
