/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.ui;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.sheetexp.integration.api.SpreadsheetTransformerService;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author Alex
 *
 */
@MountPath(value = "/spreadsheets")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ExportSpreadsheetsPage extends HeaderFooter {
	

	@SpringBean
	private SpreadsheetTransformerService transformerService;
	
	@SpringBean
	private CustomFinancialTransactionService txService;


	private final SpreadsheetResourceReference reference;
	
	public ExportSpreadsheetsPage () {
		super();
		
		this.reference						= new SpreadsheetResourceReference("spreadsheet-reference", this.transformerService, this.txService);
		
		final ResourceLink<String> fssLink	= new ResourceLink<String>("fss-export-link", this.reference );
		final Label fssLabel				= new Label("fss-export-label", "Export Forward Spending Survey");
		fssLink.add(fssLabel);
		
		this.add(fssLink);
		
	} 
}
