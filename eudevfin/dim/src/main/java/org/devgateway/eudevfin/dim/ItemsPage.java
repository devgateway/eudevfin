package org.devgateway.eudevfin.dim;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.devgateway.eudevfin.domain.Item;

@AuthorizeInstantiation("ROLE_SUPERVISOR")
public class ItemsPage extends WebPage {

	public ItemsPage() {

		add(new PropertyListView<Item>("items") {

			@Override
			protected void populateItem(ListItem<Item> item) {
				item.add(new Label("product"));
				item.add(new Label("quantity"));
			}
		});
	}
}