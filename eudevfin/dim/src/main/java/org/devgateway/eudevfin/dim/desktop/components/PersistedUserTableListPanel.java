/**
 * 
 */
package org.devgateway.eudevfin.dim.desktop.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
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
		this.itemsListView		= new ListView<PersistedUser>("userList", items  ) {

			private static final long serialVersionUID = -8758662617501215830L;

			@Override
			protected void populateItem(ListItem<PersistedUser> listItem) {
				PersistedUser user			= listItem.getModelObject();
				Label idLabel						= new Label("userName", user.getUsername());
				listItem.add(idLabel);
			}

		};
		
		this.add(itemsListView);
		
	}

}
