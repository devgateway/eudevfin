/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.common.service;

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
