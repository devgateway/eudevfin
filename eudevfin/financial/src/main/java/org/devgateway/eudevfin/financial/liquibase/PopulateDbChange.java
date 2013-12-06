/**
 * 
 */
package org.devgateway.eudevfin.financial.liquibase;

import org.devgateway.eudevfin.common.liquibase.AbstractSpringCustomTaskChange;


/**
 * @author Alex, Mihai
 *
 */
public class PopulateDbChange extends AbstractSpringCustomTaskChange {

	@Override
	public Class<PopulateMockupDb> getPopulator() {
		return PopulateMockupDb.class;
	}
	

}
