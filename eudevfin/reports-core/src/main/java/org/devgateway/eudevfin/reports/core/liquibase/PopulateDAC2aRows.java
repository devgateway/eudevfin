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
		
		while(it.hasNext()){
			Area currentArea = it.next();
			ColumnReport col1 = new ColumnReport(currentArea.getCode(), Constants.CALCULATED,
					measure, "[Area].[" + currentArea.getCode() + "]");
			columns.add(col1);
		}
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
