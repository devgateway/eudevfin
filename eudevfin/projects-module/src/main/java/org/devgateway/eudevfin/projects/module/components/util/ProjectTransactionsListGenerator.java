/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.components.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.devgateway.eudevfin.common.service.PagingHelper;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

/**
 *
 * @author alcr
 */
public class ProjectTransactionsListGenerator implements ListGeneratorInterface<FinancialTransaction> {

    private static final long serialVersionUID = 1L;
    private List<FinancialTransaction> list = new ArrayList<>();

    public ProjectTransactionsListGenerator(Set<FinancialTransaction> list) {
        this.list.addAll(list);
        
        Collections.sort(this.list, new Comparator<FinancialTransaction>() {
            @Override
            public int compare(FinancialTransaction o1, FinancialTransaction o2) {
                return o2.getId().compareTo(o1.getId());
            }
        });
    }

    /* (non-Javadoc)
     * @see org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface#getResultsList(int, int)
     */
    @Override
    public PagingHelper<FinancialTransaction> getResultsList(int pageNumber,
            int pageSize) {
        return PagingHelper.createPagingHelperFromPage(findTransactionsByProjectIDPageable(new PageRequest(pageNumber - 1, pageSize)));
    }

    //TODO Refactor with Results, Reports, Transactions
    private Page<FinancialTransaction> findTransactionsByProjectIDPageable(Pageable pageable) {
        int size = pageable.getPageSize();
        int offset = pageable.getOffset();

        List<FinancialTransaction> trans = new ArrayList<>();

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
        PageImpl<FinancialTransaction> result;
        result = new PageImpl<>(trans, pageable, this.list.size());

        return result;
    }
}
