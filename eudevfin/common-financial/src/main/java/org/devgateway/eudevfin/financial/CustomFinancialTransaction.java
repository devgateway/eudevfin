/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.financial;

import org.joda.money.BigMoney;
import org.joda.time.LocalDateTime;

public class CustomFinancialTransaction extends FinancialTransaction {
    private Boolean CPA;
    private BigMoney futureDebtPrincipal;
    private BigMoney futureDebtInterest;

    private LocalDateTime dataAsPerDate;


    public LocalDateTime getDataAsPerDate() {
        return dataAsPerDate;
    }

    public void setDataAsPerDate(LocalDateTime dataAsPerDate) {
        this.dataAsPerDate = dataAsPerDate;
    }

    public Boolean getCPA() {
        return CPA;
    }

    public void setCPA(Boolean CPA) {
        this.CPA = CPA;
    }

    public BigMoney getFutureDebtPrincipal() {
        return futureDebtPrincipal;
    }

    public void setFutureDebtPrincipal(BigMoney futureDebtPrincipal) {
        this.futureDebtPrincipal = futureDebtPrincipal;
    }

    public BigMoney getFutureDebtInterest() {
        return futureDebtInterest;
    }

    public void setFutureDebtInterest(BigMoney futureDebtInterest) {
        this.futureDebtInterest = futureDebtInterest;
    }
}
