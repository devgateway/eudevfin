/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.iati.domain;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author alexandru-m-g
 *
 */
public class CrsAdd {
	
	@XStreamImplicit
	private List<AidtypeFlag> aidtypeFlags;

	public List<AidtypeFlag> getAidtypeFlags() {
		return this.aidtypeFlags;
	}

	public void setAidtypeFlags(final List<AidtypeFlag> aidtypeFlags) {
		this.aidtypeFlags = aidtypeFlags;
	}
	
	
}
