package org.devgateway.eudevfin.reports.ui.pages;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 4/3/14
 */

@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
@MountPath(value = "/reportscountrysectorfilter")
public class ReportsCountrySectorFilter extends CustomReportsPage {

    public ReportsCountrySectorFilter () {
//        humanitarianAid.setVisibilityAllowed(Boolean.FALSE);
    }
}
