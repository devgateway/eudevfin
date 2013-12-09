package org.devgateway.eudevfin.dim.desktop.components;

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.devgateway.eudevfin.financial.util.PagingItem;

public interface Pageable {

	public AjaxLink<PagingItem> createLink(PagingItem modelObj);

}