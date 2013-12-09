/**
 * 
 */
package org.devgateway.eudevfin.financial.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Alex
 *
 */
public class PagingHelper<T> {

	private int currentPageNo;
	private int totalNumberOfPages;
	private int totalNumOfEntities;
	private List<T> entities;
	
	
	
	
	public PagingHelper(int currentPageNo, int totalNumberOfPages,
			int totalNumOfEntities) {
		
		super();
		this.currentPageNo = currentPageNo;
		this.totalNumberOfPages = totalNumberOfPages;
		this.totalNumOfEntities = totalNumOfEntities;
		this.entities			= new ArrayList<>();
	}
	
	public PagingHelper(int currentPageNo, int totalNumberOfPages,
			int totalNumOfEntities, List<T> entities) {
		super();
		this.currentPageNo = currentPageNo;
		this.totalNumberOfPages = totalNumberOfPages;
		this.totalNumOfEntities = totalNumOfEntities;
		this.entities = entities;
	}
	public int getCurrentPageNo() {
		return currentPageNo;
	}
	public void setCurrentPageNo(int currentPageNo) {
		this.currentPageNo = currentPageNo;
	}
	public int getTotalNumberOfPages() {
		return totalNumberOfPages;
	}
	public void setTotalNumberOfPages(int totalNumberOfPages) {
		this.totalNumberOfPages = totalNumberOfPages;
	}
	public int getTotalNumOfEntities() {
		return totalNumOfEntities;
	}
	public void setTotalNumOfEntities(int totalNumOfEntities) {
		this.totalNumOfEntities = totalNumOfEntities;
	}
	public List<T> getEntities() {
		return entities;
	}
	public void setEntities(List<T> entities) {
		this.entities = entities;
	}

	public List<PagingItem> createPagingItems() {
		List<PagingItem> pagingItems	= new ArrayList<>();
		
		PagingItem prevItem		= new PagingItem("<",this.currentPageNo+1, false);
		prevItem.setDisabled(this.currentPageNo == 0);
		pagingItems.add(prevItem);
		
		for(int i=1; i <= this.totalNumberOfPages; i++ ) {
			PagingItem item		= new PagingItem();
			item.setLabel( new Integer(i).toString() );
			item.setDisabled(  i == (this.currentPageNo+1) );
			pagingItems.add(item);
		}
		
		PagingItem nextItem		= new PagingItem(">",this.currentPageNo+1, false);
		nextItem.setDisabled(this.currentPageNo == this.totalNumberOfPages-1);
		pagingItems.add(nextItem);
		
		return pagingItems;
	}
	
}
