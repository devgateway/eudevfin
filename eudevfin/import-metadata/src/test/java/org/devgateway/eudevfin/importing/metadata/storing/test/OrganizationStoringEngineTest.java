/**
 * 
 */
package org.devgateway.eudevfin.importing.metadata.storing.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
import org.devgateway.eudevfin.financial.dao.OrganizationDaoImpl;
import org.devgateway.eudevfin.importing.metadata.storing.OrganizationStoringEngine;
import org.devgateway.eudevfin.importing.metadata.test.helper.DbHelper;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alex
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/commonContext.xml",
		"classpath:/META-INF/financialContext.xml",
		"classpath:META-INF/commonFinancialContext.xml","classpath:testImportMetadataContext.xml" })
@Component  
public class OrganizationStoringEngineTest {
	
	@Autowired
	CategoryDaoImpl categoryDaoImpl;
	
	@Autowired
	OrganizationDaoImpl orgDaoImpl;
	
	@Autowired
	DbHelper dbHelper;
	
	@Autowired
	OrganizationStoringEngine orgStoringEngine;
	
	private Organization createOrganization(final int index) {
		final Organization o	= new Organization();
		
		o.setCode("testORgCode" + index);
		o.setName("Test Org " + index);
		o.setAcronym("Test Org acronym " + index);
		
		o.setDonorCode("testDonorCode" + index);
		o.setDonorName("Test Donor Name " + index );
		
		return this.orgDaoImpl.save(o).getEntity();
	}
	
	private void cleanDb() {
		final List<Organization> orgs	= this.orgDaoImpl.findAllAsList();
		if ( orgs != null ) {
			for (final Organization org : orgs) {
				this.orgDaoImpl.delete(org);;
			}
		}
	}
	
	/**
	 * Test method for {@link org.devgateway.eudevfin.importing.metadata.storing.orgStoringEngine#findEquivalentEntity(org.devgateway.eudevfin.metadata.common.domain.Area)}.
	 */
	@Test
	@Transactional
	public void test() {
		final Organization o1	= this.createOrganization(1);
		final Organization o2	= new Organization();
		o2.setCode(o1.getCode());
		o2.setDonorCode("o2DonorCode");
		
		final Organization o2FromDb	= this.orgStoringEngine.findEquivalentEntity(o2);
		assertNull(o2FromDb);
		
		final Organization o1Clone	= new Organization();
		o1Clone.setCode(o1.getCode());
		o1Clone.setDonorCode(o1.getDonorCode());
		
		final Organization o1FromDb	= this.orgStoringEngine.findEquivalentEntity(o1Clone);
		assertNotNull(o1FromDb);
		
		assertTrue( this.orgStoringEngine.checkSame(o1, o1FromDb) );
		assertFalse( this.orgStoringEngine.checkSame(o1, o1Clone) );
		
		o1Clone.setName(o1.getName());
		o1Clone.setDonorName(o1.getDonorName());
		o1Clone.setAcronym(o1.getAcronym());
		
		
		assertTrue( this.orgStoringEngine.checkSame(o1, o1Clone) );
		
		this.cleanDb();
		
	}

}
