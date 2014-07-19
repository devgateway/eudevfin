/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
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
