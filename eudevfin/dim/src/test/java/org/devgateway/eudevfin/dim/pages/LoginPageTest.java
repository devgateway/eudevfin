/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages;

import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.TagTester;
import org.devgateway.eudevfin.dim.core.BaseWicketTest;
import org.devgateway.eudevfin.ui.common.pages.LoginPage;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author aartimon@developmentgateway.org
 * @since 23 October 2013
 */

public class LoginPageTest extends BaseWicketTest {

    @Test
    public void testLoginAsAdmin(){
        //test a successful login
        insertUsernamePassword("admin", "admin");
        tester.assertRenderedPage(HomePage.class);
        testRenderedMenu(true);
    }

    @Test
    public void testLoginAsUser(){
        //test a successful login
        insertUsernamePassword("user", "user");
        tester.assertRenderedPage(HomePage.class);
        testRenderedMenu(false);
    }

    @Test
    public void testLoginFailed(){
        //test a login that will fail
        insertUsernamePassword("wrong", "wrong");
        tester.assertErrorMessages("Incorrect username or password!");
    }

    private void testRenderLoginPage(){
        //set home page as start
        tester.startPage(tester.getApplication().getHomePage());
        //test that the login page is rendered
        tester.assertRenderedPage(LoginPage.class);
        testMenuItem(false, "./logout");
    }

    private void insertUsernamePassword(String user, String password){
        testRenderLoginPage();
        FormTester form = tester.newFormTester("loginform", false);
        //populate form fields and submit
        form.setValue("username:control-group:control-group_body:xPenderController:field", user);
        form.setValue("password:control-group:control-group_body:xPenderController:field", password);
        Assert.assertTrue(form.isClearFeedbackMessagesBeforeSubmit());
        tester.executeAjaxEvent("loginform:submit", "onclick");
    }

    private void testMenuItem(boolean exists, String url){
        //retrieve response's markup
        String responseTxt = tester.getLastResponse().getDocument();
        TagTester tagTester = TagTester.createTagByAttribute(responseTxt, "href", url);
        if (exists){
            Assert.assertNotNull(tagTester);
            Assert.assertEquals("a", tagTester.getName());
        }
        else
            Assert.assertNull(tagTester);
    }

    private void testRenderedMenu(boolean admin) {
        //test we have the Admin link
        testMenuItem(admin, "./users");
        //test for the logout link
        testMenuItem(true, "./logout");
        //test for home link
        testMenuItem(true, "./home");
    }
}
