package org.devgateway.eudevfin.exchange.repository;

import org.devgateway.eudevfin.exchange.common.domain.ExchangeRateConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author idobre
 * @since 9/5/14
 */
@Component
public interface ExchangeRateConfigurationRepository extends
        JpaRepository<ExchangeRateConfiguration, Long> {

    ExchangeRateConfiguration findByEntityKey(String entityKey);
}
