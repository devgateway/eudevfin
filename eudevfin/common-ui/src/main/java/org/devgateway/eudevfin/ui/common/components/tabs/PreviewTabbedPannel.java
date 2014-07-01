/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.components.tabs;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.devgateway.eudevfin.ui.common.components.PermissionAwareLabel;
import org.devgateway.eudevfin.ui.common.components.PreviewableFormPanelAware;

/**
 * 
 * @author mihai
 *
 * @param <T>
 */
public class PreviewTabbedPannel<T extends ITabWithKey> extends Panel {

	private static final long serialVersionUID = 610840627339899456L;
	private final List<T> tabs;

	private class PreviewableLoopItem extends LoopItem implements PreviewableFormPanelAware {
		private static final long serialVersionUID = -4812059975937369157L;

		public PreviewableLoopItem(int index) {
			super(index);
		}

		@Override
		public boolean isFormInPreview() {
			return true;
		}
		
	}

    @SuppressWarnings("WicketForgeJavaIdInspection")
    public PreviewTabbedPannel(String id, final List<T> tabs) {
        super(id);
        this.setOutputMarkupId(true);
        this.tabs = tabs;

        TransparentWebMarkupContainer tmc = new TransparentWebMarkupContainer("content");
        add(tmc);

        final IModel<Integer> tabCount = new AbstractReadOnlyModel<Integer>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Integer getObject() {
                return PreviewTabbedPannel.this.tabs.size();
            }
        };

        final String markupId = this.getMarkupId();

        add(new Loop("tabNames", tabCount) {
        	
        	@Override
        	protected LoopItem newItem(int iteration) {
        		return new PreviewableLoopItem(iteration);
        	}
        	
            @Override
            protected void populateItem(LoopItem item) {
                final int index = item.getIndex();
                final T tab = PreviewTabbedPannel.this.tabs.get(index);

                Label label = new PermissionAwareLabel("tabNamesLink", tab.getTitle(), tab.getKey());
                item.add(label);                              
                item.add(tab.getPanel("tabContentPanel"));
            }
        });


    }
    
    

    private String getTabId(String markupId, int item) {
        return markupId + "-" + item;
    }

    

}
