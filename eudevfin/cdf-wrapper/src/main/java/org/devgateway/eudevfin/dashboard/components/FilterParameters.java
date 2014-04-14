package org.devgateway.eudevfin.dashboard.components;

import java.io.Serializable;

/**
 * @author idobre
 * @since 1/23/14
 */
public class FilterParameters extends BaseParameters implements Serializable {
	private static final String TYPE = "select";
	private String parameter;
	private QueryDefinition queryDefinition = new QueryDefinition();

	public FilterParameters(String id) {
		super(id);
		setType(TYPE);
		// give higher priority to filters because they need to be executed before other components
		// (The lower the number, the higher priority the component has)
		setPriority(4);
	}

	public FilterParameters(String name, String id) {
		super(name, id);
		setType(TYPE);
		// give higher priority to filters because they need to be executed before other components
		// (The lower the number, the higher priority the component has)
		setPriority(4);
	}

	@Override
	protected BaseParameters getInstance() {
		return this;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public QueryDefinition getQueryDefinition() {
		return queryDefinition;
	}

	public void setQueryDefinition(QueryDefinition queryDefinition) {
		this.queryDefinition = queryDefinition;
	}
}
