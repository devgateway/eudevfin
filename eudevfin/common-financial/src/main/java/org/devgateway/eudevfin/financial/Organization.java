package org.devgateway.eudevfin.financial;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.envers.Audited;

@Entity @Audited
public class Organization {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long id = null;
	String name;
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
	@Override
	public String toString() {
		return String.format("Org name is %s, id %d",  this.name, this.id);
	}
	
	
	
}
