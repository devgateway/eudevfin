/**
 * 
 */
package org.devgateway.eudevfin.financial.repository;

import java.util.List;

import org.devgateway.eudevfin.financial.Category;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alex
 *
 */
public interface CategoryRepository extends
		PagingAndSortingRepository<Category, Long> {
	
	List<Category> findByTagsCode(String code);
	
	Category findByCode(String code);

}
