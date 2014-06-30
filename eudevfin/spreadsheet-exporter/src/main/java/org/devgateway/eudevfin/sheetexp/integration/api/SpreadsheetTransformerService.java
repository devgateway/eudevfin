/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.integration.api;

import java.io.OutputStream;
import java.util.List;

import org.devgateway.eudevfin.sheetexp.dto.EntityWrapperInterface;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.annotation.Header;

/**
 * @author Alex
 * 
 */
public interface SpreadsheetTransformerService {

	public Boolean createSpreadsheet(List<EntityWrapperInterface<?>> transactions,
			@Header(MessageHeaders.SEQUENCE_SIZE) Integer maxNum);

	public Boolean createSpreadsheetOnStream(List<EntityWrapperInterface<?>> transactions,
			@Header(MessageHeaders.SEQUENCE_SIZE) Integer maxNum, @Header("outputStream") OutputStream out,
			@Header("exportName")final String exportName);

	public Boolean createIatiXmlOnStream(List<EntityWrapperInterface<?>> transactions,
			@Header("outputStream") OutputStream out,
			@Header("exportName")final String exportName);
}
