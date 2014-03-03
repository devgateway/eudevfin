/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.util;

import java.util.ArrayList;
import java.util.List;

import org.devgateway.eudevfin.sheetexp.dto.DefaultEntityWrapper;
import org.devgateway.eudevfin.sheetexp.dto.EntityWrapperInterface;
import org.joda.time.LocalDateTime;

/**
 * @author Alex
 * 
 */
public class EntityWrapperListHelper<T> {
	private final List<T> sourceList;

	private final String reportType;

	private final LocalDateTime processStartTime;

	private final String locale;

	public EntityWrapperListHelper(final List<T> sourceList, final String reportType, final LocalDateTime processStartTime,
			final String locale) {
		this.sourceList = sourceList;
		this.reportType = reportType;
		this.processStartTime = processStartTime;
		this.locale = locale;
	}

	public EntityWrapperListHelper(final List<T> sourceList, final String reportType, final String locale) {
		this(sourceList, reportType, LocalDateTime.now(), locale);
	}

	public List<EntityWrapperInterface<?>> getWrappedList() {
		final List<EntityWrapperInterface<?>> ret = new ArrayList<EntityWrapperInterface<?>>();
		if (this.sourceList != null) {
			this.addToWrappedList(ret);
			return ret;
		} else {
			return null;
		}

	}

	public void addToWrappedList(final List<EntityWrapperInterface<?>> list) {
		if (this.sourceList != null) {
			for (final T item : this.sourceList) {
				final DefaultEntityWrapper<T> wrappedItem = new DefaultEntityWrapper<T>(this.reportType, this.processStartTime,
						this.locale, item);
				list.add(wrappedItem);
			}
		}
	}

	public LocalDateTime getProcessStartTime() {
		return this.processStartTime;
	}

}
