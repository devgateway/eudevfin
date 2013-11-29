/**
 * 
 */
package org.devgateway.eudevfin.financial.repository;

import org.devgateway.eudevfin.financial.Category;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author alex
 *
 */
public interface CategoryRepository extends
		PagingAndSortingRepository<Category, Long> {

}
