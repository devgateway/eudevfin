package org.devgateway.eudevfin.importing.metadata.mapping;

import java.util.List;

public interface MapperInterface<T> {

	T createEntity (List<String> values);

	void setMetainfos(List<String> metainfos);

	List<String> getMetainfos();
	
}

