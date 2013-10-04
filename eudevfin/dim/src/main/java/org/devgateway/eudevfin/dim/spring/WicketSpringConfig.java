package org.devgateway.eudevfin.dim.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WicketSpringConfig {

	@Bean
	public WicketSpringApplication wicketSpringApplication() {
		return new WicketSpringApplication();
	}



}
