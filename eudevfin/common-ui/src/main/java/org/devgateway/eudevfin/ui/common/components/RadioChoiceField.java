package org.devgateway.eudevfin.ui.common.components;

import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * Basic RadioChoice input field
 * @author idobre
 * @since 5/12/14
 */

public class RadioChoiceField<T> extends AbstractInputField<T, RadioChoice<T>> {
    private List<T> choices;

    public RadioChoiceField(String id, IModel<T> model, List<T> choices) {
        super(id, model);
        this.choices = choices;
    }

    @Override
    protected RadioChoice<T> newField(String id, IModel<T> model) {
        return new RadioChoice(id, model, choices);
    }
}
