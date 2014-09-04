/**
 *
 */
package org.devgateway.eudevfin.importing.transaction.transformation.test;

import java.util.Arrays;
import java.util.List;

import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.importing.transaction.transformers.TransactionRowTransformer;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

/**
 * @author alexandru-m-g
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/commonContext.xml",
		"classpath:/META-INF/financialContext.xml", "classpath:/META-INF/commonMetadataContext.xml",
		"classpath:META-INF/commonFinancialContext.xml","classpath:/META-INF/importMetadataContext.xml" })
@Component
public class ListToTransactionTransformTest {

	// reporting year, commitment date, reporting organization, extending agency,
	// CRS ID, donor project number, nature of submission,
	// recipient country, short description
	List<Object> testList = Arrays.asList(new Object[]
			{2013, new LocalDateTime(2012, 1, 31, 0, 0), "74", "1", "2012123456", "1234", "3",
			"71","Short description" }
			);

	@Autowired
	TransactionRowTransformer transformer;

	@Autowired
	FinancialTransactionService txService;

	@Test
	public void test() {
		final CustomFinancialTransaction tx = this.transformer.transform(this.testList);
		assertNotNull(tx);
		assertNotNull(tx.getRecipient());
		assertNotNull(tx.getExtendingAgency());
		assertNotNull(tx.getNatureOfSubmission());

		this.txService.save(tx);
	}
}
