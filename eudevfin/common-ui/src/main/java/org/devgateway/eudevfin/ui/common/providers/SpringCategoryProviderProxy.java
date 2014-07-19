/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.ui.common.providers;

import com.vaynberg.wicket.select2.ChoiceProvider;
import com.vaynberg.wicket.select2.Response;
import org.apache.wicket.model.IDetachable;
import org.devgateway.eudevfin.common.spring.ContextHelper;
import org.json.JSONException;
import org.json.JSONWriter;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.Collection;

/**
 * Choice Provider that will autowire the encapsulated provider's beans after it was detached
 *
 * @author aartimon
 * @since 04/02/14
 */
public class SpringCategoryProviderProxy<T> extends ChoiceProvider<T> implements IDetachable {

    private ChoiceProvider<T> provider;
    private boolean detached;

    public SpringCategoryProviderProxy(ChoiceProvider<T> provider) {
        this.provider = provider;
        this.detached = true; //always start detached
    }

    @Override
    public void query(String term, int page, Response<T> response) {
        onAttach();
        provider.query(term, page, response);
    }

    @Override
    public void toJson(T choice, JSONWriter writer) throws JSONException {
        onAttach();
        provider.toJson(choice, writer);
    }

    @Override
    public Collection<T> toChoices(Collection<String> ids) {
        onAttach();
        return provider.toChoices(ids);
    }

    private void onAttach() {
        if (detached) {
            AutowireCapableBeanFactory factory = ContextHelper.newInstance().getAutowireCapableBeanFactory();
            factory.autowireBean(provider);
            detached = false;
        }
    }

    @Override
    public void detach() {
        detached = true;
        provider.detach();
    }
}
