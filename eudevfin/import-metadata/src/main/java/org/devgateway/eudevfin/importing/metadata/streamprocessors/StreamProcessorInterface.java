package org.devgateway.eudevfin.importing.metadata.streamprocessors;

import java.util.List;

import org.devgateway.eudevfin.financial.AbstractTranslateable;
import org.devgateway.eudevfin.importing.metadata.mapping.MapperInterface;

public interface StreamProcessorInterface {

	List<String> getMetadataInfoList();

	String getMapperClassName();

	Object generateNextObject();

	boolean hasNextObject();

}
