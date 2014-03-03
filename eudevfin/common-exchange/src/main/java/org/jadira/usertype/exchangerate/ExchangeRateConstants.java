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
