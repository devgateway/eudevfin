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
package org.devgateway.eudevfin.dim.spring;

import org.apache.wicket.model.Model;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;

@Configuration
public class WicketSpringConfig {

	@Bean
	public WicketSpringApplication wicketSpringApplication() {
		return new WicketSpringApplication();
	}
	
	
}
