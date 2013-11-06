package org.devgateway.eudevfin.financial.test.storage;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.dao.FinancialTransactionDaoImpl;
import org.devgateway.eudevfin.financial.dao.OrganizationDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * This class is marked as @Transactional. This means several things:
 * <ol>
 * 	<li>All functions of this class will run inside a transation</li>
 * 	<li>AOP kicks in and creates a proxy for this class, meaning you can't use @Autowired on constructors</li>
 * </ol>
 * @author Alex
 */ 
@Component @Transactional
public class StorageHelper {

	@Autowired
	FinancialTransactionDaoImpl txDao;
	@Autowired
	OrganizationDaoImpl orgDao;
	

	/**
	 * This function changes an organization and a financial-transaction in a single transaction.
	 * Used in a test to see that both changed entity types are in the same revision.
	 *  
	 * @return the id of the modified transaction
	 */
	public Long modifyOrgAndAmountOfTransaction() {
		List<FinancialTransaction> allTx	= txDao.findAllAsList();
		List<Organization> allOrgs			= orgDao.findAllAsList();
		FinancialTransaction modifyingTx	= allTx.get(0);
		modifyingTx.setAmount(new BigDecimal(999));
		
		for (Organization o: allOrgs) {
			if ( !modifyingTx.getSourceOrganization().getId().equals(o.getId()) ) {
				o.setName(o.getName() + " !! modified");
				modifyingTx.setSourceOrganization(o);
				orgDao.save(o);
				break;
			}
		} 
		
		txDao.save(modifyingTx);
		
		return modifyingTx.getId();
	}
}