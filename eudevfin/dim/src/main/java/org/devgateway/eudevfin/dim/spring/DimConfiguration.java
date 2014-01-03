package org.devgateway.eudevfin.dim.spring;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.dim.core.Constants;
import org.devgateway.eudevfin.dim.core.temporary.SB;
import org.devgateway.eudevfin.dim.pages.transaction.crs.TransactionPage;
import org.devgateway.eudevfin.dim.pages.transaction.custom.CustomTransactionPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.DropDownSubMenu;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarDropDownButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.button.DropDownAutoOpen;

@Configuration
@DependsOn(value="wicketSpringApplication")
public class DimConfiguration {
	
	@Autowired
	private Navbar navbar;
	
	@PostConstruct
	@DependsOn(value="wicketSpringApplication")
	private void addDimNavbar() {
        NavbarDropDownButton transactionPageNavbarButton = newTransactionDropdown();
        MetaDataRoleAuthorizationStrategy.authorize(transactionPageNavbarButton, Component.RENDER, AuthConstants.Roles.ROLE_USER);
        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,transactionPageNavbarButton));

	}

	
	  private NavbarDropDownButton newTransactionDropdown() {
	        NavbarDropDownButton navbarDropDownButton = new NavbarDropDownButton(new StringResourceModel("navbar.newTransaction", null, null)) {
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

	                        return getTransactionLinks(values);
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

	                        return getTransactionLinks(values);
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

	                        return getTransactionLinks(values);
	                    }
	                };
	                nonOda.setIconType(IconType.random);
	                list.add(nonOda);

	                return list;
	            }
	        };
	        navbarDropDownButton.setIconType(IconType.plus);
	        navbarDropDownButton.add(new DropDownAutoOpen());
	        return navbarDropDownButton;
	    }
	  
	  
	    @SuppressWarnings("Convert2Diamond")
	    private List<AbstractLink> getTransactionLinks(List<String> values) {
	        List<AbstractLink> list = new ArrayList<>();
	        for (String item : values) {
	            PageParameters params = new PageParameters();
	            params.set(Constants.TRANSACTION_TYPE, item);
	            list.add(new MenuBookmarkablePageLink<TransactionPage>(CustomTransactionPage.class, params, new StringResourceModel("navbar.newTransaction." + item, null, null)));
	        }
	        return list;
	    }

}
