package org.devgateway.eudevfin.reports.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.reports.core.dao.RowReportDao;
import org.devgateway.eudevfin.reports.core.domain.ColumnReport;
import org.devgateway.eudevfin.reports.core.domain.RowReport;
import org.jgroups.util.UUID;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Arrays;

public class ReportTemplate {
    private static Logger logger = Logger.getLogger(ReportTemplate.class);
    
	private List<String> nonFlowItems = Arrays.asList("[Type of Finance].[TYPE_OF_FINANCE##1]","[Type of Finance].[TYPE_OF_FINANCE##2]","[Type of Finance].[TYPE_OF_FINANCE##3]", "[Type of Finance].[TYPE_OF_FINANCE##4]");

	public InputStream processTemplate(InputStream inputStream,
			String slicer, RowReportDao rowReportDao, boolean swapAxis, String reportName) {

		InputStream injectedStream = null;
		try {
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(inputStream);

			List<RowReport> rows = retrieveRows(rowReportDao, reportName);

			generateMDX(rows, doc, slicer);
			generateFields(rows, doc);
			generateTextElements(rows, doc, swapAxis);

			injectedStream = xmlToStream(doc);
			//logger.info(prettyPrint(doc));

		} catch (Exception e) {
			logger.error("Error processing template: " + e.getStackTrace());
		}

		return injectedStream;
	}

	private void generateTextElements(List<RowReport> rows, Document doc, boolean swapAxis) {

		HashMap<String, Node> matchingRows = new HashMap<String, Node>();
		HashMap<String, Node> matchingColumns = new HashMap<String, Node>();
	
		NodeList nodeList = doc.getElementsByTagName("reportElement");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE	&& node.getAttributes().getNamedItem("key") != null) {
				if (node.getAttributes().getNamedItem("key").getNodeValue().indexOf("r_") >= 0) {
					matchingRows.put(node.getAttributes().getNamedItem("key").getNodeValue(), node);
				} else if (node.getAttributes().getNamedItem("key").getNodeValue().indexOf("c_") >= 0) {
					matchingColumns.put(node.getAttributes().getNamedItem("key").getNodeValue(), node);
				}
			}
		}

		for (RowReport row : rows) {
			switch(row.getType()) {
				case Constants.CALCULATED:
					appendReportRows(matchingRows, matchingColumns, row, doc, swapAxis);
					break;
				case Constants.SUM:
					appendReportSumRows(matchingRows, matchingColumns, row, doc, swapAxis);
					break;
					
				case Constants.EMPTY:
					appendReportRows(matchingRows, matchingColumns, row, doc, swapAxis);
					break;
				default:
					logger.warn("Invalid row type definition: " + row.getType() + " for: " + row.getName());
			}
			
			
		}
	}

	private void appendReportSumRows(HashMap<String, Node> matchingRows, HashMap<String, Node> matchingColumns, RowReport row, Document doc, boolean swapAxis) {
		Node rowNode = matchingRows.get("r_" + row.getName());
		
		if(rowNode == null) return;
		HashMap<String, String> columns = new HashMap<String, String>();

		XPath xPath = XPathFactory.newInstance().newXPath();

		for(String rowCode : row.getRowCodes())	{
			try {
				NodeList nodes = (NodeList)xPath.evaluate("/jasperReport/detail/band/textField/reportElement[starts-with(@key, 'r_" + rowCode + "_c_')]", doc.getDocumentElement(), XPathConstants.NODESET);
				for (int i = 0; i < nodes.getLength(); ++i) {
				    Element e = (Element) nodes.item(i);
				    String columnKey = e.getAttribute("key");
				    String columnCode = columnKey.replaceFirst("r_" + rowCode + "_c_", "");
				    String expression = columns.get(columnCode) != null ? columns.get(columnCode) : "";
				    if(!e.getParentNode().getTextContent().equals("")) {
					    expression += e.getParentNode().getTextContent();
					    expression += "+";
				    }
				    columns.put(columnCode, expression);
				}
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}

		Integer yCoord, xCoord, width, height;
		xCoord = yCoord = 0;
		//Set default height
		height = 15;
		//Set default width
		width = 55;
		if(swapAxis) {
			 xCoord = rowNode.getAttributes().getNamedItem("x") != null ? Integer.parseInt(rowNode.getAttributes().getNamedItem("x").getNodeValue()) : 0;
		} else {
			 yCoord = rowNode.getAttributes().getNamedItem("y") != null ? Integer.parseInt(rowNode.getAttributes().getNamedItem("y").getNodeValue()) : 0;
		}
		
		
		for (Map.Entry<String, String> column : columns.entrySet()) {
			String cellStyle = (rowNode.getAttributes().getNamedItem("style") != null) ? rowNode.getAttributes().getNamedItem("style").getNodeValue() : "";
//			UUID uuid = UUID.randomUUID();
			Element textField = doc.createElement("textField");
			textField.setAttribute("pattern", "#,##0.00");
			Node columnNode = matchingColumns.get("c_" + column.getKey());
			if (columnNode == null) continue;
			Node parentNode;
			if(swapAxis) {
				parentNode = columnNode.getParentNode().getParentNode();
				yCoord = columnNode.getAttributes().getNamedItem("y") != null ? Integer.parseInt(columnNode.getAttributes().getNamedItem("y").getNodeValue()) : 0;
			} else {
				parentNode = rowNode.getParentNode().getParentNode();
				xCoord = columnNode.getAttributes().getNamedItem("x") != null ? Integer.parseInt(columnNode.getAttributes().getNamedItem("x").getNodeValue()) : 0;
				width = columnNode.getAttributes().getNamedItem("width") != null ? Integer.parseInt(columnNode.getAttributes().getNamedItem("width").getNodeValue()) : 0;
				height = rowNode.getAttributes().getNamedItem("height") != null ? Integer.parseInt(rowNode.getAttributes().getNamedItem("height").getNodeValue()) : 0;
				if(cellStyle.equals("") && (columnNode.getAttributes().getNamedItem("style") != null)){
					cellStyle = columnNode.getAttributes().getNamedItem("style").getNodeValue();
				}
				
			}
			Element reportElement = doc.createElement("reportElement");
			reportElement.setAttribute("key", "r_" + row.getName() + "_c_" +column.getKey());
			reportElement.setAttribute("x", xCoord.toString());
			reportElement.setAttribute("y", yCoord.toString());
			reportElement.setAttribute("width", width.toString());
			reportElement.setAttribute("height", height.toString());
			//reportElement.setAttribute("uuid", uuid.toString());

			if(!cellStyle.equals("")) {
				reportElement.setAttribute("style", cellStyle);
			}

			Element textElement = doc.createElement("textElement");
			textElement.setAttribute("textAlignment", "Right");

			Element textFieldExpression = doc.createElement("textFieldExpression");
			String columnValue = column.getValue();
			if(columnValue.endsWith("+")) {
				columnValue = columnValue.substring(0, columnValue.length() - 1);
			}
			CDATASection cdata = doc.createCDATASection(columnValue);

			textFieldExpression.appendChild(cdata);
			textField.appendChild(reportElement);
			textField.appendChild(textElement);
			textField.appendChild(textFieldExpression);
			parentNode.appendChild(textField);

		}
	}

	private void appendReportRows(HashMap<String, Node> matchingRows, HashMap<String, Node> matchingColumns, RowReport row, Document doc, boolean swapAxis) {
		Node rowNode = matchingRows.get("r_" + row.getName());
		if(rowNode == null) return;
		Integer rowMultiplier = 1;
		//TODO: Remove this terrible hack for the two columns of DAC2a that needs to subtract
		if(row.getName().equals("205") || row.getName().equals("215")){
			rowMultiplier = -1;
		}
		Integer yCoord, xCoord, width, height;
		xCoord = yCoord = 0;
		//Set default height
		height = 15;
		//Set default width
		width = 55;
		if(swapAxis) {
			 xCoord = rowNode.getAttributes().getNamedItem("x") != null ? Integer.parseInt(rowNode.getAttributes().getNamedItem("x").getNodeValue()) : 0;
		} else {
			 yCoord = rowNode.getAttributes().getNamedItem("y") != null ? Integer.parseInt(rowNode.getAttributes().getNamedItem("y").getNodeValue()) : 0;
		}

		Set<ColumnReport> columns = row.getColumns();
		//Store multipliers for later use
		HashMap<String, Integer> multipliersByColumn = new HashMap<String, Integer>(); 
		for (ColumnReport column : columns) {
			multipliersByColumn.put(column.getName(), column.getMultiplier());
		}		
		for (ColumnReport column : columns) {
//			UUID uuid = UUID.randomUUID();
			String cellStyle = (rowNode.getAttributes().getNamedItem("style") != null) ? rowNode.getAttributes().getNamedItem("style").getNodeValue() : "";

			Node columnNode = matchingColumns.get("c_" + column.getName());
			if (columnNode == null) continue;
			Node parentNode;
			if(swapAxis) {
				parentNode = columnNode.getParentNode().getParentNode();
				yCoord = columnNode.getAttributes().getNamedItem("y") != null ? Integer.parseInt(columnNode.getAttributes().getNamedItem("y").getNodeValue()) : 0;
			} else {
				parentNode = rowNode.getParentNode().getParentNode();
				xCoord = columnNode.getAttributes().getNamedItem("x") != null ? Integer.parseInt(columnNode.getAttributes().getNamedItem("x").getNodeValue()) : 0;
				width = columnNode.getAttributes().getNamedItem("width") != null ? Integer.parseInt(columnNode.getAttributes().getNamedItem("width").getNodeValue()) : 0;
				height = rowNode.getAttributes().getNamedItem("height") != null ? Integer.parseInt(rowNode.getAttributes().getNamedItem("height").getNodeValue()) : 0;
				if(cellStyle.equals("") && (columnNode.getAttributes().getNamedItem("style") != null)){
					cellStyle = columnNode.getAttributes().getNamedItem("style").getNodeValue();
				}
			}

			Element textField = doc.createElement("textField");
			textField.setAttribute("pattern", column.getPattern() != null ? column.getPattern() : "#,##0.00");

			Element reportElement = doc.createElement("reportElement");
			reportElement.setAttribute("key", "r_" + row.getName() + "_c_" + column.getName());
			reportElement.setAttribute("x", xCoord.toString());
			reportElement.setAttribute("y", yCoord.toString());
			reportElement.setAttribute("width", width.toString());
			if(row.getVisible() != null && !row.getVisible()){
				reportElement.setAttribute("height", "0");
			} else {
				reportElement.setAttribute("height", height.toString());
			}
			if(!cellStyle.equals("")) {
				reportElement.setAttribute("style", cellStyle);
			}
				
//			reportElement.setAttribute("uuid", uuid.toString());

			Element textElement = doc.createElement("textElement");
			textElement.setAttribute("textAlignment", "Right");

			Element textFieldExpression = doc.createElement("textFieldExpression");

			if (column.getType() == Constants.CALCULATED) {
				StringBuffer expression = new StringBuffer();
				String[] types = column.getSlicer().split(",");
				for(int i = 0; i < types.length; i++) {
					if(!types[i].equals("")) {
						String fieldName = row.getName() + "_" + column.getName() + "_" + shortenType(types[i]) + "_" + column.getMeasure();
						if(nonFlowItems.contains(column.getSlicer())) {
							expression.append("$F{" + fieldName + "}");
						} else {
							expression.append("checkNull($F{" + fieldName + "}).doubleValue()");
						}
						if(i != types.length - 1) {
							expression.append("+");
						}
					}
				}
				String finalExpression = "";
				
				Integer multiplier = rowMultiplier * column.getMultiplier();
				if(expression.length() > 0){
					finalExpression = "checkZero((" + expression.toString() + "), " + multiplier + ").doubleValue()";
				}

				CDATASection cdata = doc.createCDATASection(finalExpression);
				textFieldExpression.appendChild(cdata);
			} else if (column.getType() == Constants.SUM) {
				StringBuffer expression = new StringBuffer();
				Set<String> subcols = column.getColumnCodes(); 
				for(Iterator<String> it = subcols.iterator();it.hasNext();)	{
					String code = it.next();
					String[] types = code.split(",");
					for(int i = 0; i < types.length; i++) {
						String fieldName = row.getName() + "_" + types[i];
						expression.append("(");
						expression.append("checkNull($F{" + fieldName + "}).doubleValue()");
						String extractedColumnName = types[i].split("_")[0];
						Integer multiplier = multipliersByColumn.get(extractedColumnName);
						if(multiplier == null) {
							multiplier = 1;
						}
						if(multiplier != null && multiplier < 0) {
							expression.append("* (" + multiplier + ")");
						}
						expression.append(")");
						if(i != types.length - 1) {
							expression.append("+");
						}
					}
					if(it.hasNext()) {
						expression.append("+");
					}
				}
				String finalExpression = "";
				if(expression.length() > 0)
					finalExpression = "checkZero((" + expression.toString() + "), " + rowMultiplier + ").doubleValue()";

				CDATASection cdata = doc.createCDATASection(finalExpression);
				textFieldExpression.appendChild(cdata);
			} else {
				textField = doc.createElement("staticText");
				textFieldExpression = doc.createElement("text");
				CDATASection cdata = doc.createCDATASection("////////");
				textFieldExpression.appendChild(cdata);
			}

			textField.appendChild(reportElement);
			textField.appendChild(textElement);
			textField.appendChild(textFieldExpression);
			parentNode.appendChild(textField);
		}
		
	}

	private void generateFields(List<RowReport> rows, Document doc) {

		for (RowReport row : rows) {
			Set<ColumnReport> columns = row.getColumns();
			for (ColumnReport column : columns) {
				if (column.getType() == Constants.CALCULATED) {
					
					for(String type : column.getSlicer().split(",")) {
						String shortType = shortenType(type);
						if(!type.equals("")) {
							String fieldName = row.getName() + "_" + column.getName()
									+ "_" + shortType + "_"
									+ column.getMeasure();
							Element field = doc.createElement("field");
							field.setAttribute("name", fieldName);
							field.setAttribute("class", "java.lang.Number");

							Element fieldDescription = doc
									.createElement("fieldDescription");
							CDATASection cdata = doc.createCDATASection("Data(("
									+ column.getMeasure() + ", "
									+ type + "), [Type of Aid].["
									+ row.getName() + "])\n");
							fieldDescription.appendChild(cdata);
							field.appendChild(fieldDescription);
							Node background = doc.getElementsByTagName("background").item(0);
							doc.getDocumentElement().insertBefore(field, background);
							
						}
					}

				}
			}
		}
	}

	private void generateMDX(List<RowReport> rows, Document doc, String slicer) {
		if(slicer == null || slicer.equals("")) slicer = "[Type of Finance].[Code].Members";
		StringBuffer str = new StringBuffer();
		List<RowReport> calculatedRows = new ArrayList<RowReport>();
		for (RowReport row : rows) {
			if (row.getType() == Constants.CALCULATED) {
				calculatedRows.add(row);
			}
		}

		str.append("WITH\n");
		for (RowReport row : calculatedRows) {
			if (row.getType() == Constants.CALCULATED) {
				str.append("MEMBER ");
				str.append("[Type of Aid].[" + row.getName() + "]");
				str.append(" as SUM(");
				String formula = row.getFormula();
				if(formula.equals("")){
					formula = "[BiMultilateral].Members";
				}
				str.append(formula);
				str.append(")\n");
			}
		}
		str.append("\n");
		str.append("MEMBER [Measures].[E] AS [Measures].[Extended Amount Currency NATLOECD] \n");
		str.append("MEMBER [Measures].[R] AS [Measures].[Received Amount Currency NATLOECD] \n");
		str.append("MEMBER [Measures].[C] AS [Measures].[Commitments Amount Currency NATLOECD] \n");
		str.append("MEMBER [Measures].[CN] AS [Measures].[Negative Commitments Amount NATLOECD] \n");
		str.append("MEMBER [Measures].[RN] AS [Measures].[Negative Received Amount NATLOECD] \n");
		str.append("MEMBER [Measures].[A] AS [Measures].[Extended Amount No Flow] \n");
		str.append("MEMBER [Measures].[EE] AS [Measures].[Expert Extended Currency NATLOECD] \n");
		str.append("MEMBER [Measures].[EC] AS [Measures].[Expert Commitments Currency NATLOECD] \n");
		str.append("SELECT {");
		for (Iterator<RowReport> it = calculatedRows.iterator(); it.hasNext();) {
			RowReport row = it.next();
			str.append("[Type of Aid].[" + row.getName() + "]");
			if (it.hasNext())
				str.append(",");
		}
		str.append("}  ON ROWS, \n");
		
		str.append(" {[Measures].[E],[Measures].[R], [Measures].[RN], [Measures].[C], [Measures].[CN], [Measures].[A], [Measures].[EE], [Measures].[EC]}*" + slicer + " ON COLUMNS \n");
		str.append("FROM [Financial] \n");
		str.append("WHERE {[Reporting Year].[$P{REPORTING_YEAR}]} * {[Form Type].[bilateralOda.CRS], [Form Type].[multilateralOda.CRS], [Form Type].[nonOda.nonExport], [Form Type].[nonOda.export], [Form Type].[nonOda.privateGrants], [Form Type].[nonOda.privateMarket], [Form Type].[nonOda.otherFlows], [Form Type].[#null]}\n");
		Node queryString = doc.getElementsByTagName("queryString").item(0);
//		System.out.println(str.toString());
		CDATASection cdata = doc.createCDATASection(str.toString());
		queryString.appendChild(cdata);
	}

	public final InputStream xmlToStream(Document xml) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Source xmlSource = new DOMSource(xml);
		Result outputTarget = new StreamResult(outputStream);
		TransformerFactory.newInstance().newTransformer()
				.transform(xmlSource, outputTarget);
		InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
		return is;
	}

	public final String prettyPrint(Document xml) throws Exception {
		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tf.setOutputProperty(OutputKeys.INDENT, "yes");
		Writer out = new StringWriter();
		tf.transform(new DOMSource(xml), new StreamResult(out));
		return out.toString();
	}

	private String shortenType(String type) {
		String[] str = type.split("##");
		if(str.length == 2)	{
			return str[1].replace("]", "");
		}
		return type;
	}

	private List<RowReport> retrieveRows(RowReportDao rowReportDao, String reportName) {
		ArrayList<RowReport> rows = new ArrayList<RowReport>();
		for (Iterator<RowReport> it = rowReportDao.findByReportName(reportName).iterator(); it
				.hasNext();) {
			RowReport row = it.next();
			rows.add(row);
		}
		return rows;
	}
}
