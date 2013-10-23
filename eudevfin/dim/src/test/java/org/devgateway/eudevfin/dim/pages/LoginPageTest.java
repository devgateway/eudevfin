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

package org.devgateway.eudevfin.dim.pages;

import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.devgateway.eudevfin.dim.spring.WicketSpringApplication;
import org.junit.Before;
import org.junit.Test;
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
public class LoginPageTest {

    private WicketTester tester;

    @Autowired
    private WicketSpringApplication app;

    @Before
    public void setUp() throws Exception {
        tester = new WicketTester(app);
    }

    @Test
    public void testRender(){
        tester.startPage(LoginPage.class);
        tester.assertRenderedPage(LoginPage.class);
    }

    protected void insertUsernamePassword(String user, String password){
        testRender();
        FormTester form = tester.newFormTester("loginform");



        form.setValue("username.field", user);
        form.setValue("password", password);
        form.submit();
    }

    @Test
    public void testLoginSuccessful(){
        insertUsernamePassword("admin", "admin");
        tester.assertRenderedPage(HomePage.class);
    }

    public void testLoginFailed(){
        insertUsernamePassword("wrong", "wrong");
        tester.assertErrorMessages("Incorrect username or password!");
    }




}
