/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.jadira.usertype.exchangerate;

import java.util.Arrays;
import java.util.List;

public final class ExchangeRateConstants {

	public static final String SOURCE_OECD = "OECD";
	public static final String SOURCE_NATIONAL = "National";
	public static final String SOURCE_INTERNET= "Internet";

	public static final List<String> all = Arrays.asList(new String[] { SOURCE_OECD, SOURCE_NATIONAL,
			SOURCE_INTERNET});

}
