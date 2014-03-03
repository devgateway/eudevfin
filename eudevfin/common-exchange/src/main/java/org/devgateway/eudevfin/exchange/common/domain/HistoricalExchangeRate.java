/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    aartimon
 */

/**
 * 
 */
package org.devgateway.eudevfin.exchange.common.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;
import org.hibernate.annotations.Type;
import org.joda.money.CurrencyUnit;
import org.joda.money.ExchangeRate;
import org.joda.time.LocalDateTime;

/**
 * @author mihai
 *
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@javax.persistence.Table(name=HistoricalExchangeRate.TABLE_NAME)
@Table(appliesTo=HistoricalExchangeRate.TABLE_NAME, 
indexes={ @Index(columnNames={HistoricalExchangeRate.BASE_CURRENCY_COLUMN_NAME}, name="historicalexchangerate_base_currecy_idx"), 
		@Index(columnNames={HistoricalExchangeRate.COUNTER_CURRENCY_COLUMN_NAME}, name="historicalexchangerate_counter_currecy_idx")		
})
public class HistoricalExchangeRate implements Serializable {

	public static final String TABLE_NAME="HISTORICALEXCHANGERATE";
	public static final String BASE_CURRENCY_COLUMN_NAME="base_currency";
	public static final String COUNTER_CURRENCY_COLUMN_NAME="counter_currency";
	public static final String RATE_COLUMN_NAME="rate";
	
	private static final long serialVersionUID = -1514542425047805150L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long id=null;
	
	@Index(name="historicalexchangerate_source_idx")
	private String source;	
	
	@Columns(columns={@Column(name=BASE_CURRENCY_COLUMN_NAME),@Column(name=COUNTER_CURRENCY_COLUMN_NAME),@Column(name=RATE_COLUMN_NAME,precision=16,scale=8)})
	@Type(type="org.jadira.usertype.exchangerate.joda.PersistentExchangeRate")
	private ExchangeRate rate;
	
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime date;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the rate
	 */
	public ExchangeRate getRate() {
		return rate;
	}

	/**
	 * @param rate the rate to set
	 */
	public void setRate(ExchangeRate rate) {
		this.rate = rate;
	}

	/**
	 * @return the date
	 */
	public LocalDateTime getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
	
	public HistoricalExchangeRate rate(ExchangeRate rate) {
		this.rate=rate;
		return this;
	}
	

	public static HistoricalExchangeRate of(CurrencyUnit base, CurrencyUnit counter, BigDecimal rate, LocalDateTime date, String source) {
		HistoricalExchangeRate histRate=new HistoricalExchangeRate();
		histRate.setDate(date);
		histRate.setRate(ExchangeRate.of(base, counter, rate));
		histRate.setSource(source);
		return histRate;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	

}
