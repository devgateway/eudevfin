/**
 * 
 */
package org.devgateway.eudevfin.financial.auditing;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.envers.DefaultTrackingModifiedEntitiesRevisionEntity;
import org.hibernate.envers.RevisionEntity;
import org.joda.time.LocalDateTime;

/**
 * This replaces the default hibernate revision entity by adding information about the user
 * who "created" a new revision
 * 
 * @author Alex
 *
 */
@Entity
@RevisionEntity(CustomRevisionListener.class)
@Table(name="REVINFO")
public class CustomRevisionEntity  extends DefaultTrackingModifiedEntitiesRevisionEntity {

	private static final long serialVersionUID = 7194486507196863546L;
	
	String username;
	
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime modificationTime;
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalDateTime getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(LocalDateTime modificationTime) {
		this.modificationTime = modificationTime;
	}
	
	
	
}
