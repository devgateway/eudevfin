/**
 * 
 */
package org.devgateway.eudevfin.financial.util;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * @author Alex
 *
 */
//@Component
//@Scope(value="request",proxyMode=ScopedProxyMode.INTERFACES)
public class LocaleHelper implements LocaleHelperInterface {
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
