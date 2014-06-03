/**
 * 
 */
package org.devgateway.eudevfin.dim.pages.transaction.custom;

import java.util.HashMap;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.dim.pages.transaction.crs.CRSTransactionPermissionProvider;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwarePage;
import org.devgateway.eudevfin.ui.common.permissions.RoleActionMapping;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mihai
 *
 */
 @MountPath(value = "/transaction/view")
 @AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
 public class ViewCustomTransactionPage extends HeaderFooter<FinancialTransaction> implements PermissionAwarePage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4729673458654381557L;
	private static final CRSTransactionPermissionProvider componentPermissions = new CRSTransactionPermissionProvider();
	 
	/**
	 * 
	 */
	public ViewCustomTransactionPage() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashMap<String, RoleActionMapping> getPermissions() {
		return componentPermissions.permissions();
	}


}
