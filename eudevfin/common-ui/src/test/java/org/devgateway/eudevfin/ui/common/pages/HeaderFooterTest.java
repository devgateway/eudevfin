/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.pages;

import org.apache.wicket.util.tester.TagTester;
import org.devgateway.eudevfin.ui.common.BaseWicketTest;
import org.junit.Test;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertNotNull;

/**
 * @author Alexandru Artimon
 * @since 05/03/14
 */
@WebAppConfiguration
public class HeaderFooterTest extends BaseWicketTest {

    @Test
    public void renderPage() {
        tester.startPage(tester.getApplication().getHomePage());

        tester.assertRenderedPage(TestingHeaderFooter.class);

        TagTester brand = TagTester.createTagByAttribute(tester.getLastResponse().getDocument(), "class", "brand");
        assertNotNull(brand);


    }
}
