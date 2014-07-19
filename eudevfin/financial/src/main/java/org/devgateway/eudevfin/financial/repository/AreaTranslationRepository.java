/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.financial.repository;

import java.util.List;

import org.devgateway.eudevfin.metadata.common.domain.translation.AreaTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaTranslationRepository extends
		JpaRepository<AreaTranslation, Long> {

	@Override
	List<AreaTranslation> findAll();
}
