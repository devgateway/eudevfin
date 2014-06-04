package org.devgateway.eudevfin.reports.ui.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 6/2/14
 */

@MountPath(value = "/institutiondashboards")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class InstitutionDashboards extends ReportsDashboards {
    private static final Logger logger = Logger.getLogger(InstitutionDashboards.class);

    private String institutionParam;

    public InstitutionDashboards(final PageParameters parameters) {
        if(!parameters.get(ReportsConstants.INSTITUTION_PARAM).equals(StringValue.valueOf((String) null))) {
            institutionParam = parameters.get(ReportsConstants.INSTITUTION_PARAM).toString();
        }

        logger.error(">>>> institutionParam: " + institutionParam);

        addComponents();
    }

    private void addComponents() {

    }
}