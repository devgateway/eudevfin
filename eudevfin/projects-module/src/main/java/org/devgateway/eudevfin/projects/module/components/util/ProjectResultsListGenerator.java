/**
 * *****************************************************************************
 * Copyright (c) 2015 ENEA SERVICES ROMANIA All rights reserved. This program
 * and the accompanying materials are made available under the terms of the GNU
 * Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * *****************************************************************************
 */
/**
 *
 */
package org.devgateway.eudevfin.projects.module.components.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.devgateway.eudevfin.common.service.PagingHelper;
import org.devgateway.eudevfin.projects.common.entities.ProjectResult;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class ProjectResultsListGenerator implements ListGeneratorInterface<ProjectResult> {

    private static final long serialVersionUID = 1L;
    private List<ProjectResult> list = new ArrayList<>();

    public ProjectResultsListGenerator(Set<ProjectResult> list) {
        this.list.addAll(list);
        
        Collections.sort(this.list, new Comparator<ProjectResult>() {
            @Override
            public int compare(ProjectResult o1, ProjectResult o2) {
                return o2.getId().compareTo(o1.getId());
            }
        });
    }

    /* (non-Javadoc)
     * @see org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface#getResultsList(int, int)
     */
    @Override
    public PagingHelper<ProjectResult> getResultsList(int pageNumber, int pageSize) {
        return PagingHelper.createPagingHelperFromPage(findResultsByProjectIDPageable(new PageRequest(pageNumber - 1, pageSize)));
    }

    private Page<ProjectResult> findResultsByProjectIDPageable(Pageable pageable) {
        int size = pageable.getPageSize();
        int offset = pageable.getOffset();

        List<ProjectResult> trans = new ArrayList<>();

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
        PageImpl<ProjectResult> result;
        result = new PageImpl<>(trans, pageable, this.list.size());

        return result;
    }

}
