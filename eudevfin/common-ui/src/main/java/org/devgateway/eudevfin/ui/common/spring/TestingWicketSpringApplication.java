/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.ui.common.spring;

import org.apache.wicket.Page;
import org.devgateway.eudevfin.ui.common.pages.TestingHeaderFooter;

/**
 * @author Alexandru Artimon
 * @since 05/03/14
 */
public class TestingWicketSpringApplication extends WicketSpringApplication {
    @Override
    public Class<? extends Page> getHomePage() {
        return TestingHeaderFooter.class;
    }
}
