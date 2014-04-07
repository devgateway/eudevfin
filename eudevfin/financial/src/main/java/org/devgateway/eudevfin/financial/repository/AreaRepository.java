/**
 * 
 */
package org.devgateway.eudevfin.financial.repository;

import java.util.List;

import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Alex
 *
 */
public interface AreaRepository extends PagingAndSortingRepository<Area, Long> {
	
	@Query(" select trn.parent from AreaTranslation trn where trn.locale=?1 AND lower(trn.name) like %?2% ")
	List<Area> findByTranslationLocaleAndTranslationNameContaining(String locale, String searchString);

	@Query(" select distinct trn.parent from AreaTranslation trn where lower(trn.name) like %?1% ")
	Page<Area> findByTranslationNameContaining(String searchString,Pageable pageable);
	
	Area findByCode(String code);

    @Query(" select distinct rep from CustomFinancialTransaction ctx join ctx.recipient rep")
    Page<Area> findUsedArea(Pageable page);

    @Query(" select distinct rep from AreaTranslation trn, CustomFinancialTransaction ctx join ctx.recipient rep " +
            "where trn.parent = rep.id AND trn.locale=?1 AND lower(trn.name) like %?2% ")
    Page<Area> findUsedAreaByTranslationsNameIgnoreCase(String locale, String term, Pageable page);
}
