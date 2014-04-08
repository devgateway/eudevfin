/**
 * 
 */
package org.devgateway.eudevfin.financial.test.storage;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.logging.Logger;

import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

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
		"classpath:/META-INF/commonContext.xml", "classpath:/META-INF/commonMetadataContext.xml",
		"classpath:META-INF/commonFinancialContext.xml","classpath:testFinancialContext.xml" })
@TransactionConfiguration(defaultRollback=false, transactionManager="transactionManager")
public class StorageTest extends AbstractStorageTest{
	
	
	public static Logger logger	= Logger.getLogger(StorageTest.class.getName());
	
	

	@Test
	public void testSave() {
		assertEquals( NUM_OF_TXS, this.txDao.findAllAsList().size() );
		assertEquals( NUM_OF_ORGS, this.orgDao.findAllAsList().size() );
		
	}
	
	@Test  (expected = DataIntegrityViolationException.class)
	public void testDeleteOrganization() {
		final List<Organization> allOrgs	= this.orgDao.findAllAsList();
		final Organization org1	= allOrgs.get(0);
		
		this.orgDao.delete(org1);
	}
	
	@Test
	public void testDeleteTransaction() {
		this.txDao.delete( this.txDao.findAll() );
		assertEquals(NUM_OF_ORGS, this.orgDao.findAllAsList().size() );
	}
	

}
