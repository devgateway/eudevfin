package org.devgateway.eudevfin.reports.ui.pages;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath(value = "/exportreports")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ReportsExport extends HeaderFooter {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1829518959766029286L;
	
	public ReportsExport () {
        
    }
	public ReportsExport (final PageParameters parameters) {
		super(parameters);
		String reportType = parameters.get("reportType").toString("");
		
		pageTitle.setDefaultModel(new StringResourceModel("navbar.reports.export." + reportType, this, null, null));
		
		HiddenField<String> field = new HiddenField<String>("reportType", Model.of(""));
		field.setModelValue(new String[]{reportType});
		add(field);
    }
	
	@Override
    public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
	}
}
