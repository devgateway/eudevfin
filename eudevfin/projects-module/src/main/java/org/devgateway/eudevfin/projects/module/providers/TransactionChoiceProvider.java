/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.providers;

import com.vaynberg.wicket.select2.Response;
import java.util.ArrayList;
import java.util.List;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.ui.common.providers.AbstractTranslatableProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author alcr
 */
@Component
public class TransactionChoiceProvider extends AbstractTranslatableProvider<CustomFinancialTransaction> {

    private List<CustomFinancialTransaction> filteredTransactions;

    @Autowired
    private CustomFinancialTransactionService transactionService;

    public TransactionChoiceProvider(List<CustomFinancialTransaction> filteredAreas, CustomFinancialTransactionService transactionService) {
        this.filteredTransactions = filteredAreas;
        this.transactionService = transactionService;
    }

    public TransactionChoiceProvider() {
        super();
    }

    @Override
    public void query(final String term, final int page, final Response<CustomFinancialTransaction> response) {
        final List<CustomFinancialTransaction> ret = new ArrayList<>();
        List<CustomFinancialTransaction> values;
        if (this.filteredTransactions != null && this.filteredTransactions.size() > 0) {
            values = this.filteredTransactions;
        } else {
            values = new ArrayList<>();
        }

        for (final CustomFinancialTransaction el : values) {
            if (getDisplayText(el).toLowerCase().contains(term)) {
                ret.add(el);
            }
        }
        response.addAll(ret);
    }

    @Override
    protected BaseEntityService<CustomFinancialTransaction> getService() {
        return transactionService;
    }

    @Override
    public String getDisplayText(CustomFinancialTransaction choice) {
        return choice.getShortDescription();
    }

    @Override
    public void detach() {
        //Spring component no need to detach if added into wicket components with @SpringBean
    }
}
