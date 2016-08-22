/**
 * *****************************************************************************
 * Copyright (c) 2014 Development Gateway. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the GNU
 * Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************
 */
package org.devgateway.eudevfin.reports.core.dao;

import java.util.List;

import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.reports.core.domain.RowReport;
import org.devgateway.eudevfin.reports.core.repository.RowReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(value = false)
public class RowReportDao extends AbstractDaoImpl<RowReport, Long, RowReportRepository> {

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
