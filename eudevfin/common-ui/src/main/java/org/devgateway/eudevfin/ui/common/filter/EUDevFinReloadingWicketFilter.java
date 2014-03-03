/**
 * 
 */
package org.devgateway.eudevfin.ui.common.filter;

import org.apache.wicket.application.ReloadingClassLoader;
import org.apache.wicket.protocol.http.ReloadingWicketFilter;

/**
 * @author mihai
 * 
 * @see ReloadingWicketFilter
 */
public class EUDevFinReloadingWicketFilter extends ReloadingWicketFilter {
	static
  	{
  		ReloadingClassLoader.includePattern("org.devgateway.eudevfin.*");
  	}

}
