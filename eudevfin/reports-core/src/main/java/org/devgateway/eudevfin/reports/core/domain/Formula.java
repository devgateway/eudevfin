package org.devgateway.eudevfin.reports.core.domain;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Formula {
	private List<String> listBiMultilateral;
	private List<String> listTypeOfFlow;
	private List<String> listSectors;
	private List<String> listTypeofAid;
	private List<String> listChannel;
	private List<String> listPurpose;
	private List<String> listTypeOfFinance;
	private List<Column> listColumns;

	public List<String> getListTypeOfFlow() {
		return listTypeOfFlow;
	}

	public void setListTypeOfFlow(List<String> listTypeOfFlow) {
		this.listTypeOfFlow = listTypeOfFlow;
	}

	public List<String> getListSectors() {
		return listSectors;
	}

	public void setListSectors(List<String> listSectors) {
		this.listSectors = listSectors;
	}

	public List<String> getListTypeOfFinance() {
		return listTypeOfFinance;
	}

	public void setListTypeOfFinance(List<String> listTypeOfFinance) {
		this.listTypeOfFinance = listTypeOfFinance;
	}

	public List<String> getListTypeofAid() {
		return listTypeofAid;
	}

	public void setListTypeofAid(List<String> listTypeofAid) {
		this.listTypeofAid = listTypeofAid;
	}

	public List<String> getListBiMultilateral() {
		return listBiMultilateral;
	}

	public void setListBiMultilateral(List<String> listBiMultilateral) {
		this.listBiMultilateral = listBiMultilateral;
	}

	public List<String> getListChannel() {
		return listChannel;
	}

	public void setListChannel(List<String> listChannel) {
		this.listChannel = listChannel;
	}

	public List<Column> getListColumns() {
		return listColumns;
	}

	public void setListColumns(List<Column> listColumns) {
		this.listColumns = listColumns;
	}

	public List<String> getListPurpose() {
		return listPurpose;
	}

	public void setListPurpose(List<String> listPurpose) {
		this.listPurpose = listPurpose;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		List<String> consolidatedClauses = new java.util.ArrayList<String>();
		if (listBiMultilateral != null)
			consolidatedClauses.add(getClause(listBiMultilateral));
		if (listTypeOfFlow != null)
			consolidatedClauses.add(getClause(listTypeOfFlow));
		if (listSectors != null)
			consolidatedClauses.add(getClause(listSectors));
		if (listTypeofAid != null)
			consolidatedClauses.add(getClause(listTypeofAid));
		if (listChannel != null)
			consolidatedClauses.add(getClause(listChannel));
		if (listPurpose != null)
			consolidatedClauses.add(getClause(listPurpose));

		String joined = StringUtils.join(consolidatedClauses.toArray(), "*");
		sb.append(joined);
		return sb.toString();
	}

	private String getClause(List<String> list) {
		String start, clause, end;
		start = clause = end = "";
		if (list != null) {
			if (list.size() > 1) {
				start = "{";
				end = "}";
			}
			for (Iterator<String> it = list.iterator(); it.hasNext();) {
				clause += it.next();
				if (it.hasNext())
					clause += ",";
			}
		}
		return start + clause + end;
	}

}
