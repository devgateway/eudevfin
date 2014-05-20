/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

/**
 *
 */
package org.devgateway.eudevfin.common.locale;


import java.io.Serializable;

/**
 * @author Alex
 */
//@Component
//@Scope(value="request",proxyMode=ScopedProxyMode.INTERFACES)
public class LocaleHelper implements LocaleHelperInterface, Serializable {
    private String locale;

    /* (non-Javadoc)
     * @see org.devgateway.eudevfin.financial.util.LocaleHelperInterface#getLocale()
     */
    @Override
    public String getLocale() {
        return locale;
    }

    /* (non-Javadoc)
     * @see org.devgateway.eudevfin.financial.util.LocaleHelperInterface#setLocale(java.lang.String)
     */
    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }


}
