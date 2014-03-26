package org.devgateway.eudevfin.financial.repository;

import java.util.List;

import org.devgateway.eudevfin.metadata.common.domain.translation.AreaTranslation;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AreaTranslationRepository extends
		PagingAndSortingRepository<AreaTranslation, Long> {

	@Override
	List<AreaTranslation> findAll();
}
