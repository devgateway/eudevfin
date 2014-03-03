
package org.devgateway.eudevfin.cda.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    "queryInfo",
    "resultset",
    "metadata"
})
public class QueryResult {

    @JsonProperty("queryInfo")
    private QueryInfo queryInfo;
    @JsonProperty("resultset")
    private List<List<String>> resultset = new ArrayList<List<String>>();
    @JsonProperty("metadata")
    private List<Metadatum> metadata = new ArrayList<Metadatum>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("queryInfo")
    public QueryInfo getQueryInfo() {
        return queryInfo;
    }

    @JsonProperty("queryInfo")
    public void setQueryInfo(QueryInfo queryInfo) {
        this.queryInfo = queryInfo;
    }

    @JsonProperty("resultset")
    public List<List<String>> getResultset() {
        return resultset;
    }

    @JsonProperty("resultset")
    public void setResultset(List<List<String>> resultset) {
        this.resultset = resultset;
    }

    @JsonProperty("metadata")
    public List<Metadatum> getMetadata() {
        return metadata;
    }

    @JsonProperty("metadata")
    public void setMetadata(List<Metadatum> metadata) {
        this.metadata = metadata;
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
