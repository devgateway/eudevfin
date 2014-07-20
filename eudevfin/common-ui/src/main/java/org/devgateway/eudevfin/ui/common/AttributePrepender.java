/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.ui.common;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.IModel;

/**
 * @author aartimon@dginternational.org
 */
public class AttributePrepender extends AttributeAppender {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public AttributePrepender(String attribute, IModel<?> appendModel,
                              String separator) {
        super(attribute, appendModel, separator);
    }

    @Override
    protected String newValue(String currentValue, String replacementValue) {
        // swap currentValue and replacementValue in the call to the concatenator
        return super.newValue(replacementValue, currentValue);
    }
}
