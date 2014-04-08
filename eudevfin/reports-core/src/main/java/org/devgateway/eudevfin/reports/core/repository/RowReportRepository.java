package org.devgateway.eudevfin.reports.core.repository;

import org.devgateway.eudevfin.reports.core.domain.RowReport;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RowReportRepository extends PagingAndSortingRepository<RowReport, Long> {
	RowReport findByName(String name);
}
