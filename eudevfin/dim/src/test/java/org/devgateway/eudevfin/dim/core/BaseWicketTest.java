/*******************************************************************************
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    aartimon
 ******************************************************************************/

package org.devgateway.eudevfin.dim.core;

import org.apache.wicket.util.tester.WicketTester;
import org.devgateway.eudevfin.dim.spring.WicketSpringApplication;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author aartimon@developmentgateway.org
 * @since 23 October 2013
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/dimContext.xml", "classpath:META-INF/authContext.xml", "classpath:/META-INF/financialContext.xml"})
public abstract class BaseWicketTest {
    protected WicketTester tester;

    @Autowired
    private WicketSpringApplication app;

    @Before
    public void setUp() throws Exception {
        tester = new WicketTester(app);
    }
}
