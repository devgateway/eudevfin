package org.devgateway.eudevfin.reports.core.liquibase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

import org.devgateway.eudevfin.common.liquibase.AbstractSpringCustomTaskChange;
import org.devgateway.eudevfin.financial.dao.AreaDaoImpl;
import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.reports.core.dao.RowReportDao;
import org.devgateway.eudevfin.reports.core.domain.ColumnReport;
import org.devgateway.eudevfin.reports.core.domain.RowReport;
import org.devgateway.eudevfin.reports.core.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;

public class PopulateDAC2aRows extends AbstractSpringCustomTaskChange {
	@Autowired
	private RowReportDao rowReportDao;

	@Autowired
	private AreaDaoImpl areaDao;
	
	@Autowired
	private CategoryDaoImpl catDao;
	
	@Override
	public void execute(Database database) throws CustomChangeException {
		List<Area> listAreas = areaDao.findAllAsList();
		insert201(listAreas);

	}

	private void insert201(List<Area> listAreas) {
		
		RowReport row_201 = createDAC2aRow("201", Constants.CALCULATED,
				"{[BiMultilateral].[BI_MULTILATERAL##1],[BiMultilateral].[BI_MULTILATERAL##3]}",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"Except([Type of Aid].[Code].Members, [Type of Aid].[B02])",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE#210]",
				"[Measures].[E]",
				listAreas
				);
		rowReportDao.save(row_201);
		RowReport row_212 = createDAC2aRow("212", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[F01]",
				"[Sector].[60020]",
				"[Type of Finance].[TYPE_OF_FINANCE##610],[Type of Finance].[TYPE_OF_FINANCE#611],[Type of Finance].[TYPE_OF_FINANCE#612],[Type of Finance].[TYPE_OF_FINANCE#613],[Type of Finance].[TYPE_OF_FINANCE#614],[Type of Finance].[TYPE_OF_FINANCE#615],[Type of Finance].[TYPE_OF_FINANCE#616],[Type of Finance].[TYPE_OF_FINANCE#617]",
				"[Measures].[E]",
				listAreas
				);
		rowReportDao.save(row_212);
		RowReport row_221 = createDAC2aRow("221", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[F01]",
				"{[Sector].[60010],[Sector].[60030],[Sector].[60061],[Sector].[60062],[Sector].[60063]}",
				"[Type of Finance].[TYPE_OF_FINANCE##610],[Type of Finance].[TYPE_OF_FINANCE#611],[Type of Finance].[TYPE_OF_FINANCE#612],[Type of Finance].[TYPE_OF_FINANCE#613],[Type of Finance].[TYPE_OF_FINANCE#614],[Type of Finance].[TYPE_OF_FINANCE#615],[Type of Finance].[TYPE_OF_FINANCE#616],[Type of Finance].[TYPE_OF_FINANCE#617]",
				"[Measures].[E]",
				listAreas
				);
		rowReportDao.save(row_221);

		RowReport row_208 = createDAC2aRow("208", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##210]",
				"[Measures].[E]",
				listAreas
				);
		rowReportDao.save(row_208);
		RowReport row_219 = createDAC2aRow("219", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE#210]",
				"[Measures].[R]",
				listAreas
				);
		rowReportDao.save(row_219);
		RowReport row_204 = createDAC2aRow("204", Constants.CALCULATED,
				"{[BiMultilateral].[BI_MULTILATERAL##1],[BiMultilateral].[BI_MULTILATERAL##3]}",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE#621],[Type of Finance].[TYPE_OF_FINANCE##622],[Type of Finance].[TYPE_OF_FINANCE#623],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE#511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Measures].[E]",
				listAreas
				);
		rowReportDao.save(row_204);
		RowReport row_214 = createDAC2aRow("214", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[F01]",
				"[Sector].[60040]",
				"[Type of Finance].[TYPE_OF_FINANCE#621],[Type of Finance].[TYPE_OF_FINANCE##622],[Type of Finance].[TYPE_OF_FINANCE#623]",
				"[Measures].[E]",
				listAreas
				);
		rowReportDao.save(row_214);
		/*RowReport row_205 = createDAC2aRow("205", Constants.CALCULATED,
				"{[BiMultilateral].[BI_MULTILATERAL##1],[BiMultilateral].[BI_MULTILATERAL##3]}",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"Except([Type of Aid].[Code].Members, [Type of Aid].[F02])",
				"",
				"[Type of Finance].Members",
				"[Measures].[R]",
				listAreas
				);
		rowReportDao.save(row_205);*/
		RowReport row_215 = createDAC2aRow("215", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[F01]",
				"[Sector].[60020]",
				"[Type of Finance].[TYPE_OF_FINANCE##610]",
				"[Measures].[R]",
				listAreas
				);
		rowReportDao.save(row_215);
		RowReport row_217 = createDAC2aRow("217", Constants.CALCULATED,
				"{[BiMultilateral].[BI_MULTILATERAL##1],[BiMultilateral].[BI_MULTILATERAL##3]}",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE#511],[Type of Finance].[TYPE_OF_FINANCE#512]",
				"[Measures].[E]",
				listAreas
				);
		rowReportDao.save(row_217);

	}

	@Override
	public String getConfirmationMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFileOpener(ResourceAccessor resourceAccessor) {
		// TODO Auto-generated method stub

	}

	@Override
	public ValidationErrors validate(Database database) {
		// TODO Auto-generated method stub
		return null;
	}

	public RowReport createDAC2aRow(String name, int type, String biMulti,
			String typeOfFlow, String typeOfAid, String purposeCode, String typeOfFinance, String measure, List<Area> listAreas) {
		RowReport row = new RowReport("DAC2a", name, type);

		Set<String> categories = new HashSet<String>();
		if (biMulti != null)
			categories.add(biMulti);
		if (typeOfFlow != null)
			categories.add(typeOfFlow);
		if (typeOfAid != null)
			categories.add(typeOfAid);
		if (purposeCode != null)
			categories.add(purposeCode);
		
		row.setCategories(categories);

		Set<ColumnReport> columns = new HashSet<ColumnReport>();		
		Iterator<Area> it = listAreas.iterator();
		
		while(it.hasNext()){
			Area currentArea = it.next();
			ColumnReport col1 = new ColumnReport(currentArea.getCode(), Constants.CALCULATED,
					measure, "[Area].[" + currentArea.getCode() + "]");
			columns.add(col1);
		}
		row.setColumns(columns);
		return row;

	}

}
