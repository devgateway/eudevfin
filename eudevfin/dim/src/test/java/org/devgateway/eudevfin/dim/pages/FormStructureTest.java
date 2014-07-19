/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.dim.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.Loop;
import org.devgateway.eudevfin.dim.core.DimComponentInfo;
import org.devgateway.eudevfin.dim.pages.transaction.crs.TransactionPage;
import org.devgateway.eudevfin.dim.pages.transaction.custom.CustomTransactionPage;
import org.devgateway.eudevfin.ui.common.components.BootstrapCancelButton;
import org.devgateway.eudevfin.ui.common.components.tabs.BootstrapJSTabbedPanel;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author Alexandru Artimon
 * @since 29/04/14
 */
public class FormStructureTest extends AbstractFormStructureTest {
    private void testTransactionPage() {
        tester.assertComponent("form", Form.class);
        tester.assertComponent("form:bc", BootstrapJSTabbedPanel.class);
        tester.assertComponent("form:submit", TransactionPage.TransactionPageSubmitButton.class);
        tester.assertComponent("form:cancel", BootstrapCancelButton.class);
        tester.assertComponent("feedback", NotificationPanel.class);
        tester.assertComponent("form:bc:tabContent", Loop.class);
        initComponents();
        testComponents();

    }

    private void testComponents() {
        final HashMap<String, DimComponentInfo> components = getComponents();
        for (String id : components.keySet()) {
            tester.assertComponent(components.get(id).getPath(), components.get(id).getClazz());
        }
    }

    @Test
    public void testRenderPage() {
        testLoginAsUser();
        testMenuItem(true, "./custom?transactionType=bilateralOda.advanceQuestionnaire");
        tester.executeUrl("./custom?transactionType=bilateralOda.advanceQuestionnaire");
        tester.assertRenderedPage(CustomTransactionPage.class);
        testTransactionPage();
    }
}
