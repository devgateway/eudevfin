/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

/**
 * 
 */
package org.devgateway.eudevfin.mcm.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.mcm.pages.EditOrganizationPage;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.ui.common.components.TableListPanel;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;

/**
 * @author mihai
 * 
 */
public class OrganizationTableListPanel extends TableListPanel<Organization> {

	private static final long serialVersionUID = 7403189032489301520L;

	public OrganizationTableListPanel(String id,
			ListGeneratorInterface<Organization> listGenerator) {
		super(id, listGenerator);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void populateTable() {
		this.itemsListView = new ListView<Organization>("orgList", items) {

            private static final long serialVersionUID = -8758662617501215830L;

            @Override
			protected void populateItem(ListItem<Organization> listItem) {
				final Organization org = listItem.getModelObject();


				Link linkToEditOrg=new Link("linkToEditOrg") {
					@Override
					public void onClick() {
						setResponsePage(EditOrganizationPage.class,
								new PageParameters().add(EditOrganizationPage.PARAM_ORG_ID, org.getId()));						
					}
				};
				
				linkToEditOrg.setBody(Model.of(org.getName()));

				Label codeLabel = new Label("code",
						org.getCode() != null ? org.getCode(): "");

				listItem.add(codeLabel);
				listItem.add(linkToEditOrg);

			}

		};

		this.add(itemsListView);

	}
}
