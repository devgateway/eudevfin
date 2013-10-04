package org.devgateway.eudevfin.dim.spring;

import java.util.Properties;

import javax.sql.DataSource;

import org.devgateway.eudevfin.domain.Item;
import org.devgateway.eudevfin.domain.Order;
import org.hibernate.dialect.DerbyTenSevenDialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

@Configuration
public class HibernateConfiguration {

	@Value("#{dataSource}")
	private DataSource dataSource;

	@Bean
	public LocalSessionFactoryBean sessionFactoryBean() {
		Properties props = new Properties();
		props.put("hibernate.dialect", DerbyTenSevenDialect.class.getName());
		props.put("hibernate.format_sql", "true");
		props.put("hibernate.hbm2ddl.auto", "update");

		LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
		bean.setAnnotatedClasses(new Class[] { Item.class, Order.class });
		bean.setHibernateProperties(props);
		bean.setDataSource(this.dataSource);
		return bean;
	}

	@Bean
	public HibernateTransactionManager transactionManager() {
		return new HibernateTransactionManager(sessionFactoryBean().getObject());
	}

}
