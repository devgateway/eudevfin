package org.devgateway.eudevfin.reports.ui.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.reports.core.service.QueryService;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.Calendar;

/**
 * @author idobre
 * @since 4/16/14
 */
@MountPath(value = "/reportscountryinstitutiondashboards")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ReportsCountryInstitutionDashboards extends HeaderFooter {
    private static final Logger logger = Logger.getLogger(ReportsCountryInstitutionDashboards.class);

    private int tableYear;
    // variables that holds the parameters received from filter
    private String geographyParam;
    private String recipientParam;
    private String institutionParam;
    private String yearParam;
    private String coFinancingParam;

    @SpringBean
    QueryService CdaService;

    public ReportsCountryInstitutionDashboards(final PageParameters parameters) {
        // get a default year if it's not specified
        tableYear = Calendar.getInstance().get(Calendar.YEAR) - 1;

        // process the parameters received from the filters
        if(!parameters.get(ReportsConstants.GEOGRAPHY_PARAM).equals(StringValue.valueOf((String) null))) {
            geographyParam = parameters.get(ReportsConstants.GEOGRAPHY_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.RECIPIENT_PARAM).equals(StringValue.valueOf((String) null))) {
            recipientParam = parameters.get(ReportsConstants.RECIPIENT_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.INSTITUTION_PARAM).equals(StringValue.valueOf((String) null))) {
            institutionParam = parameters.get(ReportsConstants.INSTITUTION_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.YEAR_PARAM).equals(StringValue.valueOf((String) null))) {
            yearParam = parameters.get(ReportsConstants.YEAR_PARAM).toString();
            tableYear = Integer.parseInt(yearParam);
        }
        if(!parameters.get(ReportsConstants.COFINANCING_PARAM).equals(StringValue.valueOf((String) null))) {
            coFinancingParam = parameters.get(ReportsConstants.COFINANCING_PARAM).toString();
        }

        logger.error("> geographyParam: " + geographyParam);
        logger.error("> recipientParam: " + recipientParam);
        logger.error("> institutionParam: " + institutionParam);
        logger.error("> yearParam: " + yearParam);
        logger.error("> coFinancingParam: " + coFinancingParam);

        addComponents();
    }

    private void addComponents() {
        Label title = new Label("title", new StringResourceModel("reportscountryinstitutiondashboards.title", this, null, null));
        add(title);
    }
}
