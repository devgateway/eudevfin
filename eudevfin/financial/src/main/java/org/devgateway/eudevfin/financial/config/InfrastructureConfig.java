package org.devgateway.eudevfin.financial.config;

import java.util.Map;

import javax.sql.DataSource;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.hibernate.SessionFactory;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@ImportResource("classpath:/META-INF/financialContext.xml")
public class InfrastructureConfig {

	@Value("#{myDerbyDataSource}")
	private DataSource dataSource;

	// Entity Manger for JPA
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactory =
				new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setDataSource(dataSource);
		entityManagerFactory.setPackagesToScan(FinancialTransaction.class.getPackage().getName());
		entityManagerFactory.setJpaVendorAdapter(this.jpaVendorAdapter() );
		return entityManagerFactory;
	}
	// JPA Template
	@Bean
	public JpaTemplate jpaTemplate() {
		return new JpaTemplate(entityManagerFactory().getObject());
	}
	// Transaction Manager for JPA
	@Bean
	public JpaTransactionManager transactionManager() {
		return new JpaTransactionManager(entityManagerFactory().getObject());
	}
	// Local Session Factory for getting hibernate connections
	@Bean
	SessionFactory sessionFactory() {
		return ((HibernateEntityManagerFactory)
				entityManagerFactory().getObject()).getSessionFactory();
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