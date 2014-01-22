/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dashboard.components;

import java.util.Arrays;
import java.util.List;

/**
 * @author aartimon
 * @since 13/12/13
 */
public class TableDefinitionParameters {
    private List<String> colHeaders = Arrays.asList("Col 1", "Col 2", "Amount");
    private List<String> colTypes = Arrays.asList("numeric", "string", "numeric");
    private List<String> colFormats = Arrays.asList("%d", "%s", "%d");
    private List<String> colWidths = Arrays.asList("30%", "40%", "30%");
    private List<Boolean> colSortable = Arrays.asList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
    private Boolean paginate = Boolean.TRUE;
    private Boolean filter = Boolean.TRUE;
    private Boolean info = Boolean.TRUE;
    private String dataAccessId = "";

    private String tableStyle = "themeroller";
    private String paginationType = "full_numbers";
    private Boolean paginateServerside = Boolean.FALSE;
    private Boolean sort = Boolean.TRUE;
    private Boolean lengthChange = Boolean.FALSE;
    private Integer displayLength = 10;
//    private String sDom = "T<'clear'>lfrtip";
    private String path = "/some/path";

	public List<String> getColHeaders() {
		return colHeaders;
	}

	public void setColHeaders(List<String> colHeaders) {
		this.colHeaders = colHeaders;
	}

	public List<String> getColTypes() {
		return colTypes;
	}

	public void setColTypes(List<String> colTypes) {
		this.colTypes = colTypes;
	}

	public List<String> getColFormats() {
		return colFormats;
	}

	public void setColFormats(List<String> colFormats) {
		this.colFormats = colFormats;
	}

	public List<String> getColWidths() {
		return colWidths;
	}

	public void setColWidths(List<String> colWidths) {
		this.colWidths = colWidths;
	}

	public List<Boolean> getColSortable() {
		return colSortable;
	}

	public void setColSortable(List<Boolean> colSortable) {
		this.colSortable = colSortable;
	}

	public Boolean getPaginate() {
		return paginate;
	}

	public void setPaginate(Boolean paginate) {
		this.paginate = paginate;
	}

	public Boolean getFilter() {
		return filter;
	}

	public void setFilter(Boolean filter) {
		this.filter = filter;
	}

	public Boolean getInfo() {
		return info;
	}

	public void setInfo(Boolean info) {
		this.info = info;
	}

	public String getDataAccessId() {
		return dataAccessId;
	}

	public void setDataAccessId(String dataAccessId) {
		this.dataAccessId = dataAccessId;
	}

	public String getTableStyle() {
		return tableStyle;
	}

	public void setTableStyle(String tableStyle) {
		this.tableStyle = tableStyle;
	}

	public String getPaginationType() {
		return paginationType;
	}

	public void setPaginationType(String paginationType) {
		this.paginationType = paginationType;
	}

	public Boolean getPaginateServerside() {
		return paginateServerside;
	}

	public void setPaginateServerside(Boolean paginateServerside) {
		this.paginateServerside = paginateServerside;
	}

	public Boolean getSort() {
		return sort;
	}

	public void setSort(Boolean sort) {
		this.sort = sort;
	}

	public Boolean getLengthChange() {
		return lengthChange;
	}

	public void setLengthChange(Boolean lengthChange) {
		this.lengthChange = lengthChange;
	}

	public Integer getDisplayLength() {
		return displayLength;
	}

	public void setDisplayLength(Integer displayLength) {
		this.displayLength = displayLength;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
