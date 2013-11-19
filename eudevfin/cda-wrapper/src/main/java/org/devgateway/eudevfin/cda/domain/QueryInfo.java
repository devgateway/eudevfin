
package org.devgateway.eudevfin.cda.domain;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("com.googlecode.jsonschema2pojo")
public class QueryInfo {

    @Expose
    private String totalRows;

    public String getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(String totalRows) {
        this.totalRows = totalRows;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
