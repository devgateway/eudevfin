/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.reports.ui.components;

import com.google.gson.Gson;
import org.devgateway.eudevfin.reports.core.domain.QueryResult;

import java.io.Serializable;

/**
 * POJO for the base parameters to configure the reports entities
 * This is going to be transformed into a JSON and passed to the JS init function of the entity being built
 *
 * @author idobre
 * @since 6/17/14
 */

public class BaseParameters implements Serializable {
    private String id;
    private int numberOfSeries;
    private String Series1;
    private String Series2;
    private Boolean useMillion;
    private QueryResult result;

    public BaseParameters(String id) {
        this.id = id;
        this.useMillion = false;
    }

    public BaseParameters(String id, QueryResult result) {
        this.id = id;
        this.result = result;
        this.useMillion = false;
    }

    public String toJson() {
        Gson gson = new Gson();
        String ret = gson.toJson(getInstance());

        return ret;
    }

    protected BaseParameters getInstance() {
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeries2() {
        return Series2;
    }

    public void setSeries2(String series2) {
        Series2 = series2;
    }

    public String getSeries1() {
        return Series1;
    }

    public void setSeries1(String series1) {
        Series1 = series1;
    }

    public int getNumberOfSeries() {
        return numberOfSeries;
    }

    public void setNumberOfSeries(int numberOfSeries) {
        this.numberOfSeries = numberOfSeries;
    }

    public Boolean getUseMillion() {
        return useMillion;
    }

    public void setUseMillion(Boolean useMillion) {
        this.useMillion = useMillion;
    }

    public QueryResult getResult() {
        return result;
    }

    public void setResult(QueryResult result) {
        this.result = result;
    }
}
