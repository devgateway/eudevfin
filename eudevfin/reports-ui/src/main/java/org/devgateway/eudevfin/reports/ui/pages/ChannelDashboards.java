package org.devgateway.eudevfin.reports.ui.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 6/4/14
 */
@MountPath(value = "/channeldashboards")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ChannelDashboards extends ReportsDashboards {
    private static final Logger logger = Logger.getLogger(ChannelDashboards.class);

    private String agencyParam;

    public ChannelDashboards(final PageParameters parameters) {
        if(!parameters.get(ReportsConstants.AGENCY_PARAM).equals(StringValue.valueOf((String) null))) {
            agencyParam = parameters.get(ReportsConstants.AGENCY_PARAM).toString();
        }

        logger.error(">>>> agencyParam: " + agencyParam);

        addComponents();
    }

    private void addComponents() {

    }
}
