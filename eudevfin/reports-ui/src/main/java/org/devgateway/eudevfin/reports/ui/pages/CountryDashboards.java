package org.devgateway.eudevfin.reports.ui.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 6/2/14
 */
@MountPath(value = "/countrydashboards")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class CountryDashboards extends ReportsDashboards {
    private static final Logger logger = Logger.getLogger(CountryDashboards.class);

    public CountryDashboards(final PageParameters parameters) {
        addComponents();
    }

    private void addComponents() {
        Label title = new Label("title", new StringResourceModel("countrydashboards.title", this, null, null));
        add(title);
    }
}
