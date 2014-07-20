/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.dim.core;

import mondrian.tui.MockHttpServletRequest;
import org.apache.wicket.protocol.http.mock.MockHttpSession;
import org.apache.wicket.util.tester.WicketTester;
import org.devgateway.eudevfin.ui.common.spring.WicketSpringApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertTrue;

/**
 * @author Alexandru Artimon
 * @since 05/03/14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/commonContext.xml",
        "classpath:META-INF/commonAuthContext.xml",
        "classpath:META-INF/commonFinancialContext.xml",
        "classpath:META-INF/commonUiContext.xml",

        "classpath:META-INF/authContext.xml",

        "classpath:META-INF/commonExchangeContext.xml",
        "classpath:META-INF/commonMetadataContext.xml",


        "classpath:META-INF/financialContext.xml",
        "classpath:META-INF/importMetadataContext.xml",

        "classpath:META-INF/spreadsheetExporterContext.xml",
        "classpath:META-INF/reportsUIContext.xml",
        "classpath:META-INF/exchangeContext.xml",
        "classpath:META-INF/reportsCoreContext.xml",
        "classpath*:WEB-INF/applicationContext.xml",
        "classpath*:META-INF/saikuUIContext.xml",
        "classpath:testDimContext.xml"


})
/*





 */

@WebAppConfiguration
@DirtiesContext
public abstract class BaseWicketTest {
    protected MockHttpSession session;
    protected MockHttpServletRequest request;

    protected WicketTester tester;

    @Autowired
    private ApplicationContext ctx;

    @Before
    public void setUp() {
        WicketSpringApplication application = new WicketSpringApplication();
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


    @Test
    public void passTest() {
        assertTrue(true);
    }
//    protected void startSession() {
//        session = new MockHttpSession();
//    }
//
//    protected void endSession() {
//        session.clearAttributes();
//        session = null;
//    }
//
//    protected void startRequest() {
//        request = new MockHttpServletRequest();
//        request.setSession(session);
//        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//    }
//
//    protected void endRequest() {
//        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).requestCompleted();
//        RequestContextHolder.resetRequestAttributes();
//        request = null;
//    }

}
