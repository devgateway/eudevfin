/**
 * 
 */
package org.devgateway.eudevfin.dim.desktop.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.dim.pages.admin.EditPersistedUserPage;
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

			private static final long sermiialVersionUID = -8758662617501215830L;

			@Override
			protected void populateItem(ListItem<PersistedUser> listItem) {
				final PersistedUser user = listItem.getModelObject();
				
				
				
				
				Link linkToEditUser=new Link("linkToEditUser") {
					@Override
					public void onClick() {
						PageParameters pageParameters = new PageParameters(); 
				
						pageParameters.add(EditPersistedUserPage.PARAM_USER_ID, user.getId());

						setResponsePage(EditPersistedUserPage.class, pageParameters);
						
					}
				};
				
				linkToEditUser.setBody(Model.of(user.getUsername()));

				Label groupsLabel = new Label("groups",
						user.getGroups() != null ? user.getGroups().toString()
								: "");

				Label authoritiesLabel = new Label("authorities",
						user.getGroups() != null ? user.getAuthorities()
								.toString() : "");

				listItem.add(groupsLabel);
				listItem.add(authoritiesLabel);
				listItem.add(linkToEditUser);

			}

		};

		this.add(itemsListView);

	}
}
