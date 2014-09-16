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

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/**
 * @author alexandru-m-g
 *
 */
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"value"})
public class ReportingOrg extends AbstractWithLanguage {
	@XStreamAsAttribute
	private String type;
	@XStreamAsAttribute
	private String ref;
	private String value;

	public ReportingOrg(){}

	public ReportingOrg(final String type, final String ref, final String value) {
		super();
		this.type = type;
		this.ref = ref;
		this.value = value;
	}
	public String getType() {
		return this.type;
	}
	public void setType(final String type) {
		this.type = type;
	}
	public String getRef() {
		return this.ref;
	}
	public void setRef(final String ref) {
		this.ref = ref;
	}
	public String getValue() {
		return this.value;
	}
	public void setValue(final String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return this.getValue();
	}




}
