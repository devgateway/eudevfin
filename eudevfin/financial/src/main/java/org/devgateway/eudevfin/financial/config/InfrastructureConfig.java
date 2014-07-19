/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.financial.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:/META-INF/financialContext.xml")
public class InfrastructureConfig {

	@Value("#{euDevFinDataSource}")
	private DataSource dataSource;
	@Value("#{entityManagerFactory}")
	private EntityManagerFactory entityManagerFactory;

	// Entity Manger for JPA
//	@Bean
//	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//		LocalContainerEntityManagerFactoryBean entityManagerFactory =
//				new LocalContainerEntityManagerFactoryBean();
//		entityManagerFactory.setDataSource(dataSource);
//		entityManagerFactory.setPackagesToScan(FinancialTransaction.class.getPackage().getName());
//		entityManagerFactory.setJpaVendorAdapter(this.jpaVendorAdapter() );
//		return entityManagerFactory;
//	}
	// JPA Template
//	@Bean
//	public JpaTemplate jpaTemplate() {
//		return new JpaTemplate(entityManagerFactory);
//	}
	// Transaction Manager for JPA
//	@Bean
//	public JpaTransactionManager transactionManager() {
//		return new JpaTransactionManager(entityManagerFactory);
//	}
	// Local Session Factory for getting hibernate connections
//	@Bean
//	SessionFactory sessionFactory() {
//		return ((HibernateEntityManagerFactory)
//				entityManagerFactory).getSessionFactory();
//	}

//	@Bean
//	public JpaVendorAdapter jpaVendorAdapter() {
//		HibernateJpaVendorAdapter jpaVendorAdapter = 
//				new HibernateJpaVendorAdapter();
//		jpaVendorAdapter.setDatabase(Database.DERBY);
//		jpaVendorAdapter.setShowSql(true);
//		jpaVendorAdapter.setGenerateDdl(true);
//		Map<String, Object> prop = jpaVendorAdapter.getJpaPropertyMap();
//
//		return jpaVendorAdapter;
//	}

}