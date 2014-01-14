/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.providers;

import com.vaynberg.wicket.select2.Response;
import com.vaynberg.wicket.select2.TextChoiceProvider;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.financial.AbstractTranslateable;

import java.util.*;

/**
 * @author aartimon
 * @since 14/01/14
 */
public abstract class AbstractTranslatableProvider<T extends AbstractTranslateable> extends TextChoiceProvider<T> {
    private static final Logger logger = Logger.getLogger(AbstractTranslatableProvider.class);


    protected abstract BaseEntityService<T> getService();

    @Override
    public Object getId(T choice) {
        return choice.getId();
    }

    @Override
    public void query(String term, int page, Response<T> response) {
        List<T> allItems = getItemsByTerm(term);

        Collections.sort(allItems, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                String l1 = getDisplayText(o1);
                String l2 = getDisplayText(o2);
                if (l1 == null && l2 == null)
                    return 0;
                if (l1 == null)
                    return 1;
                if (l2 == null)
                    return -1;
                return l1.compareTo(l2);
            }
        });
        CollectionUtils.addAll(response.getResults(), allItems);
    }

    protected List<T> getItemsByTerm(String term) {
        return getService().findByGeneralSearch(Session.get().getLocale().getLanguage(), term);
    }

    @Override
    public Collection<T> toChoices(Collection<String> ids) {
        ArrayList<T> ret = new ArrayList<>();
        for (String strId : ids) {
            Long id = Long.parseLong(strId);
            T item = getService().findById(id).getEntity();
            if (item == null)
                logger.error("Can't find object with id: " + id);
            else
                ret.add(item);
        }
        return ret;
    }

}
