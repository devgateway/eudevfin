/**
 * 
 */
package org.devgateway.eudevfin.financial.repository;

import java.util.List;

import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Alex
 *
 */
public interface ChannelCategoryRepository extends
		JpaRepository<ChannelCategory, Long> {
	
	List<ChannelCategory> findByTagsCode(String code);
	
	Page<ChannelCategory> findByTagsCode(String code,Pageable page);
	
	ChannelCategory findByCode(String code);
	
	@Query(" select distinct trn.parent from ChannelCategoryTranslation trn where lower(trn.name) like %?1% or lower(trn.acronym) like %?1% ")
	Page<ChannelCategory> findByTranslationNameContaining(String searchString,Pageable pageable);
	
	@Query("select distinct categ from CategoryTranslation trn join trn.parent categ join categ.tags tag "
			+ "where (lower(categ.code) like %?1% or lower(trn.name) like %?1% or lower(trn.acronym) like %?1% ) and tag.code=?2")
	Page<ChannelCategory> findByTranslationsNameIgnoreCaseContainsAndTagsCode(String term, String tagsCode,Pageable page);		
	
	Page<ChannelCategory> findByParentCategoryNotNull(Pageable pageable);
}
