/**
 * 
 */
package org.devgateway.eudevfin.financial.auditing;

import org.hibernate.envers.RevisionListener;
import org.joda.time.LocalDateTime;

/**
 * @author Alex
 *
 */
public class CustomRevisionListener implements RevisionListener {
	
	private SimpleAuditorAwareImpl auditor	= new SimpleAuditorAwareImpl();

	/* (non-Javadoc)
	 * @see org.hibernate.envers.RevisionListener#newRevision(java.lang.Object)
	 */
	@Override
	public void newRevision(Object revisionEntity) {
		CustomRevisionEntity revision	= (CustomRevisionEntity) revisionEntity;
		revision.setUsername ( auditor.getCurrentAuditor() );
		revision.setModificationTime( LocalDateTime.now() );

	}

}
