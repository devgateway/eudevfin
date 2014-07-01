/**
 * 
 */
package org.devgateway.eudevfin.ui.common.components;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import com.vaynberg.wicket.select2.ChoiceProvider;

/**
 * @author mihai
 *
 */
public class BooleanDropDownField extends DropDownField<Boolean> {

	private static final long serialVersionUID = 5953458660890338937L;
	
	protected StringResourceModel yesModel= new StringResourceModel("yes", this, null);
	protected StringResourceModel noModel= new StringResourceModel("no", this, null);	
	
	/**
	 * @param id
	 * @param model
	 * @param choiceProvider
	 */
	public BooleanDropDownField(String id, IModel<Boolean> model, ChoiceProvider<Boolean> choiceProvider) {
		super(id, model, choiceProvider);
	}
	
	@Override
	protected void onConfigure() {
		Object modelObject = getDefaultModelObject();
		if(modelObject!=null)
			readOnlyRendition.setDefaultModel((Boolean)modelObject?yesModel:noModel);
		super.onConfigure();
	}
	

}
