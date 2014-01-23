/**
 * 
 */
package org.devgateway.eudevfin.financial.repository;

import java.util.List;

import org.devgateway.eudevfin.financial.ChannelCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Alex
 *
 */
public interface ChannelCategoryRepository extends
		PagingAndSortingRepository<ChannelCategory, Long> {
	
	List<ChannelCategory> findByTagsCode(String code);
	
	ChannelCategory findByCode(String code);
	
	@Query(" select distinct trn.parent from ChannelCategoryTranslation trn where lower(trn.name) like %?1% ")
	Page<ChannelCategory> findByTranslationNameContaining(String searchString,Pageable pageable);
	
	Page<ChannelCategory> findByParentCategoryNotNull(Pageable pageable);
}
