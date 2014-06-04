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

@MountPath(value = "/transactiondashboards")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class TransactionDashboards extends ReportsDashboards {
    private static final Logger logger = Logger.getLogger(TransactionDashboards.class);

    private String transactionIdParam;

    public TransactionDashboards(final PageParameters parameters) {
        if(!parameters.get(ReportsConstants.TRANSACTIONID_PARAM).equals(StringValue.valueOf((String) null))) {
            transactionIdParam = parameters.get(ReportsConstants.TRANSACTIONID_PARAM).toString();
        }

        logger.error(">>>> transactionIdParam: " + transactionIdParam);

        addComponents();
    }

    private void addComponents() {
        Label title = new Label("title", new StringResourceModel("transactiondashboards.title", this, null, null));
        add(title);
    }
}
