/**
 * 
 */
package org.devgateway.eudevfin.financial.repository;

import java.util.List;

import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.ChannelCategory;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Alex
 *
 */
public interface ChannelCategoryRepository extends
		PagingAndSortingRepository<ChannelCategory, Long> {
	
	List<ChannelCategory> findByTagsCode(String code);
	
	ChannelCategory findByCode(String code);

}
