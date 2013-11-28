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

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.money.ExchangeRate;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author mihai
 *
 */
@Entity
public class HistoricalExchangeRate implements Serializable {

	private static final long serialVersionUID = -1514542425047805150L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected String id=null;
	
	
	@Columns(columns={@Column(name="base_currency"),@Column(name="counter_currency"),@Column(name="rate")})	
	private ExchangeRate rate;
	
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private LocalDateTime date;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
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
	
	
	

}
