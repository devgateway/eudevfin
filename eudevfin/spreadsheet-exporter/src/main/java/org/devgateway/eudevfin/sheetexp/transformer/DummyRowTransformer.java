/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.transformer;

import javax.annotation.PostConstruct;

import org.devgateway.eudevfin.sheetexp.dto.EntityWrapperInterface;
import org.devgateway.eudevfin.sheetexp.dto.MetadataRow;
import org.springframework.stereotype.Component;



/**
 * @author Alex
 *
 */
@Component
public class DummyRowTransformer<T extends EntityWrapperInterface> extends AbstractRowTransformer<T> {
	
	@PostConstruct
	private void init() {
		this.addCellTransformer(new DummyCellTransformer<T>());
		this.addCellTransformer(new DummyDateCellTransformer<T>());
	}

	@Override
	protected void createHeader(final T src, final MetadataRow row) {
		super.createHeader(src, row);
		row.add( this.createHeaderCell("Header cell 1") );
		row.add( this.createHeaderCell("Header cell 2") );
	}
	
	
	
}

