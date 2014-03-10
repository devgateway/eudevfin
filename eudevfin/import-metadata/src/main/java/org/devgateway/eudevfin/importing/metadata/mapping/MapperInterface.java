package org.devgateway.eudevfin.importing.metadata.mapping;

import java.util.List;

import org.devgateway.eudevfin.metadata.common.domain.Organization;

public interface MapperInterface<T> {

	T createEntity (List<String> values);

	void setMetainfos(List<String> metainfos);

	List<String> getMetainfos();
}

