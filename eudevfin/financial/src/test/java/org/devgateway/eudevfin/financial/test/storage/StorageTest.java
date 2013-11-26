/**
 * 
 */
package org.devgateway.eudevfin.financial.test.storage;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.dao.FinancialTransactionDaoImpl;
import org.devgateway.eudevfin.financial.dao.OrganizationDaoImpl;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultTrackingModifiedEntitiesRevisionEntity;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * Please note that since this tests are not marked as @Transactional the changes to the underlying database 
 * will be persisted. ( Also if a test is marked as @Transactional then by default JUNIT will rollback the changes 
 * at the end of the test; marking it with defaultRollback=false alters this behaviour ).
 * 
 * @author Alex
 *
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/financialContext.xml",
		"classpath:META-INF/commonFinancialContext.xml","classpath:testFinancialContext.xml" })
@TransactionConfiguration(defaultRollback=false, transactionManager="transactionManager")
public class StorageTest extends AbstractStorageTest{
	
	
	public static Logger logger	= Logger.getLogger(StorageTest.class.getName());
	
	

	@Test
	public void testSave() {
		assertEquals( NUM_OF_TXS, txDao.findAllAsList().size() );
		assertEquals( NUM_OF_ORGS, orgDao.findAllAsList().size() );
		
	}
	
	@Test  (expected = DataIntegrityViolationException.class)
	public void testDeleteOrganization() {
		List<Organization> allOrgs	= orgDao.findAllAsList();
		Organization org1	= allOrgs.get(0);
		
		orgDao.delete(org1);
	}
	
	@Test
	public void testDeleteTransaction() {
		txDao.delete( txDao.findAll() );
		assertEquals(NUM_OF_ORGS, orgDao.findAllAsList().size() );
	}
	

}
