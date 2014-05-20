package org.devgateway.eudevfin.financial.test.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultTrackingModifiedEntitiesRevisionEntity;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/financialContext.xml",
		"classpath:/META-INF/commonContext.xml", "classpath:/META-INF/commonMetadataContext.xml",
		"classpath:META-INF/commonFinancialContext.xml","classpath:testFinancialContext.xml" })
@TransactionConfiguration(defaultRollback=false, transactionManager="transactionManager")
public class RevisionsTest extends AbstractStorageTest{
	
	public static Logger logger	= Logger.getLogger(RevisionsTest.class.getName());
	
	@Autowired
	EntityManagerFactory entityManagerFactory;
	
	@Autowired
	StorageHelper storageHelper;
	
	AuditReader auditReader	= null;
	
	@Before
	public void setupAuditReader() {
		//Getting the AuditReader object to be able to read revisions
		this.auditReader = AuditReaderFactory.get( this.entityManagerFactory.createEntityManager() );
	}
	
	@Test
	public void testQueryRevByTypeAndId() {
		final Long modifiedTxId	= this.storageHelper.modifyOrgAndAmountOfTransaction();
		
		// Querying for the revisions when a FinancialTransaction with id modifiedTxId was modified
		final AuditQuery auditQuery	= this.auditReader.createQuery().forRevisionsOfEntity(FinancialTransaction.class, false, false)
					.add( AuditEntity.id().eq(modifiedTxId) );
		final List<Object []> auditObjects	= auditQuery.getResultList();
		
		assertTrue(auditObjects.size() > 0);
		
		for ( final Object [] oArray: auditObjects ) {
			final FinancialTransaction fTransaction								= (FinancialTransaction) oArray[0]; 
			assertNotNull(fTransaction.getExtendingAgency());
			final DefaultTrackingModifiedEntitiesRevisionEntity trackingObject 	= (DefaultTrackingModifiedEntitiesRevisionEntity) oArray[1];
			logger.info(String.format("For transaction with id %d and amount %f and organization %s.Revision numbers is %d. Modified entities: %s ", 
					fTransaction.getId(), fTransaction.getAmount().doubleValue(),fTransaction.getExtendingAgency().getName(), 
					trackingObject.getId(), trackingObject.getModifiedEntityNames()));
			assertTrue( trackingObject.getModifiedEntityNames().contains(FinancialTransaction.class.getName()) );
		}
		final DefaultTrackingModifiedEntitiesRevisionEntity trackingObject 	= (DefaultTrackingModifiedEntitiesRevisionEntity) auditObjects.get(auditObjects.size()-1)[1];
		
		// Last revision should show that also an Organization entity was modified in the same revision
		assertTrue( trackingObject.getModifiedEntityNames().contains(Organization.class.getName()) );
	}
	
	/**
	 * Showing 2 ways of getting a revision number. The test compares these 2 revision numbers.
	 */
	@Test
	public void testRevisionNumbers() {
		final Long modifiedTxId	= this.storageHelper.modifyOrgAndAmountOfTransaction();
		

		
		AuditQuery auditQuery	= this.auditReader.createQuery().forRevisionsOfEntity(FinancialTransaction.class, false, false)
					.add( AuditEntity.id().eq(modifiedTxId) )
					.add( AuditEntity.property("donorProjectNumber").hasNotChanged() )
					.addProjection( AuditEntity.revisionNumber().max() );
		final Number num1				=  (Number) auditQuery.getSingleResult();
		logger.info("Last revision for a financial tx with unchanged description is: " + num1);
		
		auditQuery	= this.auditReader.createQuery().forRevisionsOfEntity(Organization.class, false, false)
					.add( AuditEntity.property("code").hasChanged() )
					.add( AuditEntity.revisionNumber().maximize());
			final Object [] oArray	= (Object[]) auditQuery.getSingleResult();
		final DefaultTrackingModifiedEntitiesRevisionEntity trackingObject 	= (DefaultTrackingModifiedEntitiesRevisionEntity) oArray[1];
		logger.info("Last revision for an org with changed name is: " + trackingObject.getId() );
		
		assertEquals(num1, trackingObject.getId());
		
		
		
	}
}
