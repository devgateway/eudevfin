package org.devgateway.eudevfin.dashboard.components;

/**
 * @author idobre
 * @since 1/24/14
 */
public class ChartParameters extends BaseParameters {
	private static final String TYPE = "queryComponent";
	private String resultvar;
	private QueryDefinition queryDefinition = new QueryDefinition();

	public ChartParameters(String id) {
		super(id);
		setType(TYPE);
	}

	public ChartParameters(String name, String id) {
		super(name, id);
		setType(TYPE);
	}

	@Override
	protected BaseParameters getInstance() {
		return this;
	}

	public String getResultvar() {
		return resultvar;
	}

	public void setResultvar(String resultvar) {
		this.resultvar = resultvar;
	}

	public QueryDefinition getQueryDefinition() {
		return queryDefinition;
	}

	public void setQueryDefinition(QueryDefinition queryDefinition) {
		this.queryDefinition = queryDefinition;
	}
}
