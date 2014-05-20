package org.devgateway.eudevfin.importing.metadata.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ImportedFile {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id = null;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date modfiedDate;
    
    @Column(unique=true)
    private String filename;
    
    @Column(unique=true)
    private String hashcode;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(final Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModfiedDate() {
		return modfiedDate;
	}

	public void setModfiedDate(final Date modfiedDate) {
		this.modfiedDate = modfiedDate;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(final String filename) {
		this.filename = filename;
	}

	public String getHashcode() {
		return hashcode;
	}

	public void setHashcode(final String hashcode) {
		this.hashcode = hashcode;
	}
    
    
    
    
}
