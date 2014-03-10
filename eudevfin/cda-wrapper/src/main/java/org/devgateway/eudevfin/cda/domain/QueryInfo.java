
package org.devgateway.eudevfin.cda.domain;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "totalRows"
})
public class QueryInfo {

    @JsonProperty("totalRows")
    private String totalRows;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("totalRows")
    public String getTotalRows() {
        return totalRows;
    }

    @JsonProperty("totalRows")
    public void setTotalRows(String totalRows) {
        this.totalRows = totalRows;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}