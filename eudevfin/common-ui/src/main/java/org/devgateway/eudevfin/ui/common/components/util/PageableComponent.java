package org.devgateway.eudevfin.ui.common.components.util;

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.devgateway.eudevfin.common.service.PagingItem;

public interface PageableComponent {

	public AjaxLink<PagingItem> createLink(PagingItem modelObj);

}