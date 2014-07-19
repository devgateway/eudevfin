/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.iati.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/**
 * @author alexandru-m-g
 *
 */
@XStreamAlias("sector")
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"value"})
public class Sector extends CodeVocabSignEntity {
	
	public Sector(){}
	public Sector(final String vocab, final String code, final String value) {
		super();
		this.value = value;
		this.setVocabulary(vocab);
		this.setCode(code);
	}

	private String value;

	public String getValue() {
		return this.value;
	}

	public void setValue(final String value) {
		this.value = value;
	}
	
}
