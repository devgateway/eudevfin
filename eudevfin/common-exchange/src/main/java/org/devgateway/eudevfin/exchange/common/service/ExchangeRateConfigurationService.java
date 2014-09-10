package org.devgateway.eudevfin.exchange.common.service;

import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.exchange.common.domain.ExchangeRateConfiguration;

/**
 * @author idobre
 * @since 9/5/14
 */
public interface ExchangeRateConfigurationService extends BaseEntityService<ExchangeRateConfiguration> {
    NullableWrapper<ExchangeRateConfiguration> findByEntityKey(String entityKey);
}
