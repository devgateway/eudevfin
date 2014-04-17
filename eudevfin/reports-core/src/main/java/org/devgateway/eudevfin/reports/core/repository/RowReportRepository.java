package org.devgateway.eudevfin.reports.core.repository;

import java.util.List;

import org.devgateway.eudevfin.reports.core.domain.RowReport;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RowReportRepository extends PagingAndSortingRepository<RowReport, Long> {
	RowReport findByName(String name);
	List<RowReport> findByReportName(String reportName);
}
