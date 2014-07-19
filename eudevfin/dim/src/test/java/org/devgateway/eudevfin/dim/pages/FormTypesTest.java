/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.dim.pages;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.dim.core.DimComponentInfo;
import org.devgateway.eudevfin.dim.pages.transaction.custom.CustomTransactionPage;
import org.devgateway.eudevfin.dim.pages.transaction.custom.CustomTransactionPermissionProvider;
import org.devgateway.eudevfin.ui.common.components.VisibilityAwareContainer;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAuthorizationStrategy;
import org.devgateway.eudevfin.ui.common.temporary.SB;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Alexandru Artimon
 * @since 29/04/14
 */
public class FormTypesTest extends AbstractFormStructureTest {
    private final static Logger logger = Logger.getLogger(FormTypesTest.class);
    private final static String FORM_FIELD_PATH = ":control-group:control-group_body:xPenderController:field";


    @Test
    public void testRenderPage() {
        testLoginAsUser();
        initComponents();


        List<String> transactions = Arrays.asList(
                SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE,
                SB.BILATERAL_ODA_CRS,
                SB.BILATERAL_ODA_FORWARD_SPENDING,
                SB.MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE,
                SB.MULTILATERAL_ODA_CRS,
                SB.NON_ODA_OOF_EXPORT,
                SB.NON_ODA_OOF_NON_EXPORT,
                SB.NON_ODA_OTHER_FLOWS,
                SB.NON_ODA_PRIVATE_GRANTS,
                SB.NON_ODA_PRIVATE_MARKET
        );

        for (String transaction : transactions) {
            logger.info("Testing form type: " + transaction);
            testPageType(transaction);
        }


    }

    private void testPageType(String transactionType) {
        testMenuItem(true, "./custom?transactionType=" + transactionType);
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        ((MockHttpServletRequest) httpServletRequest).setParameter("transactionType", transactionType);
        tester.executeUrl("./custom?transactionType=" + transactionType);
        tester.assertRenderedPage(CustomTransactionPage.class);
        //Advance Questionnaire Form for Bilateral ODA
        testComponents(transactionType);
    }

    private void testComponents(String transactionType) {
        CustomTransactionPermissionProvider provider = new CustomTransactionPermissionProvider();

        final HashMap<String, DimComponentInfo> components = getComponents();
        for (String id : components.keySet()) {
            if (!components.get(id).getClazz().isAssignableFrom(VisibilityAwareContainer.class)) {
                MockPermissionAwareComponent pwc = new MockPermissionAwareComponent(id);
                boolean visible = PermissionAuthorizationStrategy.checkPermissions(pwc, transactionType, provider.permissions());
                boolean parentsVisible = true;
                if (visible) {
                    String[] split = components.get(id).getPath().split(":");
                    List<String> parents = new ArrayList<>(Arrays.asList(split));
                    parents.add(components.get(id).getTabId()); //add tab as parent

                    for (String p : parents) {
                        if (!PermissionAuthorizationStrategy.checkPermissions(new MockPermissionAwareComponent(p), transactionType, provider.permissions())) {
                            parentsVisible = false;
                            break;
                        }
                    }
                }
                if (visible && parentsVisible) {
                    tester.assertVisible(components.get(id).getPath());
                    if (pwc.isRequired())
                        tester.assertRequired(components.get(id).getPath() + FORM_FIELD_PATH);
                } else {
                    tester.assertInvisible(components.get(id).getPath());
                }
            }
        }
    }

}
