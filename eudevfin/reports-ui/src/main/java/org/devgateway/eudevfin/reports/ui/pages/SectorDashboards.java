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

@MountPath(value = "/sectordashboards")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class SectorDashboards extends ReportsDashboards {
    private static final Logger logger = Logger.getLogger(SectorDashboards.class);

    private String sectorParam;

    public SectorDashboards(final PageParameters parameters) {
        if(!parameters.get(ReportsConstants.SECTOR_PARAM).equals(StringValue.valueOf((String) null))) {
            sectorParam = parameters.get(ReportsConstants.SECTOR_PARAM).toString();
        }

        logger.error(">>>> sectorParam: " + sectorParam);

        addComponents();
    }

    private void addComponents() {

    }
}