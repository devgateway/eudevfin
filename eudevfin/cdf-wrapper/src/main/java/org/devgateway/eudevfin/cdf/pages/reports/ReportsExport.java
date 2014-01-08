package org.devgateway.eudevfin.cdf.pages.reports;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath(value = "/exportreports")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ReportsExport extends HeaderFooter {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1829518959766029286L;
	
	public ReportsExport () {
        
    }
	
	@Override
    public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
	}
}
