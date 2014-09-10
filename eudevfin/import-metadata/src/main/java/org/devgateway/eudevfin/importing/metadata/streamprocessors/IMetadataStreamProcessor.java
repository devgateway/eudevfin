/**
 *
 */
package org.devgateway.eudevfin.importing.metadata.streamprocessors;

import java.util.List;

import org.devgateway.eudevfin.importing.processors.IStreamProcessor;

/**
 * @author alexandru-m-g
 *
 */
public interface IMetadataStreamProcessor extends IStreamProcessor {
	List<String> getMetadataInfoList();

	String getMapperClassName();
}
