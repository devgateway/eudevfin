import java.util.ArrayList;
import java.util.List;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.sheetexp.dto.EntityWrapperInterface;
import org.devgateway.eudevfin.sheetexp.dto.HeaderEntityWrapper;
import org.devgateway.eudevfin.sheetexp.integration.api.SpreadsheetTransformerService;
import org.devgateway.eudevfin.sheetexp.util.EntityWrapperListHelper;
import org.devgateway.eudevfin.sheetexp.util.MetadataConstants;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @author Alex
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/commonFinancialContext.xml","classpath:META-INF/spreadsheetExporterContext.xml",
		"classpath:META-INF/commonContext.xml"})
@Component 
public class TransformationTest {
	
	@Autowired
	SpreadsheetTransformerService service;

	@Test
	public void testTransform() {
		final List<FinancialTransaction> txList	= new ArrayList<FinancialTransaction>();
		final FinancialTransaction tx1		= new FinancialTransaction();
		final FinancialTransaction tx2		= new FinancialTransaction();
		
		txList.add(tx1);
		txList.add(tx2);
		
		final LocalDateTime now						= LocalDateTime.now();
		final HeaderEntityWrapper<String> header	= new HeaderEntityWrapper<String>(MetadataConstants.FSS_REPORT_TYPE, now, "en");
		
		final List<EntityWrapperInterface<?>> finalList	= new ArrayList<EntityWrapperInterface<?>>();
		finalList.add(header);
		new EntityWrapperListHelper<FinancialTransaction>(txList, MetadataConstants.FSS_REPORT_TYPE, now, "en").addToWrappedList(finalList);
		
		this.service.createSpreadsheet(finalList, finalList.size() );
		
	}
}
