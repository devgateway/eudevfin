package org.devgateway.eudevfin.reports.core.liquibase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

import org.devgateway.eudevfin.common.liquibase.AbstractSpringCustomTaskChange;
import org.devgateway.eudevfin.reports.core.dao.RowReportDao;
import org.devgateway.eudevfin.reports.core.domain.ColumnReport;
import org.devgateway.eudevfin.reports.core.domain.RowReport;
import org.devgateway.eudevfin.reports.core.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;

public class PopulateDAC1Rows extends AbstractSpringCustomTaskChange {
	@Autowired
	private RowReportDao rowReportDao;

	@Override
	public void execute(Database database) throws CustomChangeException {
		insertKeyIndicators();
		insertSectionI();
		insertSectionII();
		insertSectionIII();
		insertSectionIV();
		insertSectionV();

	}

	private void insertKeyIndicators() {
		RowReport row_001 = createDAC1KIRow("001", Constants.CALCULATED,
				"[Type of Finance].[TYPE_OF_FINANCE##1]");
		rowReportDao.save(row_001);
		RowReport row_002 = createDAC1KIRow("002", Constants.CALCULATED,
				"[Type of Finance].[TYPE_OF_FINANCE##2]");
		rowReportDao.save(row_002);
		RowReport row_003 = createDAC1KIRow("003", Constants.CALCULATED,
				"[Type of Finance].[TYPE_OF_FINANCE##3]");
		rowReportDao.save(row_003);
		RowReport row_004 = createDAC1KIRow("004", Constants.CALCULATED,
				"[Type of Finance].[TYPE_OF_FINANCE##4]");
		rowReportDao.save(row_004);
	}

	private RowReport createDAC1KIRow(String name, int type,
			String typeOfFinance) {
		RowReport row = new RowReport(name, type);

		Set<String> categories = new HashSet<String>();
		categories.add("[Type of Aid].Members");
		row.setCategories(categories);

		Set<ColumnReport> columns = new HashSet<ColumnReport>();

		ColumnReport col1 = new ColumnReport("1140", Constants.CALCULATED,
				"[Measures].[Amount]", typeOfFinance);
		col1.setPattern("#,##0");
		columns.add(col1);
		row.setColumns(columns);
		return row;
	}

	private void insertSectionI() {
		insertSectionIA();
		insertSectionIB();
	}
	private void insertSectionIB() {
		RowReport row_2101 = createDAC1Row("2101", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##2]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B02]",
				"",
				"[Channel].[41000]",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##310]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##310]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]");
		rowReportDao.save(row_2101);
		RowReport row_2102 = createDAC1Row("2102", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##2]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B02]",
				"",
				"{[Channel].[42000]}",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]");
		rowReportDao.save(row_2102);
		RowReport row_2103 = createDAC1Row("2103", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##2]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B02]",
				"",
				"{[Channel].[44002],[Channel].[44003],[Channel].[44007]}",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##310],[Type of Finance].[TYPE_OF_FINANCE##618]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##310],[Type of Finance].[TYPE_OF_FINANCE##618]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]");
		rowReportDao.save(row_2103);
		RowReport row_2104 = createDAC1Row("2104", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##2]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B02]",
				"",
				"{[Channel].[44001],[Channel].[44004],[Channel].[44005],[Channel].[44006]}",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##310]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##310]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]");
		rowReportDao.save(row_2104);
		RowReport row_2105 = createDAC1Row("2105", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##2]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B02]",
				"",
				"[Channel].[46000]",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##310],[Type of Finance].[TYPE_OF_FINANCE##618]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##310],[Type of Finance].[TYPE_OF_FINANCE##618]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]");
		rowReportDao.save(row_2105);
		RowReport row_2106 = createDAC1Row("2106", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##2]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B02]",
				"",
				"{[Channel].[47044],[Channel].[47129],[Channel].[47130]}",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##310]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##310]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]");
		rowReportDao.save(row_2106);
		RowReport row_2107 = createDAC1Row("2107", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##2]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B02]",
				"",
				"[Channel].[47078]",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##310]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##310]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]");
		rowReportDao.save(row_2107);
		//TODO: Check other categories
		RowReport row_2108 = createDAC1Row("2108", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##2]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B02]",
				"",
				"[Channel].[40000]",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##310],[Type of Finance].[TYPE_OF_FINANCE##618]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##310],[Type of Finance].[TYPE_OF_FINANCE##618]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]");
		rowReportDao.save(row_2108);
		RowReport row_2110 = createDAC1Row("2110", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##2]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B02]",
				"",
				"[Channel].[40000]",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##310]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##310]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]");
		rowReportDao.save(row_2110);
		//TODO: Rows 2901 and 2902 seems to be determined arbitrarily. Find a formula that can match it. 
		/*
		RowReport row_2901 = createDAC1Row("2901", Constants.CALCULATED,
				"{[BiMultilateral].[BI_MULTILATERAL##1],[BiMultilateral].[BI_MULTILATERAL##2]}",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"{[Type of Aid].[B02],[Type of Aid].[F01]}",
				"{[Sector].[60010],[Sector].[60020],[Sector].[60030]}",
				"{[Channel].[43002],[Channel].[43004],[Channel].[44003],[Channel].[44007],[Channel].[46003],[Channel].[46012],[Channel].[42004]}",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##310],[Type of Finance].[TYPE_OF_FINANCE##610],[Type of Finance].[TYPE_OF_FINANCE##611],[Type of Finance].[TYPE_OF_FINANCE##618]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##310],[Type of Finance].[TYPE_OF_FINANCE##610],[Type of Finance].[TYPE_OF_FINANCE##611],[Type of Finance].[TYPE_OF_FINANCE##618]",
				"");
		rowReportDao.save(row_2901);
		RowReport row_2902 = createDAC1Row("2902", Constants.CALCULATED,
				"{[BiMultilateral].[BI_MULTILATERAL##1],[BiMultilateral].[BI_MULTILATERAL##2]}",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"",
				"",
				"{[Channel].[44001],[Channel].[44002]}",
				"[Type of Finance].Members",
				"",
				"",
				"[Type of Finance].Members",
				"");
		rowReportDao.save(row_2902);*/
		
	}

	private void insertSectionIA() {
		RowReport row_1110 = createDAC1Row("1110", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[A01]",
				"[Sector].[51010]",
				"[Channel].[12000]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]");
		rowReportDao.save(row_1110);
		RowReport row_1120 = createDAC1Row("1120", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[A02]",
				"",
				"[Channel].[12000]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]");
		rowReportDao.save(row_1120);
		RowReport row_1211 = createDAC1Row("1211", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##3]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B01]", 
				"",
				"[Channel].[22000]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"");
		rowReportDao.save(row_1211);
		RowReport row_1212 = createDAC1Row("1212", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##3]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B01]",
				"",
				"{[Channel].[21000], [Channel].[23000]}",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"");
		rowReportDao.save(row_1212);
		RowReport row_1213 = createDAC1Row("1213", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##3]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B01]",
				"",
				"{[Channel].[30000], [Channel].[31000], [Channel].[32000]}",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]"
				);
		rowReportDao.save(row_1213);
		RowReport row_1214 = createDAC1Row("1214", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##3]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B01]",
				"",
				"[Channel].[51000]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]"
				);
		rowReportDao.save(row_1214);
		RowReport row_1220 = createDAC1Row("1220", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B03]",
				"",
				"{[Channel].[21000], [Channel].[40000]}",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]"
				);
		rowReportDao.save(row_1220);
		RowReport row_1230 = createDAC1Row("1230", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B04]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]");
		rowReportDao.save(row_1230);
		RowReport row_1310 = createDAC1Row("1310", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[C01]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##210]",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##210]",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]"
				);
		rowReportDao.save(row_1310);
		RowReport row_1311 = createDAC1Row("1311", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[C01]",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]"
				);
		rowReportDao.save(row_1311);		
		RowReport row_1320 = createDAC1Row("1320", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[C01]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]"
				);
		rowReportDao.save(row_1320);
		//TODO: Check 1330 PBA=1
		RowReport row_1330 = createDAC1Row("1330", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[C01]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]"
				);
		rowReportDao.save(row_1330);
		//TODO: Check 1301 amount: items 40 & 41
		RowReport row_1301 = createDAC1Row("1301", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[C01]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]"
				);
		rowReportDao.save(row_1301);		
		RowReport row_1410 = createDAC1Row("1410", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[D01]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]"
				);
		rowReportDao.save(row_1410);		
		RowReport row_1420 = createDAC1Row("1420", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[D02]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]"
				);
		rowReportDao.save(row_1420);		
		RowReport row_1510 = createDAC1Row("1510", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[E01]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				""
				);
		rowReportDao.save(row_1510);		
		RowReport row_1520 = createDAC1Row("1520", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[E01]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				""
				);
		rowReportDao.save(row_1520);
		//TODO: Check (not for	commit.),
		RowReport row_1611 = createDAC1Row("1611", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[F01]",
				"{[Sector].[60020], [Sector].[60040]}",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##610],[Type of Finance].[TYPE_OF_FINANCE##611]",
				"[Type of Finance].[TYPE_OF_FINANCE##621]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##610],[Type of Finance].[TYPE_OF_FINANCE##611]",
				"[Type of Finance].[TYPE_OF_FINANCE##621]"
				);
		rowReportDao.save(row_1611);
		RowReport row_1612 = createDAC1Row("1612", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[F01]",
				"{[Sector].[60020], [Sector].[60040]}",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##612],[Type of Finance].[TYPE_OF_FINANCE##613],[Type of Finance].[TYPE_OF_FINANCE##616]",
				"[Type of Finance].[TYPE_OF_FINANCE##622],[Type of Finance].[TYPE_OF_FINANCE##623]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##612],[Type of Finance].[TYPE_OF_FINANCE##613],[Type of Finance].[TYPE_OF_FINANCE##616]",
				"[Type of Finance].[TYPE_OF_FINANCE##622],[Type of Finance].[TYPE_OF_FINANCE##623]"
				);
		rowReportDao.save(row_1612);		
		RowReport row_1613 = createDAC1Row("1613", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[F01]",
				"{[Sector].[60020], [Sector].[60040]}",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##614],[Type of Finance].[TYPE_OF_FINANCE##615],[Type of Finance].[TYPE_OF_FINANCE##617]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##614],[Type of Finance].[TYPE_OF_FINANCE##615],[Type of Finance].[TYPE_OF_FINANCE##617]",
				""
				);
		rowReportDao.save(row_1613);		
		RowReport row_1614 = createDAC1Row("1614", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[F01]",
				"[Sector].[60020]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##616],[Type of Finance].[TYPE_OF_FINANCE##617]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##616],[Type of Finance].[TYPE_OF_FINANCE##617]",
				""
				);
		rowReportDao.save(row_1614);		
		RowReport row_1621 = createDAC1Row("1621", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[F01]",
				"[Sector].[60030]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##618]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##618]",
				""
				);
		rowReportDao.save(row_1621);		
		RowReport row_1622 = createDAC1Row("1622", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[F01]",
				"{[Sector].[60061], [Sector].[60062]}",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##610],[Type of Finance].[TYPE_OF_FINANCE##611],[Type of Finance].[TYPE_OF_FINANCE##612],[Type of Finance].[TYPE_OF_FINANCE##613],[Type of Finance].[TYPE_OF_FINANCE##614],[Type of Finance].[TYPE_OF_FINANCE##615],[Type of Finance].[TYPE_OF_FINANCE##616],[Type of Finance].[TYPE_OF_FINANCE##617],[Type of Finance].[TYPE_OF_FINANCE##618]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##610],[Type of Finance].[TYPE_OF_FINANCE##611],[Type of Finance].[TYPE_OF_FINANCE##612],[Type of Finance].[TYPE_OF_FINANCE##613],[Type of Finance].[TYPE_OF_FINANCE##614],[Type of Finance].[TYPE_OF_FINANCE##615],[Type of Finance].[TYPE_OF_FINANCE##616],[Type of Finance].[TYPE_OF_FINANCE##617],[Type of Finance].[TYPE_OF_FINANCE##618]",
				""
				);
		rowReportDao.save(row_1622);		
		RowReport row_1623 = createDAC1Row("1623", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[F01]",
				"[Sector].[60063]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##610],[Type of Finance].[TYPE_OF_FINANCE##611],[Type of Finance].[TYPE_OF_FINANCE##612],[Type of Finance].[TYPE_OF_FINANCE##613],[Type of Finance].[TYPE_OF_FINANCE##614],[Type of Finance].[TYPE_OF_FINANCE##615],[Type of Finance].[TYPE_OF_FINANCE##616],[Type of Finance].[TYPE_OF_FINANCE##617],[Type of Finance].[TYPE_OF_FINANCE##618]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##610],[Type of Finance].[TYPE_OF_FINANCE##611],[Type of Finance].[TYPE_OF_FINANCE##612],[Type of Finance].[TYPE_OF_FINANCE##613],[Type of Finance].[TYPE_OF_FINANCE##614],[Type of Finance].[TYPE_OF_FINANCE##615],[Type of Finance].[TYPE_OF_FINANCE##616],[Type of Finance].[TYPE_OF_FINANCE##617],[Type of Finance].[TYPE_OF_FINANCE##618]",
				""
				);
		rowReportDao.save(row_1623);		
		RowReport row_1624 = createDAC1Row("1624", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[F01]",
				"[Sector].[60010]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##610],[Type of Finance].[TYPE_OF_FINANCE##611],[Type of Finance].[TYPE_OF_FINANCE##612],[Type of Finance].[TYPE_OF_FINANCE##613],[Type of Finance].[TYPE_OF_FINANCE##614],[Type of Finance].[TYPE_OF_FINANCE##615],[Type of Finance].[TYPE_OF_FINANCE##616],[Type of Finance].[TYPE_OF_FINANCE##617],[Type of Finance].[TYPE_OF_FINANCE##618]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##610],[Type of Finance].[TYPE_OF_FINANCE##611],[Type of Finance].[TYPE_OF_FINANCE##612],[Type of Finance].[TYPE_OF_FINANCE##613],[Type of Finance].[TYPE_OF_FINANCE##614],[Type of Finance].[TYPE_OF_FINANCE##615],[Type of Finance].[TYPE_OF_FINANCE##616],[Type of Finance].[TYPE_OF_FINANCE##617],[Type of Finance].[TYPE_OF_FINANCE##618]",
				""
				);
		rowReportDao.save(row_1624);		
		RowReport row_1630 = createDAC1Row("1630", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[F01]",
				"[Sector].[60020]",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##610]",
				"",
				""
				);
		rowReportDao.save(row_1630);		
		RowReport row_1640 = createDAC1Row("1640", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[F01]",
				"[Sector].[60020]",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##611]",
				"",
				""
				);
		rowReportDao.save(row_1640);		
		RowReport row_1700 = createDAC1Row("1700", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[G01]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				""
				);
		rowReportDao.save(row_1700);		
		RowReport row_1810 = createDAC1Row("1810", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[H01]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				""
				);
		rowReportDao.save(row_1810);		
		RowReport row_1820 = createDAC1Row("1820", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[H01]",
				"[Sector].[93010]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				""
				);
		rowReportDao.save(row_1820);
		
		RowReport row_1900 = createDAC1Row("1900", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##210]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##210]",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE##210]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]"
				);
		rowReportDao.save(row_1900);		
		RowReport row_1901 = createDAC1Row("1901", Constants.CALCULATED,
				"{[BiMultilateral].[BI_MULTILATERAL##1], [BiMultilateral].[BI_MULTILATERAL##3]}",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]"
				);
		rowReportDao.save(row_1901);		
		RowReport row_1902 = createDAC1Row("1902", Constants.CALCULATED,
				"{[BiMultilateral].[BI_MULTILATERAL##1], [BiMultilateral].[BI_MULTILATERAL##3]}",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"Except([Type of Aid].[Code].Members, [Type of Aid].[G01])",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]"
				);
		rowReportDao.save(row_1902);		
		RowReport row_1903 = createDAC1Row("1903", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"Except([Type of Aid].[Code].Members, [Type of Aid].[B01])",
				"",
				"[Channel].[20000]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]"
				);
		rowReportDao.save(row_1903);		
		RowReport row_1904 = createDAC1Row("1904", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"Except([Type of Aid].[Code].Members, [Type of Aid].[B02])",
				"",
				"[Channel].[40000]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##410]"
				);
		rowReportDao.save(row_1904);		
		RowReport row_1905 = createDAC1Row("1905", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[C01]",
				"[Sector].[15230]",
				"[Channel].[40000]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]"
				);
		rowReportDao.save(row_1905);		
		RowReport row_1906 = createDAC1Row("1906", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"",
				"[Sector].[72040]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511],[Type of Finance].[TYPE_OF_FINANCE##512]"
				);
		rowReportDao.save(row_1906);		
	}

	private void insertSectionII() {
		insertSectionIIA();
		insertSectionIIB();
	}
	private void insertSectionIIB() {
		RowReport row_326 = createDAC1Row("326", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##2]",
				"[Type of Flow].[TYPE_OF_FLOW##20]",
				"",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##912]",
				"[Type of Finance].[TYPE_OF_FINANCE##912]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##912]"
				);
		rowReportDao.save(row_326);	
		RowReport row_327 = createDAC1Row("327", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##2]",
				"[Type of Flow].[TYPE_OF_FLOW##20]",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##412]",
				"[Type of Finance].[TYPE_OF_FINANCE##412]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##412]"
				);
		rowReportDao.save(row_327);	
/*		RowReport row_800 = createDAC1Row("800", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##20]",
				"",
				"",
				"",
				"",
				"",
				"[Type of Finance].Members",
				"",
				""
				);
		rowReportDao.save(row_800);	*/
/*		RowReport row_805 = createDAC1Row("805", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##2]",
				"[Type of Flow].[TYPE_OF_FLOW##20]",
				"",
				"",
				"",
				"",
				"",
				"[Type of Finance].Members",
				"",
				""
				);
		rowReportDao.save(row_805);	*/
		RowReport row_786 = createDAC1Row("786", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##20]",
				"",
				"",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##613],[Type of Finance].[TYPE_OF_FINANCE##631]",
				"",
				""
				);
		rowReportDao.save(row_786);	
	}

	private void insertSectionIIA() {
		RowReport row_265 = createDAC1Row("265", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##20]",
				"",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##451],[Type of Finance].[TYPE_OF_FINANCE##452],[Type of Finance].[TYPE_OF_FINANCE##453]",
				"[Type of Finance].[TYPE_OF_FINANCE##451],[Type of Finance].[TYPE_OF_FINANCE##452],[Type of Finance].[TYPE_OF_FINANCE##453]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##451],[Type of Finance].[TYPE_OF_FINANCE##452],[Type of Finance].[TYPE_OF_FINANCE##453]"
				);
		rowReportDao.save(row_265);	
		RowReport row_266 = createDAC1Row("265", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##20]",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##211]",
				"[Type of Finance].[TYPE_OF_FINANCE##414]",
				"[Type of Finance].[TYPE_OF_FINANCE##414]",
				"[Type of Finance].[TYPE_OF_FINANCE##211]",
				"[Type of Finance].[TYPE_OF_FINANCE##414]"
				);
		rowReportDao.save(row_266);	
		RowReport row_291 = createDAC1Row("291", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##20]",
				"",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##411],[Type of Finance].[TYPE_OF_FINANCE##412],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511]",
				"[Type of Finance].[TYPE_OF_FINANCE##411],[Type of Finance].[TYPE_OF_FINANCE##412],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##411],[Type of Finance].[TYPE_OF_FINANCE##412],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE##511]"
				);
		rowReportDao.save(row_291);		
		RowReport row_292 = createDAC1Row("292", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##20]",
				"",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##412],[Type of Finance].[TYPE_OF_FINANCE##510]",
				"[Type of Finance].[TYPE_OF_FINANCE##412],[Type of Finance].[TYPE_OF_FINANCE##510]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##412],[Type of Finance].[TYPE_OF_FINANCE##510]"
				);
		rowReportDao.save(row_292);		
		RowReport row_293 = createDAC1Row("293", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##20]",
				"",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##412]",
				"[Type of Finance].[TYPE_OF_FINANCE##412]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##412]"
				);
		rowReportDao.save(row_293);		
		RowReport row_280 = createDAC1Row("280", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##20]",
				"",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##510]",
				"[Type of Finance].[TYPE_OF_FINANCE##510]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##510]"
				);
		rowReportDao.save(row_280);		
		RowReport row_287 = createDAC1Row("287", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##20]",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##111]",
				"[Type of Finance].[TYPE_OF_FINANCE##413]",
				"[Type of Finance].[TYPE_OF_FINANCE##413]",
				"[Type of Finance].[TYPE_OF_FINANCE##111]",
				"[Type of Finance].[TYPE_OF_FINANCE##413]"
				);
		rowReportDao.save(row_287);	
		RowReport row_302 = createDAC1Row("302", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##20]",
				"",
				"",
				"[Channel].[60040]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##623]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##623]"
				);
		rowReportDao.save(row_302);	
		RowReport row_310 = createDAC1Row("310", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##20]",
				"",
				"",
				"[Channel].[60040]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##624],[Type of Finance].[TYPE_OF_FINANCE##625]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##624],[Type of Finance].[TYPE_OF_FINANCE##625]"
				);
		rowReportDao.save(row_310);	
		RowReport row_303 = createDAC1Row("303", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##20]",
				"",
				"",
				"[Channel].[60040]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##626],[Type of Finance].[TYPE_OF_FINANCE##627]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##626],[Type of Finance].[TYPE_OF_FINANCE##627]"
				);
		rowReportDao.save(row_303);	
		RowReport row_299 = createDAC1Row("299", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##20]",
				"",
				"",
				"[Channel].[60040]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##512]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##512]"
				);
		rowReportDao.save(row_299);	
		RowReport row_298 = createDAC1Row("298", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##20]",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##910],[Type of Finance].[TYPE_OF_FINANCE##911]",
				"[Type of Finance].[TYPE_OF_FINANCE##910],[Type of Finance].[TYPE_OF_FINANCE##911]",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"[Type of Finance].[TYPE_OF_FINANCE##910],[Type of Finance].[TYPE_OF_FINANCE##911]"
				);
		rowReportDao.save(row_298);	
		//TODO: Empty?
/*		RowReport row_102 = createDAC1Row("102", Constants.CALCULATED,
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##612],[Type of Finance].[TYPE_OF_FINANCE##622],[Type of Finance].[TYPE_OF_FINANCE##630]",
				"",
				""
				);
		rowReportDao.save(row_102);	*/
		
		}

	private void insertSectionIII() {
		RowReport row_340 = createDAC1Row("340", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##35]",
				"",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##710],[Type of Finance].[TYPE_OF_FINANCE##711]",
				"[Type of Finance].[TYPE_OF_FINANCE##710],[Type of Finance].[TYPE_OF_FINANCE##711]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##710],[Type of Finance].[TYPE_OF_FINANCE##711]"
				);
		rowReportDao.save(row_340);	
		RowReport row_345 = createDAC1Row("345", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##35]",
				"",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##710]",
				"[Type of Finance].[TYPE_OF_FINANCE##710]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##710]"
				);
		rowReportDao.save(row_345);	
		RowReport row_751 = createDAC1Row("751", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##35]",
				"",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##810]",
				"[Type of Finance].[TYPE_OF_FINANCE##810]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##810]"
				);
		rowReportDao.save(row_751);	
		RowReport row_752 = createDAC1Row("752", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##35]",
				"",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##453]",
				"[Type of Finance].[TYPE_OF_FINANCE##453]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##453]"
				);
		rowReportDao.save(row_752);	
		RowReport row_753 = createDAC1Row("753", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##35]",
				"",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##910]",
				"[Type of Finance].[TYPE_OF_FINANCE##910]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##910]"
				);
		rowReportDao.save(row_753);	
		RowReport row_756 = createDAC1Row("756", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##35]",
				"",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##451]",
				"[Type of Finance].[TYPE_OF_FINANCE##451]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##451]"
				);
		rowReportDao.save(row_756);	
		RowReport row_761 = createDAC1Row("761", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##35]",
				"",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##452]",
				"[Type of Finance].[TYPE_OF_FINANCE##452]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##452]"
				);
		rowReportDao.save(row_761);	
		RowReport row_388 = createDAC1Row("388", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##35]",
				"",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##811]",
				"[Type of Finance].[TYPE_OF_FINANCE##811]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##811]"
				);
		rowReportDao.save(row_388);	
		RowReport row_389 = createDAC1Row("389", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##35]",
				"",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##911]",
				"[Type of Finance].[TYPE_OF_FINANCE##911]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##911]"
				);
		rowReportDao.save(row_389);	
		RowReport row_359 = createDAC1Row("359", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##2]",
				"[Type of Flow].[TYPE_OF_FLOW##35]",
				"",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##912]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##912]"
				);
		rowReportDao.save(row_359);	

	}
	private void insertSectionIV() {
		RowReport row_425 = createDAC1Row("425", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##30]",
				"",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				""
				);
		rowReportDao.save(row_425);
		//TODO: excluding foreign NGOs
		RowReport row_420 = createDAC1Row("420", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##3]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"",
				"",
				"[Channel].[22000]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110]",
				"",
				""
				);
		rowReportDao.save(row_420);	
		
	}
	private void insertSectionV() {
		/*
		RowReport row_207 = createDAC1Row("207", Constants.CALCULATED,
				"{[BiMultilateral].[BI_MULTILATERAL##1],[BiMultilateral].[BI_MULTILATERAL##2]}",
				"{[Type of Flow].[TYPE_OF_FLOW##10],[Type of Flow].[TYPE_OF_FLOW##50]}",
				"",
				"[Sector].[15230]",
				"",
				"",
				"",
				"",
				"",
				""
				);
		rowReportDao.save(row_207);	*/
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

	public RowReport createDAC1Row(String name, int type, String biMulti,
			String typeOfFlow, String typeOfAid, String purposeCode,
			String channel, String col_1121_tof, String col_1122_tof,
			String col_1130_tof, String col_1151_tof, String col_1152_tof) {
		RowReport row = new RowReport(name, type);

		Set<String> categories = new HashSet<String>();
		if (biMulti != null)
			categories.add(biMulti);
		if (typeOfFlow != null)
			categories.add(typeOfFlow);
		if (typeOfAid != null)
			categories.add(typeOfAid);
		if (purposeCode != null)
			categories.add(purposeCode);
		if (channel != null)
			categories.add(channel);
		row.setCategories(categories);

		Set<ColumnReport> columns = new HashSet<ColumnReport>();
		
		HashSet<String> sumCols1120 = new HashSet<String>();
		if(col_1121_tof != null && !col_1121_tof.equals("")){
			ColumnReport col1 = new ColumnReport("1121", Constants.CALCULATED,
					"[Measures].[Extended]", col_1121_tof);
			columns.add(col1);
			sumCols1120.add(col1.getColumnCode());
		}

		if(col_1122_tof != null && !col_1122_tof.equals("")){
			ColumnReport col2 = new ColumnReport("1122", Constants.CALCULATED,
					"[Measures].[Extended]", col_1122_tof);
			columns.add(col2);
			sumCols1120.add(col2.getColumnCode());
		}

		ColumnReport col3 = new ColumnReport("1120", Constants.SUM, sumCols1120);
		columns.add(col3);
		
		HashSet<String> sumCols1140 = new HashSet<String>();
		sumCols1140.addAll(sumCols1120);

		if(col_1130_tof != null && !col_1130_tof.equals("")){
			ColumnReport col4 = new ColumnReport("1130", Constants.CALCULATED,
					"[Measures].[Received]", col_1130_tof);
			col4.setMultiplier(-1);
			columns.add(col4);
			sumCols1140.add(col4.getColumnCode());
		}

		ColumnReport col5 = new ColumnReport("1140", Constants.SUM, sumCols1140);
		columns.add(col5);

		HashSet<String> sumCols1150 = new HashSet<String>();
		if(col_1151_tof != null && !col_1151_tof.equals("")){
			ColumnReport col6 = new ColumnReport("1151", Constants.CALCULATED,
					"[Measures].[Committed]", col_1151_tof);
			columns.add(col6);
			sumCols1150.add(col6.getColumnCode());
		}

		if(col_1152_tof != null && !col_1152_tof.equals("")){
			ColumnReport col7 = new ColumnReport("1152", Constants.CALCULATED,
					"[Measures].[Committed]", col_1152_tof);
			columns.add(col7);
			sumCols1150.add(col7.getColumnCode());
		}

		ColumnReport col8 = new ColumnReport("1150", Constants.SUM, sumCols1150);
		columns.add(col8);

		row.setColumns(columns);
		return row;

	}
}
