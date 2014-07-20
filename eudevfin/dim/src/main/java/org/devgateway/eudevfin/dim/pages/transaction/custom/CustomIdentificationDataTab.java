/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.dim.pages.transaction.custom;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.dim.pages.transaction.crs.IdentificationDataTab;
import org.devgateway.eudevfin.ui.common.providers.CategoryProviderFactory;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.util.CategoryConstants;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.DateInputField;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.models.DateToLocalDateTimeModel;
import org.joda.time.LocalDateTime;

/**
 * @author aartimon
 * @since 12/12/13
 */
public class CustomIdentificationDataTab extends IdentificationDataTab {
	
	private static final long serialVersionUID = -8347292872714970637L;
	@SpringBean    
	private CategoryProviderFactory categoryFactory;

	
    public CustomIdentificationDataTab(String id,PageParameters parameters) {
        super(id, parameters);
        addComponents();
    }

    private void addComponents() {
        DateInputField firstRepaymentDate = new DateInputField("1aDataAsPerDate",
                new DateToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("dataAsPerDate")));
        add(firstRepaymentDate);
        

        DropDownField<Category> levelOfCertainty = new DropDownField<>("6aLevelOfCertainty",
                new RWComponentPropertyModel<Category>("levelOfCertainty"), categoryFactory.get(CategoryConstants.LEVEL_OF_CERTAINTY_TAG));
        levelOfCertainty.required();
        add(levelOfCertainty);

    }
}
