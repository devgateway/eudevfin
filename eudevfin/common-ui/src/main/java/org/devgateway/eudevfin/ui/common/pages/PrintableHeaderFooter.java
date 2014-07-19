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
package org.devgateway.eudevfin.ui.common.pages;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.ui.common.scripts.CommonScriptsReference;

/**
 * @author mihai
 *
 */
public class PrintableHeaderFooter<T> extends HeaderFooter<T> {
	
	private static final long serialVersionUID = 6023907278509268615L;

	public PrintableHeaderFooter(PageParameters parameters) {		
		super(parameters);	
	}
	
	public PrintableHeaderFooter() {
		super();
	}
		
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
        CommonScriptsReference.renderExportPageScripts(response);
	}
}
