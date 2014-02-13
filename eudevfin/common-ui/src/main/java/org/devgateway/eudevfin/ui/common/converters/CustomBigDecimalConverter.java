package org.devgateway.eudevfin.ui.common.converters;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.wicket.util.convert.converter.BigDecimalConverter;

/**
 * Custom {@link BigDecimalConverter} of {@link BigDecimal} objects that has a 
 * really generous number of digits. Especially useful because we deal with currencies
 * like HUF
 * 
 * @author mihai
 * @since 13.02.14
 *
 */
public class CustomBigDecimalConverter extends BigDecimalConverter {

	private static final long serialVersionUID = 3571401366527895291L;

	@Override
	public String convertToString(final BigDecimal value, final Locale locale) {
		NumberFormat fmt = getNumberFormat(locale);
		fmt.setMaximumFractionDigits(8); 
		return fmt.format(value);
	}
}