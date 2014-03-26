package org.devgateway.eudevfin.reports.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.devgateway.eudevfin.reports.core.domain.Column;
import org.devgateway.eudevfin.reports.core.domain.Formula;
import org.devgateway.eudevfin.reports.core.domain.Row;
import org.jgroups.util.UUID;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ReportTemplate {
	public static InputStream processTemplate(InputStream inputStream, String reportTypeDac1) {
		InputStream injectedStream = null;
		try {
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(inputStream);
	
			List<Row> rows = generateRows(); //This is to be replaced for other objects that will behave similarly
			
			generateMDX(rows, doc);
			generateFields(rows, doc);
			//TODO: not working yet
			// generateTextElements(rows, doc); 

			injectedStream = xmlToStream(doc);
			prettyPrint(doc);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return injectedStream;
	}
	
	
	
	private static String generateTextElements(List<Row> rows, Document doc) {
		//Update this to use XML doc
		StringBuffer str = new StringBuffer();

		for (Iterator<Row> it = rows.iterator(); it.hasNext();) {
			Row row = it.next();
			List<Column> columns = row.getColumns();
			//68
			int coordX = 300;
			int increment = 60;
			for (Iterator<Column> it2 = columns.iterator(); it2.hasNext();) {
				Column column = it2.next();
				String fieldName = row.getName() + "_" + column.getName() + "_"
						+ column.getTypeOfFinance() + "_" + column.getMeasure();
				UUID uuid = UUID.randomUUID();
				str.append("<textField pattern=\"#,##0.00\">");
				str.append("<reportElement x=\"" + coordX + "\" y=\"247\" width=\"56\" height=\"15\" uuid=\"" + uuid.toString() + "\"/>");
				str.append("<textElement textAlignment=\"Right\"/>");
				str.append("<textFieldExpression><![CDATA[$F{" + fieldName + "} == null ? 0 :$F{" + fieldName + "}.intValue()]]></textFieldExpression>");
				str.append("</textField>");
				coordX += increment;
			}
		}
		return str.toString();
	}

	private static void  generateFields(List<Row> rows, Document doc) {

		for (Iterator<Row> it = rows.iterator(); it.hasNext();) {
			Row row = it.next();
			List<Column> columns = row.getColumns();
			for (Iterator<Column> it2 = columns.iterator(); it2.hasNext();) {
				Column column = it2.next();
				String fieldName = row.getName() + "_" + column.getName() + "_"
						+ column.getTypeOfFinance() + "_" + column.getMeasure();
				Element field = doc.createElement("field");
				field.setAttribute("name", fieldName);
				field.setAttribute("class", "java.lang.Number");

				Element fieldDescription = doc.createElement("fieldDescription");
				CDATASection cdata = doc.createCDATASection("Data((" + column.getMeasure() + ", "
						+ column.getTypeOfFinance() + "), [Type of Aid].["
						+ row.getName() + "])\n");
				fieldDescription.appendChild(cdata);
				field.appendChild(fieldDescription);
				Node background = doc.getElementsByTagName("background").item(0); //TODO: Improve way of locating section
				doc.getDocumentElement().insertBefore(field, background);
			}
		}
	}

	private static void generateMDX(List<Row> rows, Document doc) {
		StringBuffer str = new StringBuffer();

		str.append("WITH\n");
		for (Iterator<Row> it = rows.iterator(); it.hasNext();) {
			Row row = it.next();
			str.append("MEMBER ");
			str.append("[Type of Aid].[" + row.getName() + "]");
			str.append(" as SUM(");
			str.append(row.getFormula());
			str.append(")");
		}
		str.append("\n");
		str.append("MEMBER [Measures].[Extended] AS [Measures].[Extended Amount Currency NATLOECD] \n");
		str.append("MEMBER [Measures].[Received] AS [Measures].[Received Amount Currency NATLOECD] \n");
		str.append("MEMBER [Measures].[Committed] AS [Measures].[Commitments Amount Currency NATLOECD] \n");
		str.append("SELECT {");
		for (Iterator<Row> it = rows.iterator(); it.hasNext();) {
			Row row = it.next();
			str.append("[Type of Aid].[" + row.getName() + "]");
			if (it.hasNext())
				str.append(",");
		}
		str.append("}  ON ROWS, \n");
		str.append("NON EMPTY {[Measures].[Extended],[Measures].[Received],[Measures].[Committed]}*[Type of Finance].[Code].Members ON COLUMNS \n");
		str.append("FROM [Financial] \n");
		str.append("WHERE [Reporting Year].[2011] \n"); //TODO: Replace with appropriate JR Parameter
		Node queryString = doc.getElementsByTagName("queryString").item(0); //TODO: Check if the element exist, if it doesn't, add it.
		CDATASection cdata = doc.createCDATASection(str.toString());
		queryString.appendChild(cdata);
	}

	
	public static final InputStream xmlToStream(Document xml) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Source xmlSource = new DOMSource(xml);
		Result outputTarget = new StreamResult(outputStream);
		TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
		InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
		return is;
	}

	//TODO: Remove this before release
	public static final void prettyPrint(Document xml) throws Exception {
		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tf.setOutputProperty(OutputKeys.INDENT, "yes");
		Writer out = new StringWriter();
		tf.transform(new DOMSource(xml), new StreamResult(out));
		System.out.println(out.toString());
	}

	/*
	 * Generates row definitions for development purposes
	 * */ 
	private static List<Row> generateRows() {
		List<Row> rows = new ArrayList<Row>();
		Row row = new Row();
		row.setName("1110");
		Formula formula = new Formula();

		List<String> listBiMultilateral = new ArrayList<String>();
		List<String> listTypeOfFlow = new ArrayList<String>();
		List<String> listTypeofAid = new ArrayList<String>();
		List<String> listPurpose = new ArrayList<String>();
		/*
		List<String> listSectors = new ArrayList<String>();
		List<String> listTypeOfFinance = new ArrayList<String>();
		List<String> listChannel = new ArrayList<String>();
		*/

		listBiMultilateral.add("[BiMultilateral].[BI_MULTILATERAL##1]");
		listTypeOfFlow.add("[Type of Flow].[TYPE_OF_FLOW##10]");
		listTypeofAid.add("[Type of Aid].[A01]");
		listPurpose.add("[Sector].[51010]");

		formula.setListBiMultilateral(listBiMultilateral);
		formula.setListTypeOfFlow(listTypeOfFlow);
		formula.setListTypeofAid(listTypeofAid);
		formula.setListPurpose(listPurpose);

		List<Column> columns = new ArrayList<Column>();

		Column col1 = new Column();
		col1.setName("1121");
		col1.setMeasure("[Measures].[Extended]");
		col1.setTypeOfFinance("[Type of Finance].[TYPE_OF_FINANCE##110]");
		columns.add(col1);

		Column col2 = new Column();
		col2.setName("1122");
		col2.setMeasure("[Measures].[Extended]");
		col2.setTypeOfFinance("[Type of Finance].[TYPE_OF_FINANCE##410]");
		columns.add(col2);

		Column col3 = new Column();
		col3.setName("1139");
		col3.setMeasure("[Measures].[Received]");
		col3.setTypeOfFinance("[Type of Finance].[TYPE_OF_FINANCE##410]");
		columns.add(col3);

		Column col4 = new Column();
		col4.setName("1151");
		col4.setMeasure("[Measures].[Committed]");
		col4.setTypeOfFinance("[Type of Finance].[TYPE_OF_FINANCE##110]");
		columns.add(col4);

		Column col5 = new Column();
		col5.setName("1152");
		col5.setMeasure("[Measures].[Committed]");
		col5.setTypeOfFinance("[Type of Finance].[TYPE_OF_FINANCE##410]");
		columns.add(col5);

		row.setColumns(columns);
		row.setFormula(formula);
		rows.add(row);
		return rows;
	}


}
