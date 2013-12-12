/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.custom;

import org.devgateway.eudevfin.dim.pages.transaction.crs.SupplementaryDataTab;

/**
 * Supplementary Data Tab extension for the EU-DEVFIN Form
 *
 * @author aartimon
 * @since 11/12/13
 */
class CustomSupplementaryDataTab extends SupplementaryDataTab {
    public CustomSupplementaryDataTab(String id) {
        super(id);
        addComponents();
    }

    private void addComponents() {
        /*
        DropDownField<String> tbd96 = new DropDownField<>("96tbd", new RWComponentPropertyModel<String>("tbd96"),
                SB.countryProvider);
        add(tbd96);
        */
    }

}
