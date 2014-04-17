package org.devgateway.eudevfin.reports.core.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
import org.devgateway.eudevfin.reports.core.utils.Constants;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

@Entity
public class RowReport implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2918246983336492166L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long id=null;
	
	@Index(name="row_name_idx")
	private String name;

	private String reportName;

	private int type;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> categories = new HashSet<String>();

	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<ColumnReport> columns = new HashSet<ColumnReport>();

	@ElementCollection(fetch = FetchType.EAGER)
	@Column(length = 1000)
	private Set<String> rowCodes;

	
	public RowReport(){
		
	}
	
	public RowReport(String reportName, String name, int type) {
		this.setReportName(reportName);
		this.setName(name);
		this.setType(type);
	}
	
	public RowReport(String reportName, String name, int type, Set<String> rowCodes) {
		this.setReportName(reportName);
		this.setName(name);
		this.setType(type);
		this.setRowCodes(rowCodes);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Set<ColumnReport> getColumns() {
		return columns;
	}

	public void setColumns(Set<ColumnReport> columns) {
		this.columns = columns;
	}

	public Set<String> getCategories() {
		return categories;
	}

	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}

	public String getFormula() {
		StringBuffer formula = new StringBuffer();
		this.categories.removeAll(Collections.singleton(""));

		for(Iterator<String> it = this.categories.iterator();it.hasNext();){
			String currentString = it.next();
			formula.append(currentString);
			if(it.hasNext()){
				formula.append("*");
			}
		}
		return formula.toString();
	}
	public Set<String> getRowCodes() {
		return rowCodes;
	}
	public void setRowCodes(Set<String> rowCodes) {
		this.rowCodes = rowCodes;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
}
