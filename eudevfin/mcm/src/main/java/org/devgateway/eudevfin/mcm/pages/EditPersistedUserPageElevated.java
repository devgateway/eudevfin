package org.devgateway.eudevfin.mcm.pages;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(AuthConstants.Roles.ROLE_SUPERVISOR)
@MountPath(value = "/user")
public class EditPersistedUserPageElevated extends EditPersistedUserPage {

	private static final long serialVersionUID = 1926551934261179357L;
	public static final String PARAM_USER_ID="userId";
	public static final String PARAM_USER_ID_VALUE_NEW="new";

	public EditPersistedUserPageElevated(PageParameters parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public PersistedUser getEditablePersistedUser(PageParameters parameters) {
		Long userId = null;
		if (!parameters.get(PARAM_USER_ID).isNull()) {
			PersistedUser persistedUser = null;			
			if (PARAM_USER_ID_VALUE_NEW.equals(parameters.get(PARAM_USER_ID).toString()))
				persistedUser = new PersistedUser();
			else {
				userId = parameters.get(PARAM_USER_ID).toLong();
				persistedUser = userService.findOne(userId).getEntity();
			}
			return persistedUser;
		} else
			return super.getEditablePersistedUser(parameters);
	}

}
