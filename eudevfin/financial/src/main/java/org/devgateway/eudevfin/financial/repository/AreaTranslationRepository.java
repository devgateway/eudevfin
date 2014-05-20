package org.devgateway.eudevfin.financial.repository;

import java.util.List;

import org.devgateway.eudevfin.metadata.common.domain.translation.AreaTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaTranslationRepository extends
		JpaRepository<AreaTranslation, Long> {

	@Override
	List<AreaTranslation> findAll();
}
