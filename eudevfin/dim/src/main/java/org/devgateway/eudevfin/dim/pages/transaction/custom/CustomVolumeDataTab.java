/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.custom;

import org.devgateway.eudevfin.dim.core.RWComponentPropertyModel;
import org.devgateway.eudevfin.dim.core.components.TextInputField;
import org.devgateway.eudevfin.dim.pages.transaction.crs.VolumeDataTab;

/**
 * @author aartimon
 * @since 16/12/13
 */
public class CustomVolumeDataTab extends VolumeDataTab {
    public CustomVolumeDataTab(String id) {
        super(id);
        addComponents();
    }

    private void addComponents() {
        TextInputField<String> budgetCode = new TextInputField<>("34bBudgetCode", new RWComponentPropertyModel<String>("budgetCode"));
        add(budgetCode);

        TextInputField<String> budgetLine = new TextInputField<>("34cBudgetLine", new RWComponentPropertyModel<String>("budgetLine"));
        add(budgetLine);

        TextInputField<String> budgetActivity = new TextInputField<>("34dBudgetActivity", new RWComponentPropertyModel<String>("budgetActivity"));
        add(budgetActivity);

    }
}
