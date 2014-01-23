package org.devgateway.eudevfin.dashboard.components;

/**
 * @author idobre
 * @since 1/23/14
 */
public class FilterParameters extends BaseParameters {
	private static final String TYPE = "select";
	private String parameter;
	private QueryDefinition queryDefinition = new QueryDefinition();

	public FilterParameters(String id) {
		super(id);
		setType(TYPE);
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
