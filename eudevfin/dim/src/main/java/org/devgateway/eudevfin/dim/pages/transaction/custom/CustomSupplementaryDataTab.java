/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.custom;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.dim.pages.transaction.crs.SupplementaryDataTab;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.util.CategoryConstants;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.DropDownField;

/**
 * Supplementary Data Tab extension for the EU-DEVFIN Form
 *
 * @author aartimon
 * @since 11/12/13
 */
public class CustomSupplementaryDataTab extends SupplementaryDataTab {

	private static final long serialVersionUID = 9016463137955199053L;

	public CustomSupplementaryDataTab(String id,PageParameters parameters) {
        super(id,parameters);
        addComponents();
    }

    private void addComponents() {
        DropDownField<Category> genderEquality = new DropDownField<>("96rmnch",
                new RWComponentPropertyModel<Category>("rmnch"), categoryFactory.get(CategoryConstants.RMNCH_TAG));
        add(genderEquality);

    }

}
