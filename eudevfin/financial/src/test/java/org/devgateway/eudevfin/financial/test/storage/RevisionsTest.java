package org.devgateway.eudevfin.financial.test.storage;

import static org.junit.Assert.*;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.Organization;
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
		"classpath:META-INF/commonFinancialContext.xml" })
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
		this.auditReader = AuditReaderFactory.get( entityManagerFactory.createEntityManager() );
	}
	
	@Test
	public void testQueryRevByTypeAndId() {
		Long modifiedTxId	= storageHelper.modifyOrgAndAmountOfTransaction();
		
		// Querying for the revisions when a FinancialTransaction with id modifiedTxId was modified
		AuditQuery auditQuery	= auditReader.createQuery().forRevisionsOfEntity(FinancialTransaction.class, false, false)
					.add( AuditEntity.id().eq(modifiedTxId) );
		List<Object []> auditObjects	= auditQuery.getResultList();
		
		assertTrue(auditObjects.size() > 0);
		
		for ( Object [] oArray: auditObjects ) {
			FinancialTransaction fTransaction								= (FinancialTransaction) oArray[0]; 
			assertNotNull(fTransaction.getSourceOrganization());
			DefaultTrackingModifiedEntitiesRevisionEntity trackingObject 	= (DefaultTrackingModifiedEntitiesRevisionEntity) oArray[1];
			logger.info(String.format("For transaction with id %d and amount %f and organization %s.Revision numbers is %d. Modified entities: %s ", 
					fTransaction.getId(), fTransaction.getAmount().doubleValue(),fTransaction.getSourceOrganization().getName(), 
					trackingObject.getId(), trackingObject.getModifiedEntityNames()));
			assertTrue( trackingObject.getModifiedEntityNames().contains(FinancialTransaction.class.getName()) );
		}
		DefaultTrackingModifiedEntitiesRevisionEntity trackingObject 	= (DefaultTrackingModifiedEntitiesRevisionEntity) auditObjects.get(auditObjects.size()-1)[1];
		
		// Last revision should show that also an Organization entity was modified in the same revision
		assertTrue( trackingObject.getModifiedEntityNames().contains(Organization.class.getName()) );
	}
	
	/**
	 * Showing 2 ways of getting a revision number. The test compares these 2 revision numbers.
	 */
	@Test
	public void testRevisionNumbers() {
		Long modifiedTxId	= storageHelper.modifyOrgAndAmountOfTransaction();
		

		
		AuditQuery auditQuery	= auditReader.createQuery().forRevisionsOfEntity(FinancialTransaction.class, false, false)
					.add( AuditEntity.id().eq(modifiedTxId) )
					.add( AuditEntity.property("donorProjectNumber").hasNotChanged() )
					.addProjection( AuditEntity.revisionNumber().max() );
		Number num1				=  (Number) auditQuery.getSingleResult();
		logger.info("Last revision for a financial tx with unchanged description is: " + num1);
		
		auditQuery	= auditReader.createQuery().forRevisionsOfEntity(Organization.class, false, false)
					.add( AuditEntity.property("code").hasChanged() )
					.add( AuditEntity.revisionNumber().maximize());
			Object [] oArray	= (Object[]) auditQuery.getSingleResult();
		DefaultTrackingModifiedEntitiesRevisionEntity trackingObject 	= (DefaultTrackingModifiedEntitiesRevisionEntity) oArray[1];
		logger.info("Last revision for an org with changed name is: " + trackingObject.getId() );
		
		assertEquals(num1, trackingObject.getId());
		
		
		
	}
}
