/**
 * 
 */
package org.devgateway.eudevfin.exchange.common.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.money.ExchangeRate;
import org.joda.time.LocalDateTime;

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
