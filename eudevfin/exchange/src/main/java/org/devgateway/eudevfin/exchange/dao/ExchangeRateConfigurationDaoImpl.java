package org.devgateway.eudevfin.exchange.dao;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.exchange.common.domain.ExchangeRateConfiguration;
import org.devgateway.eudevfin.exchange.repository.ExchangeRateConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

/**
 * @author idobre
 * @since 9/5/14
 */
@Component
@Lazy(value=false)
public class ExchangeRateConfigurationDaoImpl
        extends AbstractDaoImpl<ExchangeRateConfiguration, Long, ExchangeRateConfigurationRepository> {
    private static final Logger logger = Logger.getLogger(ExchangeRateConfigurationDaoImpl.class);

    @Autowired
    private ExchangeRateConfigurationRepository repo;

    @ServiceActivator(inputChannel = "findRateConfigurationByEntityKeyChannel")
    public NullableWrapper<ExchangeRateConfiguration> findRateConfigurationByEntityKey(String entityKey) {
        ExchangeRateConfiguration exchangeRateConfiguration = repo.findByEntityKey(entityKey);
        return new NullableWrapper(exchangeRateConfiguration);
    }

    @ServiceActivator(inputChannel = "saveRateConfigurationChannel")
    public NullableWrapper<ExchangeRateConfiguration> saveRateConfiguration(
            ExchangeRateConfiguration u) {
        return newWrapper(repo.save(u));
    }

    @Override
    protected ExchangeRateConfigurationRepository getRepo() {
        return repo;
    }
}
