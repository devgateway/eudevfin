/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.core;

import org.apache.wicket.util.tester.WicketTester;
import org.devgateway.eudevfin.ui.common.spring.TestingWicketSpringApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Alexandru Artimon
 * @since 05/03/14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/commonContext.xml",
        "classpath:META-INF/commonAuthContext.xml",
        "classpath:META-INF/commonFinancialContext.xml"
})
@DirtiesContext
public abstract class BaseWicketTest {
    protected WicketTester tester;

    @Autowired
    private ApplicationContext ctx;

    @Before
    public void setUp() {
        TestingWicketSpringApplication application = new TestingWicketSpringApplication();
        // applicationContext is injected by Spring as my test class extends AbstractJUnit4SpringContextTests
        AutowireCapableBeanFactory autowireCapableBeanFactory = ctx.getAutowireCapableBeanFactory();
        //autowireCapableBeanFactory.autowireBeanProperties(application, AutowireCapableBeanFactory.AUTOWIRE_AUTODETECT, false );
        autowireCapableBeanFactory.autowireBean(application);

        application.setApplicationContext(ctx);
        tester = new WicketTester(application);
    }

    @After
    public void tearDown() throws Exception {
        tester.destroy();
    }
}
