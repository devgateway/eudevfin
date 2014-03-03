/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.ui;

import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.sheetexp.integration.api.SpreadsheetTransformerService;

/**
 * @author Alex
 * 
 */
public class SpreadsheetResourceReference extends ResourceReference {

	private final SpreadsheetResource resource;

	// public SpreadsheetResourceReference(final Class<?> scope, final String name,
	// final Locale locale, final String style, final String variation) {
	// super(scope, name, locale, style, variation);
	// // TODO Auto-generated constructor stub
	// }
	//
	// public SpreadsheetResourceReference(final Class<?> scope, final String name) {
	// super(scope, name);
	// // TODO Auto-generated constructor stub
	// }
	//
	// public SpreadsheetResourceReference(final Key key) {
	// super(key);
	// // TODO Auto-generated constructor stub
	// }

	public SpreadsheetResourceReference(final String name, final Integer year, final SpreadsheetTransformerService transformerService,
			final CustomFinancialTransactionService txService) {
		super(name);
		this.resource = new SpreadsheetResource(year, transformerService, txService);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.wicket.request.resource.ResourceReference#getResource()
	 */
	@Override
	public IResource getResource() {
		return this.resource;
	}

}
