/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.custom;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.devgateway.eudevfin.dim.core.Constants;
import org.devgateway.eudevfin.dim.core.components.tabs.ITabWithKey;
import org.devgateway.eudevfin.dim.pages.transaction.crs.IdentificationDataTab;
import org.devgateway.eudevfin.dim.pages.transaction.crs.TransactionPage;
import org.devgateway.eudevfin.dim.pages.transaction.crs.VolumeDataTab;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aartimon
 * @since 11/12/13
 */
@MountPath(value = "/custom")
@AuthorizeInstantiation(Constants.ROLE_USER)
public class CustomTransactionPage extends TransactionPage {
    public CustomTransactionPage() {
    }

    @Override
    protected List<ITabWithKey> getTabs() {
        List<ITabWithKey> tabList = new ArrayList<>();
        tabList.add(IdentificationDataTab.newTab(this));
        tabList.add(CustomBasicDataTab.newTab(this));
        tabList.add(CustomSupplementaryDataTab.newTab(this));
        tabList.add(VolumeDataTab.newTab(this));
        tabList.add(CustomForLoansOnlyTab.newTab(this));
        return tabList;
    }

    newPermissions


}
