/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.providers;





/**
 * @author aartimon
 * @since 14/01/14
 */
public class TagCategoryProvider extends AbstractCategoryProvider {

	private static final long serialVersionUID = 5293225229898330470L;
	private final String tag;

    protected TagCategoryProvider(String tag) {
        super();
        this.tag = tag;
    }

    @Override
    protected String getCategoryTag() {
        return tag;
    }
}
