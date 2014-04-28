package org.devgateway.eudevfin.reports.core.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.devgateway.eudevfin.reports.core.utils.Constants;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

@Entity
public class ColumnReport implements Serializable  {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id = null;

	private String measure;
	
	@Column(length = 2000)
	private String slicer;
	private String name;
	private int type = Constants.CALCULATED;

	@ElementCollection(fetch = FetchType.EAGER)
	@Column(length = 4000)
	private Set<String> columnCodes;
	private int multiplier = 1;
	private String pattern = "#,##0.00";

	public ColumnReport() {
	}

	public ColumnReport(String name, int type, String measure, String slicer) {
		this.setName(name);
		this.setType(type);
		this.setMeasure(measure);
		this.setSlicer(slicer);
	}

	public ColumnReport(String name, int type, HashSet<String> hashSet) {
		this.setName(name);
		this.setType(type);
		this.setColumnCodes(hashSet);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMeasure() {
		return measure;
	}

	public void setMeasure(String measure) {
		this.measure = measure;
	}

	public String getSlicer() {
		return slicer;
	}

	public void setSlicer(String slicer) {
		this.slicer = slicer;
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

	public void setColumnCodes(Set<String> columnCodes) {
		this.columnCodes = columnCodes;
	}

	public Set<String> getColumnCodes() {
		return columnCodes;
	}

	public void setMultiplier(int i) {
		multiplier = i;
	}

	public int getMultiplier() {
		return multiplier;
	}

	public String getColumnCode() {
		StringBuffer sb = new StringBuffer();
		String[] types = this.getSlicer().split(",");
		for(int i = 0; i < types.length; i++) {
			sb.append(this.getName() + "_" + shortenType(types[i]) + "_"	+ this.getMeasure());
			if(i != types.length - 1)
				sb.append(",");
		}		
		return sb.toString();
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	private String shortenType(String type) {
		String[] str = type.split("##");
		if(str.length == 2) {
			return str[1].replace("]", "");
		}
		return type;
	}
}
