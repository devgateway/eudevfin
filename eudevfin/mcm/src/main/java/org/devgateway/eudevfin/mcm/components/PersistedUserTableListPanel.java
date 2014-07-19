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
import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.mcm.pages.EditPersistedUserPageElevated;
import org.devgateway.eudevfin.ui.common.components.TableListPanel;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;

/**
 * @author mihai
 * 
 */
public class PersistedUserTableListPanel extends TableListPanel<PersistedUser> {

	private static final long serialVersionUID = 7403189032489301520L;

	public PersistedUserTableListPanel(String id,
			ListGeneratorInterface<PersistedUser> listGenerator) {
		super(id, listGenerator);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void populateTable() {
		this.itemsListView = new ListView<PersistedUser>("userList", items) {

            private static final long serialVersionUID = -8758662617501215830L;

            @Override
			protected void populateItem(ListItem<PersistedUser> listItem) {
				final PersistedUser user = listItem.getModelObject();
				
				
				
				
				Link linkToEditUser=new Link("linkToEditUser") {
					@Override
					public void onClick() {
						setResponsePage(EditPersistedUserPageElevated.class,
								new PageParameters().add(EditPersistedUserPageElevated.PARAM_USER_ID, user.getId()));
						
					}
				};
				
				linkToEditUser.setBody(Model.of(user.getUsername()));

				Label groupLabel = new Label("group",
						user.getGroup() != null ? user.getGroup().getName()
								: "");

				Label authoritiesLabel = new Label("authorities",
						user.getPersistedAuthorities() != null ? user.getPersistedAuthorities()
								.toString() : "");

				listItem.add(groupLabel);
				listItem.add(authoritiesLabel);
				listItem.add(linkToEditUser);

			}

		};

		this.add(itemsListView);

	}
}
