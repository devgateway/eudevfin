package org.devgateway.eudevfin.mcm.pages;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 2/7/14
 */
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_SUPERVISOR)
@MountPath(value = "/systemmaintenance")
public class SystemMaintenance extends HeaderFooter {

	public SystemMaintenance () {}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
	}
}
