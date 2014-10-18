/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.reports.core.controller;

import mondrian.olap.Connection;
import mondrian.olap.DriverManager;
import mondrian.olap.Util.PropertyList;
import mondrian.rolap.RolapConnectionProperties;
import org.apache.log4j.Logger;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.common.locale.LocaleHelper;
import org.devgateway.eudevfin.financial.util.FinancialTransactionUtil;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.reports.core.dao.RowReportDao;
import org.joda.money.CurrencyUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

@Controller
public class ReportsController {
	@Autowired
	private DataSource cdaDataSource;

	@Autowired
	private RowReportDao rowReportDao;

    private static Logger logger = Logger.getLogger(ReportsController.class);

    public static final String REPORT_DATASOURCE_AQ = "Advance Questionnaire input form";
    public static final String REPORT_DATASOURCE_CRS = "CRS++ input form";

	public static final String REPORT_TYPE = "reportType";
	public static final String REPORT_TYPE_AQ = "aq";
	public static final String REPORT_TYPE_DAC1 = "dac1";
	public static final String REPORT_TYPE_DAC2A = "dac2a";
	public static final String OUTPUT_TYPE = "outputType";
	public static final String DATASOURCE = "dataSourceGroup:dataSource";
	public static final String OUTPUT_TYPE_PDF = "pdf";
	public static final String OUTPUT_TYPE_EXCEL = "excel";
	public static final String OUTPUT_TYPE_HTML = "html";
	public static final String OUTPUT_TYPE_CSV = "csv";
	public static final String REPORT_YEAR = "reportYear";
	public static final String REPORT_CURRENCY = "reportCurrency";
	public static final String REPORT_DEFAULT_CURRENCY_CODE = "USD";

	@Autowired
	ApplicationContext applicationContext;

	/**
	 * Set currency
	 */
	@PreAuthorize("hasRole('" + AuthConstants.Roles.ROLE_USER + "')")
    @RequestMapping(value = "/currency", method = {
    			RequestMethod.GET, RequestMethod.POST
    		})
    public ModelAndView setCurrency(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView)  throws IOException {
		String currency = request.getParameter(REPORT_CURRENCY);
		RequestContextHolder.getRequestAttributes().setAttribute("CURRENCY",
				currency, RequestAttributes.SCOPE_SESSION);
		return null;
	}
	/**
	 * get currency list
	 */
	@PreAuthorize("hasRole('" + AuthConstants.Roles.ROLE_USER + "')")
    @RequestMapping(value = "/currencylist", method = {
    			RequestMethod.GET, RequestMethod.POST
    		})
    public ModelAndView getCurrencyList(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView)  throws IOException {
		ModelAndView mav = new ModelAndView();
		mav.setView(new MappingJackson2JsonView());
        CurrencyUnit currencyForCountryIso = FinancialTransactionUtil
                .getCurrencyForCountryIso(AuthUtils
                        .getIsoCountryForCurrentUser());
        //Add EUR, USD and the country currency
        String[] currencyList = {"EUR", "USD", currencyForCountryIso.getCode()};
		mav.addObject("list", currencyList);
		mav.addObject("selectedCurrency", getCurrencyParameter());
		return mav;
	}

    private String getCurrencyParameter() {
    	String currency;
    	if(RequestContextHolder.getRequestAttributes() != null && RequestContextHolder.getRequestAttributes().getAttribute("CURRENCY", RequestAttributes.SCOPE_SESSION) != null && !"".equals((String)RequestContextHolder.getRequestAttributes().getAttribute("CURRENCY", RequestAttributes.SCOPE_SESSION)))
    	{
    		currency = (String)RequestContextHolder.getRequestAttributes().getAttribute("CURRENCY", RequestAttributes.SCOPE_SESSION);
    	}
    	else
    	{
    		currency = REPORT_DEFAULT_CURRENCY_CODE;
    	}
		return currency;
	}

	/**
	 * Generate a report
	 */
	@PreAuthorize("hasRole('" + AuthConstants.Roles.ROLE_USER + "')")
    @RequestMapping(value = "/generate", method = {
    			RequestMethod.GET, RequestMethod.POST
    		})
    public ModelAndView generateReport(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView)  throws IOException {
		String reportType = request.getParameter(REPORT_TYPE);
		String outputType = request.getParameter(OUTPUT_TYPE);
		String dataSource = request.getParameter(DATASOURCE);

		// create the Mondrian connection
		PropertyList propertyList = new PropertyList();
		propertyList.put("Provider", "mondrian");
		// don't add the Catalog here (but the Connect string must contain a property 'Catalog')
		// it will be added in Schema Processor
		propertyList.put("Catalog", "somePath");
        propertyList.put(RolapConnectionProperties.DynamicSchemaProcessor.toString(),
				"org.devgateway.eudevfin.reports.core.utils.SchemaProcessor");

		String currency = request.getParameter(REPORT_CURRENCY);
		if (currency == null || currency.equals("")) {
			currency = REPORT_DEFAULT_CURRENCY_CODE;
		}
		propertyList.put("CURRENCY", currency);

        LocaleHelper localeHelper = (LocaleHelper) applicationContext.getBean("localeHelperSession");
        String locale = (localeHelper.getLocale() != null) ? localeHelper.getLocale() : "en";
		propertyList.put("LOCALE", locale);

		// add the country currency parameter
		String countryCurrency = "";
		CurrencyUnit currencyForCountryIso = FinancialTransactionUtil
				.getCurrencyForCountryIso(AuthUtils
						.getIsoCountryForCurrentUser());
		if (currencyForCountryIso != null) {
			countryCurrency = currencyForCountryIso.getCode();
		}
		propertyList.put("COUNTRY_CURRENCY", countryCurrency);

		Connection connection = DriverManager.getConnection(propertyList, null,
				cdaDataSource);

		// add default values
		if (reportType == null || reportType.equals("")) {
			reportType = REPORT_TYPE_AQ;
		}

		if (outputType == null || outputType.equals("")) {
			outputType = OUTPUT_TYPE_HTML;
		}

		switch (reportType) {
		case REPORT_TYPE_AQ:
            generateAdvanceQuestionnaire(request, response, connection, outputType, currency, dataSource);
			break;
		case REPORT_TYPE_DAC1:
			generateDAC1(request, response, connection, outputType);
			break;
		case REPORT_TYPE_DAC2A:
			generateDAC2a(request, response, connection, outputType);
			break;
		default:
			break;
		}

		return null;
	}

    /**
     * Publish a report
     */
    @PreAuthorize("hasRole('" + AuthConstants.Roles.ROLE_SUPERVISOR + "')")
    @RequestMapping(value = "/publish", method = {
            RequestMethod.GET, RequestMethod.POST
    })
    public ModelAndView publishReport(HttpServletRequest request,
                                      HttpServletResponse response,
                                      ModelAndView modelAndView)  throws IOException {
        String reportType = request.getParameter(REPORT_TYPE);
        String yearParam = request.getParameter(REPORT_YEAR);
        String dataSource = request.getParameter(DATASOURCE);

        // add default values
        if (reportType == null || reportType.equals("")) {
            reportType = REPORT_TYPE_AQ;
        }
        if (yearParam == null || yearParam.equals("")) {
            yearParam = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        }
        String currency = request.getParameter(REPORT_CURRENCY);
        if (currency == null || currency.equals("")) {
            currency = REPORT_DEFAULT_CURRENCY_CODE;
        }

        if (REPORT_TYPE_AQ.equals(reportType)) {
            if (!REPORT_DATASOURCE_CRS.equals(dataSource)) {
                dataSource = REPORT_DATASOURCE_AQ;
            }
        }


        // get the name of the Country
        String serverInstance = "";
        Organization organizationForCurrentUser = AuthUtils.getOrganizationForCurrentUser();
        if (organizationForCurrentUser != null) {
            serverInstance = organizationForCurrentUser.getDonorName();
        }

        // get the Mondrian connection
        Connection connection = ReportsControllerUtils.createMondrianConnection(applicationContext, cdaDataSource, request);

        // set the files path and names
        String tmpDirPath = System.getProperty("java.io.tmpdir");
        String dirPath = tmpDirPath + File.separator + serverInstance +  "Repository" +
                File.separator + reportType;
        String filePathPDF = dirPath + File.separator + reportType.toUpperCase() + "_"  +
                yearParam + (dataSource == null ? "" : "_" + dataSource) + ".pdf";
        String filePathXLS = dirPath + File.separator + reportType.toUpperCase() + "_"  +
                yearParam + (dataSource == null ? "" : "_" + dataSource) + ".xls";

        File dir = new File(dirPath);
        File filePDF = new File(filePathPDF);
        File fileXLS = new File(filePathXLS);
        FileOutputStream fosPDF = null;
        FileOutputStream fosXLS = null;
        ByteArrayOutputStream baosPDF;
        ByteArrayOutputStream baosXLS;

        try {
            // create the directory structure
            dir.mkdirs();

            // delete the files if it already exist
            if (filePDF.exists()) {
                filePDF.delete();
            }
            if (fileXLS.exists()) {
                fileXLS.delete();
            }

            filePDF.createNewFile();
            fileXLS.createNewFile();
            fosPDF = new FileOutputStream (filePDF);
            fosXLS = new FileOutputStream (fileXLS);

            // generate the files (both PDF and Excel)
            switch (reportType) {
                case REPORT_TYPE_AQ:
                    baosPDF = ReportsControllerUtils.generateAdvanceQuestionnaire(connection, yearParam,
                            OUTPUT_TYPE_PDF, currency, dataSource);
                    baosPDF.writeTo(fosPDF);

                    baosXLS = ReportsControllerUtils.generateAdvanceQuestionnaire(connection, yearParam,
                            OUTPUT_TYPE_EXCEL, currency, dataSource);
                    baosXLS.writeTo(fosXLS);

                    break;
                case REPORT_TYPE_DAC1:
                    baosPDF = ReportsControllerUtils.generateDAC1(connection, rowReportDao, yearParam, OUTPUT_TYPE_PDF);
                    baosPDF.writeTo(fosPDF);

                    baosXLS = ReportsControllerUtils.generateDAC1(connection, rowReportDao, yearParam, OUTPUT_TYPE_EXCEL);
                    baosXLS.writeTo(fosXLS);

                    break;
                case REPORT_TYPE_DAC2A:
                    baosPDF = ReportsControllerUtils.generateDAC2a(connection, rowReportDao, yearParam, OUTPUT_TYPE_PDF);
                    baosPDF.writeTo(fosPDF);

                    baosXLS = ReportsControllerUtils.generateDAC2a(connection, rowReportDao, yearParam, OUTPUT_TYPE_EXCEL);
                    baosXLS.writeTo(fosXLS);

                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            logger.error("Publishing reports failed!", e);
            response.getWriter().println("Published successfully!");

            return null;
        }  finally {
            fosPDF.close();
            fosXLS.close();
        }

        response.getWriter().println("Published successfully!");

        return null;
    }

	/**
	 * Create the Advance Questionnaire report
	 *
	 * @param request
	 * @param response
     * @param connection the Mondrian connection
     * @param outputType the output for the report: HTML, Excel, PDF, CSV
	 * @param dataSource
	 */
    private void generateAdvanceQuestionnaire (HttpServletRequest request, HttpServletResponse response,
            Connection connection, String outputType, String currency, String dataSource) {
		String yearParam = request.getParameter(REPORT_YEAR);
		if (yearParam == null || yearParam.equals("")) {
            yearParam = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		}
		ByteArrayOutputStream baos = ReportsControllerUtils.generateAdvanceQuestionnaire(connection, yearParam,
                outputType, currency, dataSource);

		try {
			String fileName;

			switch (outputType) {
			case OUTPUT_TYPE_PDF:
				fileName = "Advance Questionnaire.pdf";
                response.setHeader("Content-Disposition", "inline; filename=" + fileName);
				response.setContentType("application/pdf");
				break;
			case OUTPUT_TYPE_EXCEL:
				fileName = "Advance Questionnaire.xls";
                response.setHeader("Content-Disposition", "inline; filename=" + fileName);
				response.setContentType("application/vnd.ms-excel");
				break;
			case OUTPUT_TYPE_HTML:
				fileName = "Advance Questionnaire.html";
                response.setHeader("Content-Disposition", "inline; filename=" + fileName);
				response.setContentType("text/html");
				break;
			case OUTPUT_TYPE_CSV:
				fileName = "Advance Questionnaire.csv";
                response.setHeader("Content-Disposition", "inline; filename=" + fileName);
				response.setContentType("text/csv");
				break;
			default:
				break;
			}

			response.setContentLength(baos.size());

			// write to response stream
			this.writeReportToResponseStream(response, baos);
		} catch (Exception e) {
			response.setContentType("text/html");

			try {
				baos.write("No data/Data Invalid".getBytes());
				logger.error("Error creating the AQ report", e);
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
			this.writeReportToResponseStream(response, baos);
		}
	}

	/**
	 * Create the DAC2a report
	 *
	 * @param request
	 * @param response
     * @param connection the Mondrian connection
     * @param outputType the output for the report: HTML, Excel, PDF, CSV
	 */
    private void generateDAC2a (HttpServletRequest request, HttpServletResponse response, Connection connection, String outputType) {
        // Assign Reporting Year
        String yearParam = request.getParameter(REPORT_YEAR);
        if (yearParam == null || yearParam.equals("")) {
            yearParam = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        }

        ByteArrayOutputStream baos = ReportsControllerUtils.generateDAC2a(connection, rowReportDao, yearParam, outputType);

		try {
			String fileName;

			switch (outputType) {
				case OUTPUT_TYPE_PDF:
					fileName = "DAC 2a.pdf";
	                response.setHeader("Content-Disposition", "inline; filename=" + fileName);
					response.setContentType("application/pdf");
					break;
				case OUTPUT_TYPE_EXCEL:
					fileName = "DAC 2a.xls";
                    response.setHeader("Content-Disposition", "inline; filename=" + fileName);
					response.setContentType("application/vnd.ms-excel");
					break;
				case OUTPUT_TYPE_HTML:
					fileName = "DAC 2a.html";
                    response.setHeader("Content-Disposition", "inline; filename=" + fileName);
					response.setContentType("text/html");
					break;
				case OUTPUT_TYPE_CSV:
					fileName = "DAC 2a.csv";
                    response.setHeader("Content-Disposition", "inline; filename=" + fileName);
					response.setContentType("text/csv");
					break;
				default:
					break;
			}

			response.setContentLength(baos.size());

			// write to response stream
			this.writeReportToResponseStream(response, baos);
        } catch (Exception e) {
            response.setContentType("text/html");

            try {
                baos.write("No data/Data Invalid".getBytes());
                logger.error("Error creating the DAC2a report", e);
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
            this.writeReportToResponseStream(response, baos);
        }
	}

    /**
     * Create the DAC1 report
     *
     * @param request
     * @param response
     * @param connection
     * @param outputType
     */
	private void generateDAC1(HttpServletRequest request,
			HttpServletResponse response, Connection connection,
			String outputType) {
        String yearParam = request.getParameter(REPORT_YEAR);
        if (yearParam == null || yearParam.equals("")) {
            yearParam = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        }

        ByteArrayOutputStream baos = ReportsControllerUtils.generateDAC1(connection, rowReportDao, yearParam, outputType);

		try {
			String fileName;

			switch (outputType) {
			case OUTPUT_TYPE_PDF:
				fileName = "DAC 1.pdf";
				response.setHeader("Content-Disposition", "inline; filename="
						+ fileName);
				response.setContentType("application/pdf");
				break;
			case OUTPUT_TYPE_EXCEL:
				fileName = "DAC 1.xls";
				response.setHeader("Content-Disposition", "inline; filename="
						+ fileName);
				response.setContentType("application/vnd.ms-excel");
				break;
			case OUTPUT_TYPE_HTML:
				fileName = "DAC 1.html";
				response.setHeader("Content-Disposition", "inline; filename="
						+ fileName);
				response.setContentType("text/html");
				break;
			case OUTPUT_TYPE_CSV:
				fileName = "DAC 1.csv";
				response.setHeader("Content-Disposition", "inline; filename="
						+ fileName);
				response.setContentType("text/csv");
				break;
			default:
				break;
			}

			response.setContentLength(baos.size());

			// write to response stream
			this.writeReportToResponseStream(response, baos);
		} catch (Exception e) {
            response.setContentType("text/html");

            try {
                baos.write("No data/Data Invalid".getBytes());
                logger.error("Error creating the DAC1 report", e);
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
            this.writeReportToResponseStream(response, baos);
        }
	}

	/**
	 * Writes the report to the output stream.
	 */
    private void writeReportToResponseStream(HttpServletResponse response, ByteArrayOutputStream baos) {
		logger.debug("Writing report to the stream");
		try {
			// retrieve the output stream
			ServletOutputStream outputStream = response.getOutputStream();
			// write and flush to the output stream
			baos.writeTo(outputStream);
			outputStream.flush();
		} catch (Exception e) {
			logger.error("Unable to write report to the output stream");
		}
	}

}
