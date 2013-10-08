/*******************************************************************************
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *    mpostelnicu
 ******************************************************************************/
package org.devgateway.eudevfin.persistence.config;

import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.devgateway.eudevfin.domain.Item;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 
 * @author mihai Configuration class for spring. Configures datasource,
 *         transaction manager, entity factory, and the vendor of JPA (Hibernate
 *         or something else).
 */
@Configuration
@EnableTransactionManagement
public class InfrastructureConfig {

	/**
	 * 
	 * @return the spring {@link DataSource}
	 */
	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.DERBY).build();
	}

	/**
	 * The transaction manager for the persistence module.
	 * 
	 * @param emf
	 *            the {@link EntityManagerFactory}
	 * @return a {@link JpaTransactionManager}
	 */
	@Bean
	public JpaTransactionManager transactionManager(
			final EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factoryBean = 
				new LocalContainerEntityManagerFactoryBean();
		factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
		factoryBean.setDataSource(dataSource());
		factoryBean.setPackagesToScan(Item.class.getPackage().getName());
		
		Properties props = new Properties();
		props.put("hibernate.hbm2ddl.auto", "create-drop");
		props.put("hibernate.hbm2ddl.import_files", 
				"derby_memory_initial.sql");		
		factoryBean.setJpaProperties(props);
		return factoryBean;
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter jpaVendorAdapter = 
				new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setDatabase(Database.DERBY);
		jpaVendorAdapter.setShowSql(true);
		jpaVendorAdapter.setGenerateDdl(true);
		Map<String, Object> prop = jpaVendorAdapter.getJpaPropertyMap();

		return jpaVendorAdapter;
	}
}
