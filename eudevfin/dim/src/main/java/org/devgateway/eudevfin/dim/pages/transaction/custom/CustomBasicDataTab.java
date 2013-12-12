/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.custom;

import org.devgateway.eudevfin.dim.core.RWComponentPropertyModel;
import org.devgateway.eudevfin.dim.core.components.DropDownField;
import org.devgateway.eudevfin.dim.core.temporary.SB;
import org.devgateway.eudevfin.dim.pages.transaction.crs.BasicDataTab;

/**
 * Basic Data Tab extension for the EU-DEVFIN Form
 *
 * @author aartimon
 * @since 11/12/13
 */
class CustomBasicDataTab extends BasicDataTab {
    public CustomBasicDataTab(String id) {
        super(id);
        addComponents();
    }

    private void addComponents() {
        DropDownField<Boolean> cpa = new DropDownField<>("7bCPA", new RWComponentPropertyModel<Boolean>("CPA"),
                SB.boolProvider);
        add(cpa);

    }
}
