/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.sheetexp.iati.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

@XStreamAlias("participating-org")
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"value"})
public class ParticipatingOrg extends AbstractWithLanguage {
	private String value;

	private String role;

	private String ref;

	public ParticipatingOrg(final String role, final String ref, final String value) {
		super();
		this.value = value;
		this.role = role;
		this.ref = ref;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(final String role) {
		this.role = role;
	}

	public String getRef() {
		return this.ref;
	}

	public void setRef(final String ref) {
		this.ref = ref;
	}

}
