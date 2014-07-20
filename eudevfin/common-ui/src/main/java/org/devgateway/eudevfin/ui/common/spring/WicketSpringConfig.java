/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.ui.common.spring;

import org.apache.wicket.protocol.http.ReloadingWicketFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WicketSpringConfig {

    /**
     * @return
     * @see ReloadingWicketFilter
     */
    @Bean
    public WicketSpringApplication wicketSpringApplication() {
        return new WicketSpringApplication();
    }


}
