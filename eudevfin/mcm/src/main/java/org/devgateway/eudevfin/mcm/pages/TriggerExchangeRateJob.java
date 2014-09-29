package org.devgateway.eudevfin.mcm.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.exchange.job.HistoricalExchangeRateRetrieveJob;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 9/29/14
 *
 * Class that will trigger the exchange rate job (HistoricalExchangeRateRetrieveJob)
 */
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_SUPERVISOR)
@MountPath(value = "/triggerexchangeratejob")
public class TriggerExchangeRateJob  extends HeaderFooter {
    private static final Logger logger = Logger.getLogger(TriggerExchangeRateJob.class);

    @SpringBean(name="schedulingHistoricalExchange")
    private HistoricalExchangeRateRetrieveJob historicalExchangeRateRetrieveJob;

    public TriggerExchangeRateJob(final PageParameters parameters) {
        super(parameters);

        if (historicalExchangeRateRetrieveJob != null) {
            historicalExchangeRateRetrieveJob.jobRetrieveHistoricalExchangeRates();
        }
    }
}
