/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.financial.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
@Lazy(value=false)
public class FinancialTransactionHistExchangeRateDaoImpl {

	@Autowired
	EntityManagerFactory entityManagerFactory;
	
	@ServiceActivator(inputChannel="getFinancialTransactionHistoricalExchangeRatesChannel")
	public Collection<LocalDateTime> getHistoricalExchangeRates() {
		
		EntityManager em = entityManagerFactory.createEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<LocalDateTime> cq = cb.createQuery(LocalDateTime.class);
		Root<FinancialTransaction> e = cq.from(FinancialTransaction.class);
		cq.select(e.<LocalDateTime>get("commitmentDate")).distinct(true);
		cq.where(e.<LocalDateTime>get("commitmentDate").isNotNull());
		List<LocalDateTime> list = em.createQuery(cq).getResultList();

        // AQ don't have commitment date so we take the reporting year date
        cq.select(e.<LocalDateTime>get("reportingYear")).distinct(true);
        cq.where(e.<LocalDateTime>get("reportingYear").isNotNull());
        List<LocalDateTime> listReportingYear = em.createQuery(cq).getResultList();

        list.addAll(listReportingYear);

		return list;
	}
}
