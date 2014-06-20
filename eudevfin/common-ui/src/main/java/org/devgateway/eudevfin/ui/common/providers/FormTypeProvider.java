/**
 * 
 */
package org.devgateway.eudevfin.ui.common.providers;

import java.util.Collection;

import org.apache.wicket.Component;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.eudevfin.ui.common.temporary.SB;

import com.vaynberg.wicket.select2.Response;
import com.vaynberg.wicket.select2.TextChoiceProvider;

/**
 * @author mihai
 *
 */
public class FormTypeProvider extends TextChoiceProvider<String> {


	private static final long serialVersionUID = 4554272931187087047L;
	private Component component;
	
	public FormTypeProvider(Component component) {
		this.component=component;
	}
	

	@Override
    protected String getDisplayText(String choice) {
    	return getTranslation(choice);
    }

    @Override
    protected Object getId(String choice) {
    	return choice;
    }

    private String getTranslation(String key) {
    	return new StringResourceModel("navbar.newTransaction."+key,  component, null).getObject();
    
    }
    
    @Override
    public void query(String term, int page, Response<String> response) {
        response.add(SB.BILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        response.add(SB.BILATERAL_ODA_CRS);
        response.add(SB.BILATERAL_ODA_FORWARD_SPENDING);
        response.add(SB.MULTILATERAL_ODA_ADVANCE_QUESTIONNAIRE);
        response.add(SB.MULTILATERAL_ODA_CRS);
        response.add(SB.NON_ODA_OOF_NON_EXPORT);
        response.add(SB.NON_ODA_OOF_EXPORT);
        response.add(SB.NON_ODA_PRIVATE_GRANTS);
        response.add(SB.NON_ODA_PRIVATE_MARKET);
        response.add(SB.NON_ODA_OTHER_FLOWS);            
    }

    @Override
    public Collection<String> toChoices(Collection<String> ids) {
    	return ids;
    }

}
