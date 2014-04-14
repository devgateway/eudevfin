package org.devgateway.eudevfin.dashboard.components;

import java.io.Serializable;

/**
 * @author idobre
 * @since 1/23/14
 */
public class QueryDefinition implements Serializable {
	private String  dataAccessId = "";
	private String path = "/some/path";

	public String getDataAccessId() {
		return dataAccessId;
	}

	public void setDataAccessId(String dataAccessId) {
		this.dataAccessId = dataAccessId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
