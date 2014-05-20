
package org.devgateway.eudevfin.reports.core.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.Generated;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "colIndex",
    "colType",
    "colName"
})
public class Metadatum implements Serializable {

    @JsonProperty("colIndex")
    private Integer colIndex;
    @JsonProperty("colType")
    private String colType;
    @JsonProperty("colName")
    private String colName;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("colIndex")
    public Integer getColIndex() {
        return colIndex;
    }

    @JsonProperty("colIndex")
    public void setColIndex(Integer colIndex) {
        this.colIndex = colIndex;
    }

    @JsonProperty("colType")
    public String getColType() {
        return colType;
    }

    @JsonProperty("colType")
    public void setColType(String colType) {
        this.colType = colType;
    }

    @JsonProperty("colName")
    public String getColName() {
        return colName;
    }

    @JsonProperty("colName")
    public void setColName(String colName) {
        this.colName = colName;
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
