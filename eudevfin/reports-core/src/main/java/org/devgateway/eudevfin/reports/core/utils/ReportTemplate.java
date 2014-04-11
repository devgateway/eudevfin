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

import org.devgateway.eudevfin.reports.core.dao.RowReportDao;
import org.devgateway.eudevfin.reports.core.domain.ColumnReport;
import org.devgateway.eudevfin.reports.core.domain.RowReport;
import org.jgroups.util.UUID;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReportTemplate {

	public InputStream processTemplate(InputStream inputStream,
			String reportTypeDac1, RowReportDao rowReportDao) {
		InputStream injectedStream = null;
		try {
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = dBuilder.parse(inputStream);

			List<RowReport> rows = retrieveRows(rowReportDao);

			generateMDX(rows, doc);
			generateFields(rows, doc);
			generateTextElements(rows, doc);

			injectedStream = xmlToStream(doc);
//			prettyPrint(doc);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return injectedStream;
	}

	private List<RowReport> retrieveRows(RowReportDao rowReportDao) {
		ArrayList<RowReport> rows = new ArrayList<RowReport>();
		for (Iterator<RowReport> it = rowReportDao.findAll().iterator(); it
				.hasNext();) {
			RowReport row = it.next();
			rows.add(row);
		}
		return rows;
	}

	private void generateTextElements(List<RowReport> rows, Document doc) {

		HashMap<String, Node> matchingRows = new HashMap<String, Node>();
		HashMap<String, Node> matchingColumns = new HashMap<String, Node>();
	
		NodeList nodeList = doc.getElementsByTagName("reportElement");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE
					&& node.getAttributes().getNamedItem("key") != null) {

				if (node.getAttributes().getNamedItem("key").getNodeValue()
						.indexOf("row_") >= 0) {
					matchingRows.put(node.getAttributes().getNamedItem("key")
							.getNodeValue(), node);
				} else if (node.getAttributes().getNamedItem("key")
						.getNodeValue().indexOf("column_") >= 0) {
					matchingColumns.put(node.getAttributes()
							.getNamedItem("key").getNodeValue(), node);
				}
			}
		}

		for (RowReport row : rows) {

			
			if(row.getType() == Constants.CALCULATED){
				appendReportRows(matchingRows, matchingColumns, row, doc);
			}
			else
			{
				appendReportSumRows(matchingRows, matchingColumns, row, doc);
			}
			
		}
	}
	private void appendReportSumRows(HashMap<String, Node> matchingRows, HashMap<String, Node> matchingColumns, RowReport row, Document doc) {
		Node rowNode = matchingRows.get("row_" + row.getName());
		System.out.println( row.getName());
		HashMap<String, String> columns = new HashMap<String, String>();

		//Accumulate expressions by columns
		//Get all columns related to these rows
		XPath xPath = XPathFactory.newInstance().newXPath();
		//Search for the nodes that will be summarized
		for(String rowCode : row.getRowCodes()){
			try {
				NodeList nodes = (NodeList)xPath.evaluate("/jasperReport/detail/band/textField/reportElement[starts-with(@key, 'row_" + rowCode + "_column_')]", doc.getDocumentElement(), XPathConstants.NODESET);
				for (int i = 0; i < nodes.getLength(); ++i) {
				    Element e = (Element) nodes.item(i);
				    String columnKey = e.getAttribute("key");
				    String columnCode = columnKey.replaceFirst("row_" + rowCode + "_column_", "");
				    String expression = columns.get(columnCode) != null? columns.get(columnCode) : "";
				    if(!e.getParentNode().getTextContent().equals("")){
					    expression += e.getParentNode().getTextContent();
					    expression += "+";
				    }
				    columns.put(columnCode, expression);
				}
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Integer yCoordRow = rowNode.getAttributes().getNamedItem("y") != null ? Integer.parseInt(rowNode.getAttributes().getNamedItem("y").getNodeValue()) : 0;
		for (Map.Entry<String, String> column : columns.entrySet())
		{
			UUID uuid = UUID.randomUUID();
			Element textField = doc.createElement("textField");
			textField.setAttribute("pattern", "#,##0.00");
			Node columnNode = matchingColumns.get("column_" + column.getKey());
			Integer xCoordColumn = columnNode.getAttributes().getNamedItem("x") != null ? Integer.parseInt(columnNode.getAttributes().getNamedItem("x").getNodeValue()) : 0;
			Element reportElement = doc.createElement("reportElement");
			//reportElement.setAttribute("key", "row_" + row.getName() + "_column_" + column.getKey());
			reportElement.setAttribute("x", xCoordColumn.toString());
			reportElement.setAttribute("y", yCoordRow.toString());
			reportElement.setAttribute("width", "55");
			reportElement.setAttribute("height", "15");
			reportElement.setAttribute("uuid", uuid.toString());
			Element textElement = doc.createElement("textElement");
			textElement.setAttribute("textAlignment", "Right");

			Element textFieldExpression = doc.createElement("textFieldExpression");
			CDATASection cdata = doc.createCDATASection(column.getValue() +"0");
			textFieldExpression.appendChild(cdata);
			textField.appendChild(reportElement);
			textField.appendChild(textElement);
			textField.appendChild(textFieldExpression);
			rowNode.getParentNode().getParentNode().appendChild(textField);

		}
	}

	private void appendReportRows(HashMap<String, Node> matchingRows, HashMap<String, Node> matchingColumns, RowReport row, Document doc) {
		Node rowNode = matchingRows.get("row_" + row.getName());
		if(rowNode == null) return;
		Integer yCoordRow = rowNode.getAttributes().getNamedItem("y") != null ? Integer.parseInt(rowNode.getAttributes().getNamedItem("y").getNodeValue()) : 0;
		Set<ColumnReport> columns = row.getColumns();
		for (ColumnReport column : columns) {
			UUID uuid = UUID.randomUUID();
			Element textField = doc.createElement("textField");
			textField.setAttribute("pattern", column.getPattern() != null ? column.getPattern(): "#,##0.00");

			Node columnNode = matchingColumns.get("column_" + column.getName());
			Integer xCoordColumn = columnNode.getAttributes().getNamedItem("x") != null ? Integer.parseInt(columnNode.getAttributes().getNamedItem("x").getNodeValue()) : 0;

			Element reportElement = doc.createElement("reportElement");
			reportElement.setAttribute("key", "row_" + row.getName() + "_column_" + column.getName());
			reportElement.setAttribute("x", xCoordColumn.toString());
			reportElement.setAttribute("y", yCoordRow.toString());
			reportElement.setAttribute("width", "55");
			reportElement.setAttribute("height", "15");
			reportElement.setAttribute("uuid", uuid.toString());

			Element textElement = doc.createElement("textElement");
			textElement.setAttribute("textAlignment", "Right");

			Element textFieldExpression = doc.createElement("textFieldExpression");

			if (column.getType() == Constants.CALCULATED) {
				StringBuffer expression = new StringBuffer();
				
				String[] types = column.getTypeOfFinance().split(",");
				for(int i = 0; i < types.length; i++){
					if(!types[i].equals("")){
						String fieldName = row.getName() + "_" + column.getName() + "_" + types[i] + "_" + column.getMeasure();
						//expression.append("(");
						expression.append("($F{" + fieldName + "} == null ? 0 :$F{" + fieldName + "}.intValue())");
						//expression.append("* (" + column.getMultiplier() + ")");
						//expression.append(")");
						
						if(i != types.length-1){
							expression.append("+");
						}
					}
				}
				if(expression.length() == 0){
					expression.append("\"////////\"");
				}

				CDATASection cdata = doc.createCDATASection(expression.toString());
				textFieldExpression.appendChild(cdata);
			} else if (column.getType() == Constants.SUM) {
				StringBuffer expression = new StringBuffer();
				Set<String> subcols = column.getColumnCodes(); 
				for(Iterator<String> it=subcols.iterator();it.hasNext();){
					String code = it.next();
					String[] types = code.split(",");
					for(int i = 0; i<types.length; i++){
						String fieldName = row.getName() + "_" + types[i];
						expression.append("(");
						expression.append("($F{" + fieldName + "} == null ? 0 :$F{" + fieldName + "}.intValue())");
						//TODO: Remove this terrible hack for the one column that needs to subtract
						if(types[i].indexOf("1130") == 0){
							expression.append("* (-1)");
						}
						expression.append(")");
						
						if(i != types.length-1)
							expression.append("+");
					}
					if(it.hasNext()){
						expression.append("+");
					}
				}
				CDATASection cdata = doc.createCDATASection(expression.toString());
				textFieldExpression.appendChild(cdata);
			}

			textField.appendChild(reportElement);
			textField.appendChild(textElement);
			textField.appendChild(textFieldExpression);
			rowNode.getParentNode().getParentNode().appendChild(textField);
		}
		
	}

	private void generateFields(List<RowReport> rows, Document doc) {

		for (RowReport row : rows) {
			Set<ColumnReport> columns = row.getColumns();
			for (ColumnReport column : columns) {
				if (column.getType() == Constants.CALCULATED) {
					
					for(String type : column.getTypeOfFinance().split(",")){
						if(!type.equals("")){
							String fieldName = row.getName() + "_" + column.getName()
									+ "_" + type + "_"
									+ column.getMeasure();
							Element field = doc.createElement("field");
							field.setAttribute("name", fieldName);
							field.setAttribute("class", "java.lang.Number");
							//The finances are all together in one field. Eventually will be moved.
							Element fieldDescription = doc
									.createElement("fieldDescription");
							CDATASection cdata = doc.createCDATASection("Data(("
									+ column.getMeasure() + ", "
									+ type + "), [Type of Aid].["
									+ row.getName() + "])\n");
							fieldDescription.appendChild(cdata);
							field.appendChild(fieldDescription);
							Node background = doc.getElementsByTagName("background")
									.item(0); // TODO: Improve way of locating section
							doc.getDocumentElement().insertBefore(field, background);
							
						}
					}

				}
			}
		}
	}

	private void generateMDX(List<RowReport> rows, Document doc) {
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
				str.append(row.getFormula());
				str.append(")");
			}
		}
		str.append("\n");
		str.append("MEMBER [Measures].[Extended] AS [Measures].[Extended Amount Currency NATLOECD] \n");
		str.append("MEMBER [Measures].[Received] AS [Measures].[Received Amount Currency NATLOECD] \n");
		str.append("MEMBER [Measures].[Committed] AS [Measures].[Commitments Amount Currency NATLOECD] \n");
		str.append("MEMBER [Measures].[Amount] AS [Measures].[Extended Amount No Flow] \n");
		str.append("SELECT {");
		for (Iterator<RowReport> it = calculatedRows.iterator(); it.hasNext();) {
			RowReport row = it.next();
			str.append("[Type of Aid].[" + row.getName() + "]");
			if (it.hasNext())
				str.append(",");
		}
		str.append("}  ON ROWS, \n");
		str.append(" {[Measures].[Extended],[Measures].[Received],[Measures].[Committed], [Measures].[Amount]}*[Type of Finance].[Code].Members ON COLUMNS \n");
		str.append("FROM [Financial] \n");
		str.append("WHERE {[Reporting Year].[$P{REPORTING_YEAR}]} * {[Form Type].[bilateralOda.CRS], [Form Type].[multilateralOda.CRS]}\n");
		Node queryString = doc.getElementsByTagName("queryString").item(0);
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

	// TODO: Remove this before release
	public final void prettyPrint(Document xml) throws Exception {
		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tf.setOutputProperty(OutputKeys.INDENT, "yes");
		Writer out = new StringWriter();
		tf.transform(new DOMSource(xml), new StreamResult(out));
		System.out.println(out.toString());
	}


}
