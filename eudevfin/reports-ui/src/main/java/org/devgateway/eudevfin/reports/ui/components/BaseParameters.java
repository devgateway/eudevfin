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
    private QueryResult result;

    public BaseParameters(String id, QueryResult result) {
        this.id = id;
        this.result = result;
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

    public QueryResult getResult() {
        return result;
    }

    public void setResult(QueryResult result) {
        this.result = result;
    }
}
