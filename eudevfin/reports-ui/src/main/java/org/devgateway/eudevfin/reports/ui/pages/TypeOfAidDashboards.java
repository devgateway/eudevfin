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
 * @since 6/4/14
 */
@MountPath(value = "/typeofaiddashboards")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class TypeOfAidDashboards extends ReportsDashboards {
    private static final Logger logger = Logger.getLogger(TypeOfAidDashboards.class);

    private String typeOfAidParam;

    public TypeOfAidDashboards(final PageParameters parameters) {
        if(!parameters.get(ReportsConstants.TYPEOFAID_PARAM).equals(StringValue.valueOf((String) null))) {
            typeOfAidParam = parameters.get(ReportsConstants.TYPEOFAID_PARAM).toString();
        }

        logger.error(">>>> typeOfAidParam: " + typeOfAidParam);

        addComponents();
    }

    private void addComponents() {

    }
}
