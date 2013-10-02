package org.devgateway.eudevfin.forms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WicketSpringConfig {

	@Bean
	public WicketSpringApplication wicketSpringApplication() {
		return new WicketSpringApplication();
	}

//	@Bean
//	public ItemDao itemDao() {
//		return new ItemDao();
//	}

}
