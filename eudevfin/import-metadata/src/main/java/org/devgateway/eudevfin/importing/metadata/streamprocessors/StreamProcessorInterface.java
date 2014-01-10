package org.devgateway.eudevfin.importing.metadata.streamprocessors;

import java.util.List;

import org.devgateway.eudevfin.financial.AbstractTranslateable;

public interface StreamProcessorInterface {

	public List<? extends AbstractTranslateable> generateObjectList();

	public String getEntityClassName();

}
