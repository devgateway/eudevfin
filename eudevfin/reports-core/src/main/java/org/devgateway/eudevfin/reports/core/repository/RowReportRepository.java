package org.devgateway.eudevfin.reports.core.repository;

import java.util.List;

import org.devgateway.eudevfin.reports.core.domain.RowReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RowReportRepository extends JpaRepository<RowReport, Long> {
	RowReport findByName(String name);
	List<RowReport> findByReportName(String reportName);
}
