package org.devgateway.eudevfin.financial.translate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.devgateway.eudevfin.financial.AbstractTranslateable;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"parent_id","locale"}))
public abstract class AbstractTranslation<P extends AbstractTranslateable<? extends AbstractTranslation<P>>> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long id;
	
	@CreatedBy
	private String createdBy;
	@CreatedDate
	private Date createdDate;
	@LastModifiedBy
	private String modifiedBy;
	@LastModifiedDate
	private Date modfiedDate;
	
	@Column(name="locale")
	protected String locale ;
	
	@ManyToOne(  optional= false )
	@JoinColumn(name="parent_id")
	private P parent;
	
	public Object get(String property) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(this);
		return beanWrapper.getPropertyValue(property);
	}
	
	public void set(String property, Object value) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(this);
		beanWrapper.setPropertyValue(property, value);
	}

	
	public P getParent() {
		return parent;
	}

	public void setParent(P parent) {
		this.parent = parent;
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

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModfiedDate() {
		return modfiedDate;
	}

	public void setModfiedDate(Date modfiedDate) {
		this.modfiedDate = modfiedDate;
	}

	
}