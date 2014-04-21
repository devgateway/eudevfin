package org.devgateway.eudevfin.reports.core.liquibase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

import org.devgateway.eudevfin.common.liquibase.AbstractSpringCustomTaskChange;
import org.devgateway.eudevfin.financial.dao.AreaDaoImpl;
import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
import org.devgateway.eudevfin.financial.dao.ChannelCategoryDao;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
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

	@Autowired
	private ChannelCategoryDao channelCatDao;
	
	List<ChannelCategory> listChannels;
	
	
	@Override
	public void execute(Database database) throws CustomChangeException {
		List<Area> listAreas = areaDao.findAllAsList();
		insertFirstSection(listAreas);

		listChannels = channelCatDao.findAllAsList();
		insertSecondSection();

	}

	private void insertSecondSection() {
		RowReport row_201 = createDAC2aRowChannel("201", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##2]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B02]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE#618]",
				"[Measures].[E]"
				);
		rowReportDao.save(row_201);
		
		RowReport row_219 = createDAC2aRowChannel("219", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##2]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B02]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE#310]",
				"[Measures].[R]"
				);
		rowReportDao.save(row_219);
		
		RowReport row_210 = createDAC2aRowChannel("210", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##2]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B02]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE#310]",
				"[Measures].[R]"
				);
		rowReportDao.save(row_210);
		
		RowReport row_211 = createDAC2aRowChannel("211", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##2]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B02]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE#311]",
				"[Measures].[R]"
				);
		rowReportDao.save(row_211);
		
		RowReport row_204 = createDAC2aRowChannel("204", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##2]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B02]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Measures].[E]"
				);
		rowReportDao.save(row_204);
		
		RowReport row_205 = createDAC2aRowChannel("205", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##2]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B02]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE#410]",
				"[Measures].[R]"
				);
		rowReportDao.save(row_205);
		
		HashSet<String> sumRow218 = new HashSet<String>();
		sumRow218.add("204");
		sumRow218.add("205");
		RowReport row_218 = createDAC2aSumRow("218","DAC2aChannel", Constants.SUM, sumRow218);
		rowReportDao.save(row_218);

		HashSet<String> sumRow206 = new HashSet<String>();
		sumRow206.add("201");
		sumRow206.add("219");
		sumRow206.add("218");
		RowReport row_206 = createDAC2aSumRow("206", "DAC2aChannel", Constants.SUM,	sumRow206);
		rowReportDao.save(row_206);

		//TODO: Make two columns and subtract the interest received measure
		RowReport row_209a = createDAC2aRowChannel("209", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##2]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[B02]",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##410]",
				"[Measures].[E]"
				);
		rowReportDao.save(row_209a);
		
	}

	private void insertFirstSection(List<Area> listAreas) {

		
		RowReport row_210 = createDAC2aRowEmpty("210", "DAC2aArea", listAreas);
		rowReportDao.save(row_210);

		RowReport row_211 = createDAC2aRowEmpty("211", "DAC2aArea", listAreas);
		rowReportDao.save(row_211);

		RowReport row_201 = createDAC2aRowArea("201", Constants.CALCULATED,
				"{[BiMultilateral].[BI_MULTILATERAL##1],[BiMultilateral].[BI_MULTILATERAL##3]}",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"Except([Type of Aid].[Code].Members, [Type of Aid].[B02])",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE#210]",
				"[Measures].[E]",
				listAreas
				);
		rowReportDao.save(row_201);
		RowReport row_212 = createDAC2aRowArea("212", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[F01]",
				"[Sector].[60020]",
				"[Type of Finance].[TYPE_OF_FINANCE##610],[Type of Finance].[TYPE_OF_FINANCE#611],[Type of Finance].[TYPE_OF_FINANCE#612],[Type of Finance].[TYPE_OF_FINANCE#613],[Type of Finance].[TYPE_OF_FINANCE#614],[Type of Finance].[TYPE_OF_FINANCE#615],[Type of Finance].[TYPE_OF_FINANCE#616],[Type of Finance].[TYPE_OF_FINANCE#617]",
				"[Measures].[E]",
				listAreas
				);
		rowReportDao.save(row_212);
		
		RowReport row_221 = createDAC2aRowArea("221", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[F01]",
				"{[Sector].[60010],[Sector].[60030],[Sector].[60061],[Sector].[60062],[Sector].[60063]}",
				"[Type of Finance].[TYPE_OF_FINANCE##610],[Type of Finance].[TYPE_OF_FINANCE#611],[Type of Finance].[TYPE_OF_FINANCE#612],[Type of Finance].[TYPE_OF_FINANCE#613],[Type of Finance].[TYPE_OF_FINANCE#614],[Type of Finance].[TYPE_OF_FINANCE#615],[Type of Finance].[TYPE_OF_FINANCE#616],[Type of Finance].[TYPE_OF_FINANCE#617]",
				"[Measures].[E]",
				listAreas
				);
		rowReportDao.save(row_221);

		RowReport row_208 = createDAC2aRowArea("208", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##210]",
				"[Measures].[E]",
				listAreas
				);
		rowReportDao.save(row_208);
		
		RowReport row_219 = createDAC2aRowArea("219", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE#210]",
				"[Measures].[R]",
				listAreas
				);
		rowReportDao.save(row_219);
		
		RowReport row_204 = createDAC2aRowArea("204", Constants.CALCULATED,
				"{[BiMultilateral].[BI_MULTILATERAL##1],[BiMultilateral].[BI_MULTILATERAL##3]}",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##410],[Type of Finance].[TYPE_OF_FINANCE#621],[Type of Finance].[TYPE_OF_FINANCE##622],[Type of Finance].[TYPE_OF_FINANCE#623],[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE#511],[Type of Finance].[TYPE_OF_FINANCE##512]",
				"[Measures].[E]",
				listAreas
				);
		rowReportDao.save(row_204);
		
		RowReport row_214 = createDAC2aRowArea("214", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[F01]",
				"[Sector].[60040]",
				"[Type of Finance].[TYPE_OF_FINANCE#621],[Type of Finance].[TYPE_OF_FINANCE##622],[Type of Finance].[TYPE_OF_FINANCE#623]",
				"[Measures].[E]",
				listAreas
				);
		rowReportDao.save(row_214);
		
		RowReport row_205 = createDAC2aRowArea("205", Constants.CALCULATED,
				"{[BiMultilateral].[BI_MULTILATERAL##1],[BiMultilateral].[BI_MULTILATERAL##3]}",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"Except([Type of Aid].[Code].Members, [Type of Aid].[F02])",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##610]", //TODO: Correct this. This is wrong, it has no filter by Type of Finance
				"[Measures].[R]",
				listAreas
				);
		rowReportDao.save(row_205);
		
		RowReport row_215 = createDAC2aRowArea("215", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"[Type of Aid].[F01]",
				"[Sector].[60020]",
				"[Type of Finance].[TYPE_OF_FINANCE##610]",
				"[Measures].[R]",
				listAreas
				);
		rowReportDao.save(row_215);
		
		RowReport row_217 = createDAC2aRowArea("217", Constants.CALCULATED,
				"{[BiMultilateral].[BI_MULTILATERAL##1],[BiMultilateral].[BI_MULTILATERAL##3]}",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##510],[Type of Finance].[TYPE_OF_FINANCE#511],[Type of Finance].[TYPE_OF_FINANCE#512]",
				"[Measures].[E]",
				listAreas
				);
		rowReportDao.save(row_217);

		HashSet<String> sumRow218 = new HashSet<String>();
		sumRow218.add("204");
		sumRow218.add("205");
		sumRow218.add("215");

		RowReport row_218 = createDAC2aSumRow("218","DAC2aArea", Constants.SUM,	sumRow218);
		rowReportDao.save(row_218);

		HashSet<String> sumRow206 = new HashSet<String>();
		sumRow206.add("201");
		sumRow206.add("219");
		sumRow206.add("218");
		RowReport row_206 = createDAC2aSumRow("206", "DAC2aArea", Constants.SUM,	sumRow206);
		rowReportDao.save(row_206);
		//TODO: Add FTC=1 value
		RowReport row_207 = createDAC2aRowArea("207", Constants.CALCULATED,
				"{[BiMultilateral].[BI_MULTILATERAL##1],[BiMultilateral].[BI_MULTILATERAL##3]}",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"",
				"",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE#410]",
				"[Measures].[E]",
				listAreas
				);
		rowReportDao.save(row_207);
		RowReport row_213 = createDAC2aRowArea("213", Constants.CALCULATED,
				"{[BiMultilateral].[BI_MULTILATERAL##1],[BiMultilateral].[BI_MULTILATERAL##3]}",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"",
				"[Sector].[52010]",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE#410]",
				"[Measures].[E]",
				listAreas
				);
		rowReportDao.save(row_213);
		RowReport row_216 = createDAC2aRowArea("216", Constants.CALCULATED,
				"{[BiMultilateral].[BI_MULTILATERAL##1],[BiMultilateral].[BI_MULTILATERAL##3]}",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"",
				"[Sector].[Humanitarian Aid]",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE#410]",
				"[Measures].[E]",
				listAreas
				);
		rowReportDao.save(row_216);
		//TODO: Make two columns and subtract the interest received measure
		RowReport row_209a = createDAC2aRowArea("209", Constants.CALCULATED,
				"[BiMultilateral].[BI_MULTILATERAL##1]",
				"[Type of Flow].[TYPE_OF_FLOW##10]",
				"",
				"[Sector].[Humanitarian Aid]",
				"[Type of Finance].[TYPE_OF_FINANCE##110],[Type of Finance].[TYPE_OF_FINANCE#410]",
				"[Measures].[E]",
				listAreas
				);
		rowReportDao.save(row_209a);
		
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

	public RowReport createDAC2aRowArea(String name, int type, String biMulti,
			String typeOfFlow, String typeOfAid, String purposeCode, String typeOfFinance, String measure, List<Area> listAreas) {
		RowReport row = new RowReport("DAC2aArea", name, type);

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

		Map<String, String> columnsByCode = new HashMap<String, String>();
		
		while(it.hasNext()){
			Area currentArea = it.next();
			ColumnReport col1 = new ColumnReport(currentArea.getCode(), Constants.CALCULATED, measure, "[Area].[" + currentArea.getCode() + "]");
			columnsByCode.put(currentArea.getCode(), col1.getColumnCode());
			columns.add(col1);
		}

		HashSet<String> sumColsEuropa = new HashSet<String>();
		sumColsEuropa.add(columnsByCode.get("71"));
		sumColsEuropa.add(columnsByCode.get("86"));
		sumColsEuropa.add(columnsByCode.get("64"));
		sumColsEuropa.add(columnsByCode.get("66"));
		sumColsEuropa.add(columnsByCode.get("57"));
		sumColsEuropa.add(columnsByCode.get("93"));
		sumColsEuropa.add(columnsByCode.get("65"));
		sumColsEuropa.add(columnsByCode.get("63"));
		sumColsEuropa.add(columnsByCode.get("55"));
		sumColsEuropa.add(columnsByCode.get("85"));
		sumColsEuropa.add(columnsByCode.get("88"));
		sumColsEuropa.add(columnsByCode.get("89"));
		ColumnReport colEuropa = new ColumnReport("europe_total", Constants.SUM, sumColsEuropa);
		columns.add(colEuropa);

		HashSet<String> sumColsNorthOfSahara = new HashSet<String>();
		sumColsNorthOfSahara.add(columnsByCode.get("130"));
		sumColsNorthOfSahara.add(columnsByCode.get("142"));
		sumColsNorthOfSahara.add(columnsByCode.get("133"));
		sumColsNorthOfSahara.add(columnsByCode.get("136"));
		sumColsNorthOfSahara.add(columnsByCode.get("139"));
		sumColsNorthOfSahara.add(columnsByCode.get("189"));
		ColumnReport colNorthOfSahara= new ColumnReport("north_of_sahara", Constants.SUM, sumColsNorthOfSahara);
		columns.add(colNorthOfSahara);

		HashSet<String> sumColsSouthOfSahara = new HashSet<String>();
		sumColsSouthOfSahara.add(columnsByCode.get("225"));
		sumColsSouthOfSahara.add(columnsByCode.get("236"));
		sumColsSouthOfSahara.add(columnsByCode.get("227"));
		sumColsSouthOfSahara.add(columnsByCode.get("287"));
		sumColsSouthOfSahara.add(columnsByCode.get("228"));
		sumColsSouthOfSahara.add(columnsByCode.get("229"));
		sumColsSouthOfSahara.add(columnsByCode.get("230"));
		sumColsSouthOfSahara.add(columnsByCode.get("231"));
		sumColsSouthOfSahara.add(columnsByCode.get("232"));
		sumColsSouthOfSahara.add(columnsByCode.get("233"));
		sumColsSouthOfSahara.add(columnsByCode.get("235"));
		sumColsSouthOfSahara.add(columnsByCode.get("234"));
		sumColsSouthOfSahara.add(columnsByCode.get("247"));
		sumColsSouthOfSahara.add(columnsByCode.get("274"));
		sumColsSouthOfSahara.add(columnsByCode.get("245"));
		sumColsSouthOfSahara.add(columnsByCode.get("271"));
		sumColsSouthOfSahara.add(columnsByCode.get("238"));
		sumColsSouthOfSahara.add(columnsByCode.get("239"));
		sumColsSouthOfSahara.add(columnsByCode.get("240"));
		sumColsSouthOfSahara.add(columnsByCode.get("241"));
		sumColsSouthOfSahara.add(columnsByCode.get("243"));
		sumColsSouthOfSahara.add(columnsByCode.get("244"));
		sumColsSouthOfSahara.add(columnsByCode.get("248"));
		sumColsSouthOfSahara.add(columnsByCode.get("249"));
		sumColsSouthOfSahara.add(columnsByCode.get("251"));
		sumColsSouthOfSahara.add(columnsByCode.get("252"));
		sumColsSouthOfSahara.add(columnsByCode.get("253"));
		sumColsSouthOfSahara.add(columnsByCode.get("255"));
		sumColsSouthOfSahara.add(columnsByCode.get("256"));
		sumColsSouthOfSahara.add(columnsByCode.get("257"));
		sumColsSouthOfSahara.add(columnsByCode.get("259"));
		sumColsSouthOfSahara.add(columnsByCode.get("275"));
		sumColsSouthOfSahara.add(columnsByCode.get("260"));
		sumColsSouthOfSahara.add(columnsByCode.get("261"));
		sumColsSouthOfSahara.add(columnsByCode.get("266"));
		sumColsSouthOfSahara.add(columnsByCode.get("276"));
		sumColsSouthOfSahara.add(columnsByCode.get("268"));
		sumColsSouthOfSahara.add(columnsByCode.get("269"));
		sumColsSouthOfSahara.add(columnsByCode.get("270"));
		sumColsSouthOfSahara.add(columnsByCode.get("272"));
		sumColsSouthOfSahara.add(columnsByCode.get("273"));
		sumColsSouthOfSahara.add(columnsByCode.get("218"));
		sumColsSouthOfSahara.add(columnsByCode.get("279"));
		sumColsSouthOfSahara.add(columnsByCode.get("278"));
		sumColsSouthOfSahara.add(columnsByCode.get("280"));
		sumColsSouthOfSahara.add(columnsByCode.get("282"));
		sumColsSouthOfSahara.add(columnsByCode.get("283"));
		sumColsSouthOfSahara.add(columnsByCode.get("285"));
		sumColsSouthOfSahara.add(columnsByCode.get("288"));
		sumColsSouthOfSahara.add(columnsByCode.get("265"));
		sumColsSouthOfSahara.add(columnsByCode.get("289"));
		ColumnReport colSouthOfSahara= new ColumnReport("south_of_sahara", Constants.SUM, sumColsSouthOfSahara);
		columns.add(colSouthOfSahara);


		HashSet<String> sumColsNorthCentralTotal = new HashSet<String>();
		sumColsNorthCentralTotal.add(columnsByCode.get("376"));
		sumColsNorthCentralTotal.add(columnsByCode.get("377"));
		sumColsNorthCentralTotal.add(columnsByCode.get("352"));
		sumColsNorthCentralTotal.add(columnsByCode.get("336"));
		sumColsNorthCentralTotal.add(columnsByCode.get("338"));
		sumColsNorthCentralTotal.add(columnsByCode.get("378"));
		sumColsNorthCentralTotal.add(columnsByCode.get("340"));
		sumColsNorthCentralTotal.add(columnsByCode.get("342"));
		sumColsNorthCentralTotal.add(columnsByCode.get("381"));
		sumColsNorthCentralTotal.add(columnsByCode.get("347"));
		sumColsNorthCentralTotal.add(columnsByCode.get("349"));
		sumColsNorthCentralTotal.add(columnsByCode.get("351"));
		sumColsNorthCentralTotal.add(columnsByCode.get("354"));
		sumColsNorthCentralTotal.add(columnsByCode.get("358"));
		sumColsNorthCentralTotal.add(columnsByCode.get("385"));
		sumColsNorthCentralTotal.add(columnsByCode.get("364"));
		sumColsNorthCentralTotal.add(columnsByCode.get("366"));
		sumColsNorthCentralTotal.add(columnsByCode.get("382"));
		sumColsNorthCentralTotal.add(columnsByCode.get("383"));
		sumColsNorthCentralTotal.add(columnsByCode.get("384"));
		sumColsNorthCentralTotal.add(columnsByCode.get("380"));
		sumColsNorthCentralTotal.add(columnsByCode.get("389"));
		ColumnReport colNorthCentralTotal= new ColumnReport("north_central_total", Constants.SUM, sumColsNorthCentralTotal);
		columns.add(colNorthCentralTotal);

		HashSet<String> sumColsSouthTotal = new HashSet<String>();
		sumColsSouthTotal.add(columnsByCode.get("425"));
		sumColsSouthTotal.add(columnsByCode.get("428"));
		sumColsSouthTotal.add(columnsByCode.get("431"));
		sumColsSouthTotal.add(columnsByCode.get("434"));
		sumColsSouthTotal.add(columnsByCode.get("437"));
		sumColsSouthTotal.add(columnsByCode.get("440"));
		sumColsSouthTotal.add(columnsByCode.get("446"));
		sumColsSouthTotal.add(columnsByCode.get("451"));
		sumColsSouthTotal.add(columnsByCode.get("454"));
		sumColsSouthTotal.add(columnsByCode.get("457"));
		sumColsSouthTotal.add(columnsByCode.get("460"));
		sumColsSouthTotal.add(columnsByCode.get("463"));
		sumColsSouthTotal.add(columnsByCode.get("489"));
		ColumnReport colSouthTotal = new ColumnReport("south_total", Constants.SUM, sumColsSouthTotal);
		columns.add(colSouthTotal);


		HashSet<String> sumColsMiddleEast = new HashSet<String>();
		sumColsMiddleEast.add(columnsByCode.get("540"));
		sumColsMiddleEast.add(columnsByCode.get("543"));
		sumColsMiddleEast.add(columnsByCode.get("549"));
		sumColsMiddleEast.add(columnsByCode.get("555"));
		sumColsMiddleEast.add(columnsByCode.get("573"));
		sumColsMiddleEast.add(columnsByCode.get("550"));
		sumColsMiddleEast.add(columnsByCode.get("580"));
		sumColsMiddleEast.add(columnsByCode.get("589"));
		ColumnReport colMiddleEast= new ColumnReport("middle_east_total", Constants.SUM, sumColsMiddleEast);
		columns.add(colMiddleEast);

		HashSet<String> sumColsSouthCentralAsia = new HashSet<String>();
		sumColsSouthCentralAsia.add(columnsByCode.get("625"));
		sumColsSouthCentralAsia.add(columnsByCode.get("610"));
		sumColsSouthCentralAsia.add(columnsByCode.get("611"));
		sumColsSouthCentralAsia.add(columnsByCode.get("666"));
		sumColsSouthCentralAsia.add(columnsByCode.get("630"));
		sumColsSouthCentralAsia.add(columnsByCode.get("612"));
		sumColsSouthCentralAsia.add(columnsByCode.get("645"));
		sumColsSouthCentralAsia.add(columnsByCode.get("613"));
		sumColsSouthCentralAsia.add(columnsByCode.get("614"));
		sumColsSouthCentralAsia.add(columnsByCode.get("655"));
		sumColsSouthCentralAsia.add(columnsByCode.get("635"));
		sumColsSouthCentralAsia.add(columnsByCode.get("660"));
		sumColsSouthCentralAsia.add(columnsByCode.get("665"));
		sumColsSouthCentralAsia.add(columnsByCode.get("640"));
		sumColsSouthCentralAsia.add(columnsByCode.get("615"));
		sumColsSouthCentralAsia.add(columnsByCode.get("616"));
		sumColsSouthCentralAsia.add(columnsByCode.get("617"));
		sumColsSouthCentralAsia.add(columnsByCode.get("619"));
		sumColsSouthCentralAsia.add(columnsByCode.get("679"));
		sumColsSouthCentralAsia.add(columnsByCode.get("689"));
		ColumnReport colSouthCentralAsia= new ColumnReport("south_central_asia_total", Constants.SUM, sumColsSouthCentralAsia);
		columns.add(colSouthCentralAsia);

		HashSet<String> sumColsFarEast = new HashSet<String>();
		sumColsFarEast.add(columnsByCode.get("728"));
		sumColsFarEast.add(columnsByCode.get("730"));
		sumColsFarEast.add(columnsByCode.get("738"));
		sumColsFarEast.add(columnsByCode.get("740"));
		sumColsFarEast.add(columnsByCode.get("745"));
		sumColsFarEast.add(columnsByCode.get("751"));
		sumColsFarEast.add(columnsByCode.get("753"));
		sumColsFarEast.add(columnsByCode.get("755"));
		sumColsFarEast.add(columnsByCode.get("764"));
		sumColsFarEast.add(columnsByCode.get("765"));
		sumColsFarEast.add(columnsByCode.get("769"));
		sumColsFarEast.add(columnsByCode.get("789"));
		ColumnReport colFarEast = new ColumnReport("far_east_total", Constants.SUM, sumColsFarEast);
		columns.add(colFarEast);

		HashSet<String> sumColsOceaniaTotal = new HashSet<String>();
		sumColsOceaniaTotal.add(columnsByCode.get("831"));
		sumColsOceaniaTotal.add(columnsByCode.get("832"));
		sumColsOceaniaTotal.add(columnsByCode.get("836"));
		sumColsOceaniaTotal.add(columnsByCode.get("859"));
		sumColsOceaniaTotal.add(columnsByCode.get("860"));
		sumColsOceaniaTotal.add(columnsByCode.get("845"));
		sumColsOceaniaTotal.add(columnsByCode.get("856"));
		sumColsOceaniaTotal.add(columnsByCode.get("861"));
		sumColsOceaniaTotal.add(columnsByCode.get("862"));
		sumColsOceaniaTotal.add(columnsByCode.get("880"));
		sumColsOceaniaTotal.add(columnsByCode.get("866"));
		sumColsOceaniaTotal.add(columnsByCode.get("868"));
		sumColsOceaniaTotal.add(columnsByCode.get("870"));
		sumColsOceaniaTotal.add(columnsByCode.get("872"));
		sumColsOceaniaTotal.add(columnsByCode.get("854"));
		sumColsOceaniaTotal.add(columnsByCode.get("876"));
		sumColsOceaniaTotal.add(columnsByCode.get("889"));
		ColumnReport colOceaniaTotal= new ColumnReport("oceania_total", Constants.SUM, sumColsOceaniaTotal);
		columns.add(colOceaniaTotal);

		HashSet<String> sumColsAfricaTotal = new HashSet<String>();
		sumColsAfricaTotal.add(columnsByCode.get("north_of_sahara"));
		sumColsAfricaTotal.add(columnsByCode.get("south_of_sahara"));
		sumColsAfricaTotal.add(columnsByCode.get("298"));
		ColumnReport colAfricaTotal= new ColumnReport("africa_total", Constants.SUM, sumColsAfricaTotal);
		columns.add(colAfricaTotal);

		HashSet<String> sumColsAmericaTotal = new HashSet<String>();
		sumColsAmericaTotal.add(columnsByCode.get("north_central_total"));
		sumColsAmericaTotal.add(columnsByCode.get("south_total"));
		sumColsAmericaTotal.add(columnsByCode.get("498"));
		ColumnReport colAmericaTotal= new ColumnReport("america_total", Constants.SUM, sumColsAmericaTotal);
		columns.add(colAmericaTotal);

		HashSet<String> sumColsAsiaTotal = new HashSet<String>();
		sumColsAsiaTotal.add(columnsByCode.get("middle_east_total"));
		sumColsAsiaTotal.add(columnsByCode.get("south_central_asia_total"));
		sumColsAsiaTotal.add(columnsByCode.get("far_east_total"));
		sumColsAsiaTotal.add(columnsByCode.get("798"));
		ColumnReport colAsiaTotal= new ColumnReport("asia_total", Constants.SUM, sumColsAsiaTotal);
		columns.add(colAsiaTotal);

		row.setColumns(columns);
		
		return row;

	}
	
	public RowReport createDAC2aRowEmpty(String name, String reportName, List<Area> listAreas) {
		RowReport row = new RowReport(reportName, name, Constants.EMPTY);

		Set<ColumnReport> columns = new HashSet<ColumnReport>();		
		Iterator<Area> it = listAreas.iterator();

		while(it.hasNext()){
			Area currentArea = it.next();
			ColumnReport col1 = new ColumnReport(currentArea.getCode(), Constants.EMPTY, null, null);
			columns.add(col1);
		}

		ColumnReport colEuropa = new ColumnReport("europe_total", Constants.EMPTY, null);
		columns.add(colEuropa);

		ColumnReport colNorthOfSahara= new ColumnReport("north_of_sahara", Constants.EMPTY, null);
		columns.add(colNorthOfSahara);

		ColumnReport colSouthOfSahara= new ColumnReport("south_of_sahara", Constants.EMPTY, null);
		columns.add(colSouthOfSahara);

		ColumnReport colNorthCentralTotal= new ColumnReport("north_central_total", Constants.EMPTY, null);
		columns.add(colNorthCentralTotal);

		ColumnReport colSouthTotal = new ColumnReport("south_total", Constants.EMPTY, null);
		columns.add(colSouthTotal);

		ColumnReport colMiddleEast= new ColumnReport("middle_east_total", Constants.EMPTY, null);
		columns.add(colMiddleEast);

		ColumnReport colSouthCentralAsia= new ColumnReport("south_central_asia_total", Constants.EMPTY, null);
		columns.add(colSouthCentralAsia);

		ColumnReport colFarEast = new ColumnReport("far_east_total", Constants.EMPTY, null);
		columns.add(colFarEast);

		ColumnReport colOceaniaTotal= new ColumnReport("oceania_total", Constants.EMPTY, null);
		columns.add(colOceaniaTotal);

		ColumnReport colAfricaTotal= new ColumnReport("africa_total", Constants.EMPTY, null);
		columns.add(colAfricaTotal);

		ColumnReport colAmericaTotal= new ColumnReport("america_total", Constants.EMPTY, null);
		columns.add(colAmericaTotal);

		ColumnReport colAsiaTotal= new ColumnReport("asia_total", Constants.EMPTY, null);
		columns.add(colAsiaTotal);

		row.setColumns(columns);
		
		return row;

	}

	public RowReport createDAC2aSumRow(String name, String reportName, int type,
			HashSet<String> rowCodes) {
		RowReport row = new RowReport(reportName, name, type, rowCodes);
		return row;
	}
	public RowReport createDAC2aRowChannel(String name, int type, String biMulti,
			String typeOfFlow, String typeOfAid, String purposeCode, String typeOfFinance, String measure) {
		RowReport row = new RowReport("DAC2aChannel", name, type);

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
		Map<String, String> channelsByRow = getChannelMapping();
		Map<String, String> columnsByCode = new HashMap<String, String>();

		for (Map.Entry<String, String> currRow : channelsByRow.entrySet())
		{
			ColumnReport col1 = new ColumnReport(currRow.getKey(), Constants.CALCULATED,
					measure, currRow.getValue());
			columnsByCode.put(currRow.getKey(), col1.getColumnCode());
			columns.add(col1);
		}
		
		HashSet<String> sumCols992 = new HashSet<String>();
		sumCols992.add(columnsByCode.get("959"));
		sumCols992.add(columnsByCode.get("963"));
		sumCols992.add(columnsByCode.get("964"));
		sumCols992.add(columnsByCode.get("966"));
		sumCols992.add(columnsByCode.get("967"));
		sumCols992.add(columnsByCode.get("974"));
		sumCols992.add(columnsByCode.get("988"));
		sumCols992.add(columnsByCode.get("975"));
		ColumnReport col992 = new ColumnReport("992", Constants.SUM, sumCols992);
		columns.add(col992);

		row.setColumns(columns);
		return row;

	}

	private Map<String, String> getChannelMapping() {
		Map<String, String> channelsByRow = new HashMap<String, String>();
		List<ChannelCategory> channelCategoryList = listChannels;
		for (ChannelCategory currCategory : channelCategoryList)
		{
			if(currCategory.getDac2a3a() != null){
				channelsByRow.put(currCategory.getDac2a3a().toString(), "[Channel].[" + currCategory.getCode() + "]");
			}
			
		}
		channelsByRow.put("975", "[Channel].[41101],[Channel].[41102],[Channel].[41103],[Channel].[41104],[Channel].[41105],[Channel].[41106],[Channel].[41107],[Channel].[41110],[Channel].[41111],[Channel].[41112],[Channel].[41116],[Channel].[41120],[Channel].[41123],[Channel].[41125],[Channel].[41126],[Channel].[41127],[Channel].[41128],[Channel].[41129],[Channel].[41131],[Channel].[41132],[Channel].[41133],[Channel].[41134],[Channel].[41135],[Channel].[41136],[Channel].[41137],[Channel].[41138],[Channel].[41141],[Channel].[41142],[Channel].[41143],[Channel].[41144],[Channel].[41145],[Channel].[41146],[Channel].[41147],[Channel].[41148],[Channel].[41301],[Channel].[41302],[Channel].[41303],[Channel].[41304],[Channel].[41305],[Channel].[41306],[Channel].[41307],[Channel].[41308],[Channel].[41309],[Channel].[41310],[Channel].[41311],[Channel].[41312],[Channel].[41313],[Channel].[41314],[Channel].[41315],[Channel].[41316]");
		channelsByRow.put("939", "[Channel].[41101],[Channel].[41102],[Channel].[41103],[Channel].[41104],[Channel].[41105],[Channel].[41106],[Channel].[41107],[Channel].[41108],[Channel].[41110],[Channel].[41111],[Channel].[41112],[Channel].[41114],[Channel].[41116],[Channel].[41119],[Channel].[41120],[Channel].[41121],[Channel].[41122],[Channel].[41123],[Channel].[41125],[Channel].[41126],[Channel].[41127],[Channel].[41128],[Channel].[41129],[Channel].[41130],[Channel].[41131],[Channel].[41132],[Channel].[41133],[Channel].[41134],[Channel].[41135],[Channel].[41136],[Channel].[41137],[Channel].[41138],[Channel].[41140],[Channel].[41141],[Channel].[41142],[Channel].[41143],[Channel].[41144],[Channel].[41145],[Channel].[41146],[Channel].[41147],[Channel].[41148]");
		channelsByRow.put("927", "[Channel].[42000]");
		channelsByRow.put("900", "[Channel].[44003],[Channel].[44006]");
		channelsByRow.put("816", "[Channel].[46006],[Channel].[46008],[Channel].[46015],[Channel].[46016],[Channel].[46017],[Channel].[46018],[Channel].[46019],[Channel].[46020],[Channel].[46021],[Channel].[46022],[Channel].[46023]");
		channelsByRow.put("907", "[Channel].[43001],[Channel].[43002],[Channel].[43003],[Channel].[43004],[Channel].[43005]");
		channelsByRow.put("989", "[Channel].[21002],[Channel].[30010],[Channel].[47001],[Channel].[47002],[Channel].[47003],[Channel].[47005],[Channel].[47009],[Channel].[47011],[Channel].[47012],[Channel].[47013],[Channel].[47015],[Channel].[47019],[Channel].[47022],[Channel].[47025],[Channel].[47026],[Channel].[47027],[Channel].[47029],[Channel].[47034],[Channel].[47036],[Channel].[47037],[Channel].[47040],[Channel].[47044],[Channel].[47045],[Channel].[47046],[Channel].[47050],[Channel].[47058],[Channel].[47059],[Channel].[47061],[Channel].[47064],[Channel].[47065],[Channel].[47066],[Channel].[47067],[Channel].[47068],[Channel].[47073],[Channel].[47074],[Channel].[47076],[Channel].[47077],[Channel].[47078],[Channel].[47079],[Channel].[47080],[Channel].[47081],[Channel].[47082],[Channel].[47083],[Channel].[47084],[Channel].[47086],[Channel].[47087],[Channel].[47089],[Channel].[47092],[Channel].[47093],[Channel].[47095],[Channel].[47096],[Channel].[47097],[Channel].[47098],[Channel].[47100],[Channel].[47102],[Channel].[47105],[Channel].[47106],[Channel].[47107],[Channel].[47109],[Channel].[47110],[Channel].[47111],[Channel].[47112],[Channel].[47113],[Channel].[47116],[Channel].[47117],[Channel].[47118],[Channel].[47119],[Channel].[47120],[Channel].[47121],[Channel].[47122],[Channel].[47123],[Channel].[47127],[Channel].[47128],[Channel].[47129],[Channel].[47130],[Channel].[47131],[Channel].[47132],[Channel].[47136]");


		return channelsByRow;
	}

}
