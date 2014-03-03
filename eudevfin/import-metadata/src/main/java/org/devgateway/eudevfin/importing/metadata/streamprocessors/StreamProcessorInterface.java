package org.devgateway.eudevfin.importing.metadata.streamprocessors;

import java.util.List;

public interface StreamProcessorInterface {

	List<String> getMetadataInfoList();

	String getMapperClassName();

	Object generateNextObject();

	boolean hasNextObject();

	void close();

}
