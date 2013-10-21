package org.devgateway.eudevfin.financial;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.devgateway.eudevfin.financial.service.Organization;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
@Entity
@Table(name="FINANCIAL_TRANSACTIONS")
public class FinancialTransaction implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id=null;
	private BigDecimal amount;
	private String description;
	
	@CreatedBy
	private String createdBy;
	@CreatedDate
	private Date createdDate;
	
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional= false )
	@JoinColumn(name="ORGANIZATION_ID")
	private Organization sourceOrganization;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Organization getSourceOrganization() {
		return sourceOrganization;
	}
	public void setSourceOrganization(Organization sourceOrganization) {
		this.sourceOrganization = sourceOrganization;
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
	@Override
	public String toString() {
		return String.format("Id %s, amount %f, description %s", id, amount.doubleValue(), description);
	}

}