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
import org.devgateway.eudevfin.auth.common.domain.PersistedUserGroup;
import org.devgateway.eudevfin.mcm.pages.EditPersistedUserGroupPage;
import org.devgateway.eudevfin.ui.common.components.TableListPanel;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;

/**
 * @author mihai
 * 
 */
public class PersistedUserGroupTableListPanel extends TableListPanel<PersistedUserGroup> {

	private static final long serialVersionUID = 7403189032489301520L;

	public PersistedUserGroupTableListPanel(String id,
			ListGeneratorInterface<PersistedUserGroup> listGenerator) {
		super(id, listGenerator);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void populateTable() {
		this.itemsListView = new ListView<PersistedUserGroup>("groupList", items) {
			
			private static final long serialVersionUID = 1669165609822401092L;

			@Override
			protected void populateItem(ListItem<PersistedUserGroup> listItem) {
				final PersistedUserGroup group = listItem.getModelObject();
				
				Label orgLabel = new Label("org",group.getOrganization()!=null?group.getOrganization().getName():"");
				
				Link linkToEditUserGroup=new Link("linkToEditUserGroup") {
					@Override
					public void onClick() {
						PageParameters pageParameters = new PageParameters(); 
				
						pageParameters.add(EditPersistedUserGroupPage.PARAM_GROUP_ID, group.getId());

						setResponsePage(EditPersistedUserGroupPage.class, pageParameters);
						
					}
				};
				
				linkToEditUserGroup.setBody(Model.of(group.getName()));

				listItem.add(orgLabel);
				listItem.add(linkToEditUserGroup);

			}

		};

		this.add(itemsListView);

	}
}
