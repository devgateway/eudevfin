package org.jadira.usertype.exchangerate;

import java.util.Arrays;
import java.util.List;

public final class ExchangeRateConstants {

	public static final String SOURCE_OECD = "OECD";
	public static final String SOURCE_CUSTOM = "Custom";
	public static final String SOURCE_OPENEXCHANGE = "OpenExchangeRates.Org";

	public static final List<String> all = Arrays.asList(new String[] { SOURCE_OECD, SOURCE_CUSTOM,
			SOURCE_OPENEXCHANGE });

}
