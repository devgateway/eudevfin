package org.devgateway.eudevfin.reports.core.dao;

import java.util.List;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.reports.core.domain.RowReport;
import org.devgateway.eudevfin.reports.core.repository.RowReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(value=false)
public class RowReportDao
		extends
			AbstractDaoImpl<RowReport, Long, RowReportRepository> {

	@Autowired
	private RowReportRepository repo;

	@Override
	protected RowReportRepository getRepo() {
		return repo;
	}
	public List<RowReport> findByReportName(String reportName) {
		return repo.findByReportName(reportName);
	}
		
}
