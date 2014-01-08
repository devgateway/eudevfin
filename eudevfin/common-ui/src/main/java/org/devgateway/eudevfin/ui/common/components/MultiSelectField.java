/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.components;

import java.util.Collection;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import com.vaynberg.wicket.select2.ChoiceProvider;
import com.vaynberg.wicket.select2.Select2MultiChoice;

/**
 * Multi select field implemented with Select2
 * @author aartimon@developmentgateway.org
 * @since 31 October 2013
 */
public class MultiSelectField<T> extends AbstractInputField<Collection<T>> {

	private static final long serialVersionUID = -2955458671417231490L;

	public class NotEmptyCollectionValidator<T> extends Behavior implements IValidator<Collection<T>> {
		@Override
		public void validate(IValidatable<Collection<T>> validatable) {			
			if(validatable.getValue().size()<1) {
                ValidationError error = new ValidationError();
                error.addKey("notEmptyCollectionValidator");               
                validatable.error(error);
			}
		}
		
	}

    @SuppressWarnings("unchecked")
    public MultiSelectField(String id, IModel<Collection<T>> model, ChoiceProvider<T> choiceProvider) {
        super(id, model);
        //the field will already be populated by the AbstractInputField constructor
        ((Select2MultiChoice<T>)field).setProvider(choiceProvider);
    }
    
    @Override
    public AbstractInputField<Collection<T>> required() {        	
    	field.add(new NotEmptyCollectionValidator<Collection<T>>());
        return this;
    }
    

    @Override
    protected FormComponent<Collection<T>> newField(String id, IModel<Collection<T>> model) {
        return new Select2MultiChoice<>(id, model);
    }
}
