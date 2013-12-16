/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.custom;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.Panel;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.dim.core.permissions.RoleActionMapping;
import org.devgateway.eudevfin.dim.pages.transaction.crs.TransactionPage;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Custom Transaction Page for the EU-DEVFIN Form, extends the CRS Form with extended tabs and permissions
 *
 * @author aartimon
 * @see TransactionPage
 * @since 11/12/13
 */
@MountPath(value = "/custom")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class CustomTransactionPage extends TransactionPage {

    private static final CustomTransactionPermissionProvider permissions = new CustomTransactionPermissionProvider();

    @Override
    protected List<Class<? extends Panel>> getTabs() {
        List<Class<? extends Panel>> tabList = new ArrayList<>();
        tabList.add(CustomIdentificationDataTab.class);
        tabList.add(CustomBasicDataTab.class);
        tabList.add(CustomSupplementaryDataTab.class);
        tabList.add(CustomVolumeDataTab.class);
        tabList.add(CustomForLoansOnlyTab.class);
        tabList.add(AdditionalInfoTab.class);
        return tabList;
    }

    @Override
    public HashMap<String, RoleActionMapping> getPermissions() {
        return permissions.permissions();
    }

    @Override
    protected FinancialTransaction getFinancialTransaction() {
        return new CustomFinancialTransaction();
    }
}
