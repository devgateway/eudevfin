package org.devgateway.eudevfin.ui.common.forms;

import java.io.Serializable;

public class GroupingBoxPanelForm implements Serializable { 

		private static final long serialVersionUID = -4207835378383930432L;
		private String crsIdSearch;
		private String donorIdSearch; 

		public void reset() {
			donorIdSearch=null;
			crsIdSearch=null;
		}

		/**
		 * @return the crsIdSearch
		 */
		public String getCrsIdSearch() {
			return crsIdSearch;
		}

		/**
		 * @param crsIdSearch the crsIdSearch to set
		 */
		public void setCrsIdSearch(String crsIdSearch) {
			this.crsIdSearch = crsIdSearch;
		}

		/**
		 * @return the donorIdSearch
		 */
		public String getDonorIdSearch() {
			return donorIdSearch;
		}

		/**
		 * @param donorIdSearch the donorIdSearch to set
		 */
		public void setDonorIdSearch(String donorIdSearch) {
			this.donorIdSearch = donorIdSearch;
		}
			
	
	}