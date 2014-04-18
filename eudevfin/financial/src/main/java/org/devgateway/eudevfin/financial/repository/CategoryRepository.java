/**
 * 
 */
package org.devgateway.eudevfin.financial.repository;

import java.util.List;

import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Alex
 *
 */
public interface CategoryRepository extends
		JpaRepository<Category, Long> {
	
	List<Category> findByTagsCode(String tagsCode);
	

	@Query("select distinct categ from CategoryTranslation trn join trn.parent categ join categ.tags tag "
			+ "where (lower(categ.code) like %?1% or lower(trn.name) like %?1% ) and tag.code=?2")
	Page<Category> findByTranslationsNameIgnoreCaseContainsAndTagsCode(String term, String tagsCode,Pageable page);		
	
	//@Query("select categ from Category categ where lower(categ.translations[?1]).name like %?2%")
	@Query("select distinct categ from CategoryTranslation trn join trn.parent categ join categ.tags tag "
			+ " where trn.locale=?1 and "
			+ "(lower(categ.code) like %?2% or lower(trn.name) like %?2% ) and tag.code=?3 ")
	List<Category> findByTranslationsLocaleAndTranslationsNameIgnoreCaseContainsAndTagsCode(String locale, String term, String tagsCode);
	
	Page<Category> findByTagsCode(String code,Pageable page);
	
	List<Category> findByCode(String code);

    @Query(" select distinct geography from CustomFinancialTransaction ctx join ctx.recipient rep join rep.geographyCategory geography")
    Page<Category> findUsedGeography(Pageable page);

    @Query(" select distinct geography from CategoryTranslation trn, CustomFinancialTransaction ctx join ctx.recipient rep join rep.geographyCategory geography " +
            "where trn.parent = geography.id and trn.locale=?1 and (lower(geography.code) like %?2% or lower(trn.name) like %?2% )")
    Page<Category> findUsedGeographyByTranslationsNameIgnoreCase(String locale, String term, Pageable page);

    @Query(" select distinct sector from CustomFinancialTransaction ctx join ctx.sector sector")
    Page<Category> findUsedSector(Pageable page);

    @Query(" select distinct sector from CategoryTranslation trn, CustomFinancialTransaction ctx join ctx.sector sector " +
            "where trn.parent = sector.id and trn.locale=?1 and (lower(sector.code) like %?2% or lower(trn.name) like %?2% )")
    Page<Category> findUsedSectorByTranslationsNameIgnoreCase(String locale, String term, Pageable page);

    @Query(" select distinct typeOfAid from CustomFinancialTransaction ctx join ctx.typeOfAid typeOfAid")
    Page<Category> findUsedTypeOfAid(Pageable page);

    @Query(" select distinct typeOfAid from CategoryTranslation trn, CustomFinancialTransaction ctx join ctx.typeOfAid typeOfAid " +
            "where trn.parent = typeOfAid.id and trn.locale=?1 and (lower(typeOfAid.code) like %?2% or lower(trn.name) like %?2% )")
    Page<Category> findUsedTypeOfAidByTranslationsNameIgnoreCase(String locale, String term, Pageable page);

    @Query(" select distinct biMultilateral from CustomFinancialTransaction ctx join ctx.biMultilateral biMultilateral")
    Page<Category> findUsedTypeOfFlowBiMulti(Pageable page);

    @Query(" select distinct biMultilateral from CategoryTranslation trn, CustomFinancialTransaction ctx join ctx.biMultilateral biMultilateral " +
            "where trn.parent = biMultilateral.id and trn.locale=?1 and (lower(biMultilateral.code) like %?2% or lower(trn.name) like %?2% )")
    Page<Category> findUsedTypeOfFlowBiMultiByTranslationsNameIgnoreCase(String locale, String term, Pageable page);

    @Query(" select distinct channel from CustomFinancialTransaction ctx join ctx.channel channel")
    Page<Category> findUsedChannel(Pageable page);

    @Query(" select distinct channel from CategoryTranslation trn, CustomFinancialTransaction ctx join ctx.channel channel " +
            "where trn.parent = channel.id and trn.locale=?1 and (lower(channel.code) like %?2% or lower(trn.name) like %?2% )")
    Page<Category> findUsedChannelByTranslationsNameIgnoreCase(String locale, String term, Pageable page);
}
