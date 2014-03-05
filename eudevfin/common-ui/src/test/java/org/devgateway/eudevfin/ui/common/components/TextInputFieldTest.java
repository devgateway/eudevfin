/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.components;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.TagTester;
import org.devgateway.eudevfin.ui.common.BaseWicketTest;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Alexandru Artimon
 * @since 05/03/14
 */
public class TextInputFieldTest extends BaseWicketTest {

    @Test
    public void testTypeIsRequired() {
        TextInputField<String> component = new TextInputField<>("text", new Model<String>());

        try {
            tester.startComponentInPage(component);
            fail("Shouldn't allow a text input field without the type set");
        } catch (RuntimeException e) {
            //expected
        }
    }

    @Test
    public void testTextRender() {
        TextInputField<String> component = new TextInputField<>("text", new Model<String>());
        component.typeString(10);
        tester.startComponentInPage(component);
        tester.assertComponent("text", TextInputField.class);

        TagTester label = TagTester.createTagByAttribute(tester.getLastResponse().getDocument(), "class", "control-label");
        assertNotNull(label);
        assertEquals("label", label.getName());
        assertEquals("Label", label.getValue());

        TagTester controls = TagTester.createTagByAttribute(tester.getLastResponse().getDocument(), "class", "controls");
        assertNotNull(controls);
        assertEquals("div", controls.getName());

        List<TagTester> help = TagTester.createTagsByAttribute(tester.getLastResponse().getDocument(), "class", "help-block", false);
        assertNotNull(help);
        assertEquals(1L, help.size());
        assertEquals("Help Text", help.get(0).getValue());

        Component icon = tester.getComponentFromLastRenderedPage("text:control-group:detailedHelpIcon");
        //click on the expand help button and see if the extra help arrives
        tester.executeBehavior(icon.getBehaviors(AjaxEventBehavior.class).get(0));


        help = TagTester.createTagsByAttribute(tester.getLastResponse().getDocument(), "class", "help-block", false);
        assertNotNull(help);
        assertEquals(2L, help.size());
        assertEquals("Help Text", help.get(0).getValue());
        assertEquals("Help Detail", help.get(1).getValue());

        TagTester inputField = TagTester.createTagByAttribute(tester.getLastResponse().getDocument(), "type", "text");
        assertNotNull(inputField);
        assertEquals("input", inputField.getName());
        assertEquals("Placeholder", inputField.getAttribute("placeholder"));

    }
}
