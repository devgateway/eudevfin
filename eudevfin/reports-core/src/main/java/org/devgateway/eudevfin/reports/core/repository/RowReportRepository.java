/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.reports.core.repository;

import java.util.List;

import org.devgateway.eudevfin.reports.core.domain.RowReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RowReportRepository extends JpaRepository<RowReport, Long> {
	RowReport findByName(String name);
	List<RowReport> findByReportName(String reportName);
}
