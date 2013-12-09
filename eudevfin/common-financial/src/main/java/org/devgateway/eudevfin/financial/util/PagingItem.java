package org.devgateway.eudevfin.financial.util;

import java.io.Serializable;

public class PagingItem implements Serializable{
	private String label;
	private boolean disabled;
	private int currentPageNo;
	
	public PagingItem() {
	}
	public PagingItem(String label, int currentPageNo, boolean disabled) {
		super();
		this.label = label;
		this.disabled = disabled;
		this.currentPageNo	= currentPageNo;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public int getCurrentPageNo() {
		return currentPageNo;
	}
	public void setCurrentPageNo(int currentPageNo) {
		this.currentPageNo = currentPageNo;
	}
	
	
}
