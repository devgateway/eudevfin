/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.components.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.devgateway.eudevfin.common.service.PagingHelper;
import org.devgateway.eudevfin.projects.common.entities.ProjectReport;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author alcr
 */
public class ProjectReportsListGenerator implements ListGeneratorInterface<ProjectReport> {

    private static final long serialVersionUID = 1L;
    private List<ProjectReport> list = new ArrayList<>();

    public ProjectReportsListGenerator(Set<ProjectReport> list) {
        this.list.addAll(list);
    }

    /* (non-Javadoc)
     * @see org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface#getResultsList(int, int)
     */
    @Override
    public PagingHelper<ProjectReport> getResultsList(int pageNumber,
            int pageSize) {
        return PagingHelper.createPagingHelperFromPage(findReportsByProjectIDPageable(new PageRequest(pageNumber - 1, pageSize)));
    }

    //TODO Refactor with Results, Reports, Transactions
    private Page<ProjectReport> findReportsByProjectIDPageable(Pageable pageable) {
        int size = pageable.getPageSize();
        int offset = pageable.getOffset();

        List<ProjectReport> trans = new ArrayList<>();

        int end;

        if (size + offset < this.list.size()) {
            end = size + offset;
        } else {
            end = this.list.size();
        }

        for (int i = offset; i < end; i++) {
            trans.add(this.list.get(i));
        }

        @SuppressWarnings("unchecked")
        PageImpl<ProjectReport> result;
        result = new PageImpl<>(trans, pageable, this.list.size());

        return result;
    }
}
