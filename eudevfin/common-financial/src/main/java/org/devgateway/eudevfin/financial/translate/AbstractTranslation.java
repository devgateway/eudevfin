package org.devgateway.eudevfin.financial.translate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

@MappedSuperclass
public abstract class AbstractTranslation {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long id;
	
	@CreatedBy
	private String createdBy;
	@CreatedDate
	private Date createdDate;
	
	@Column(name="LOCALE")
	protected String locale ;
	
	public Object get(String property) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(this);
		return beanWrapper.getPropertyValue(property);
	}
	
	public void set(String property, Object value) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(this);
		beanWrapper.setPropertyValue(property, value);
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
}
