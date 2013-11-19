
package org.devgateway.eudevfin.cda.domain;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import org.apache.commons.lang.builder.ToStringBuilder;

public class QueryResult {

    @Expose
    private QueryInfo queryInfo;
    @Expose
    private List<List<String>> resultset = new ArrayList<List<String>>();
    @Expose
    private List<Metadatum> metadata = new ArrayList<Metadatum>();

    public QueryInfo getQueryInfo() {
        return queryInfo;
    }

    public void setQueryInfo(QueryInfo queryInfo) {
        this.queryInfo = queryInfo;
    }

    public List<List<String>> getResultset() {
        return resultset;
    }

    public void setResultset(List<List<String>> resultset) {
        this.resultset = resultset;
    }

    public List<Metadatum> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<Metadatum> metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
