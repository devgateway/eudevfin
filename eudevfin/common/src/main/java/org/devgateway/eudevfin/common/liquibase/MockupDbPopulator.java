/**
 * 
 */
package org.devgateway.eudevfin.common.liquibase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mihai Implement this interface to create populators for liquibase, if
 *         you need JPA beans to be persisted for testing purposes
 * @see PopulateMockupDb for an example
 */
@Component
public interface MockupDbPopulator {

	@Transactional
	public void populate();
}
