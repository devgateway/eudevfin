/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.sheetexp.iati.domain;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author alexandru-m-g
 *
 */
public class CodeVocabSignEntity {
	
	@XStreamAsAttribute
	private String vocabulary;
	
	@XStreamAsAttribute
	private String significance;
	
	@XStreamAsAttribute
	private String code;

	public String getVocabulary() {
		return this.vocabulary;
	}

	public void setVocabulary(final String vocabulary) {
		this.vocabulary = vocabulary;
	}

	public String getSignificance() {
		return this.significance;
	}

	public void setSignificance(final String significance) {
		this.significance = significance;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

}
