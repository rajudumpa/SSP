/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.ssp.web.api.reports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import org.jasig.ssp.web.api.AbstractBaseController;
import org.slf4j.Logger;

abstract class ReportBaseController extends AbstractBaseController {

	protected static final String DEFAULT_REPORT_TYPE = "pdf";
	protected static final String REPORT_TYPE_PDF = "pdf";
	protected static final String REPORT_TYPE_CSV = "csv";
	
	protected static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";
	
	@Override
	protected Logger getLogger() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected void generateReport(HttpServletResponse response, Map<String, Object> parameters, 
			Collection<?> beanCollection, String url, String reportType, String reportName) throws JRException, IOException{
		
		final InputStream is = getClass().getResourceAsStream(url);
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		
		JRDataSource beanDS;
		if (beanCollection == null || beanCollection.size() <= 0) {
			beanDS = new JREmptyDataSource();
		} else {
			beanDS = new JRBeanCollectionDataSource(beanCollection);
		}
		
		JasperFillManager.fillReportToStream(is, os, parameters, beanDS);
		final InputStream decodedInput = new ByteArrayInputStream(
				os.toByteArray());

		if (REPORT_TYPE_PDF.equals(reportType)) {
			response.setHeader(
					"Content-disposition",
					"attachment; filename=" + reportName + "." + REPORT_TYPE_PDF);
			JasperExportManager.exportReportToPdfStream(decodedInput,
					response.getOutputStream());
		} else if ("csv".equals(reportType)) {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader(
					"Content-disposition",
					"attachment; filename=" + reportName + "." + REPORT_TYPE_CSV);

			final JRCsvExporter exporter = new JRCsvExporter();
			exporter.setParameter(JRExporterParameter.INPUT_STREAM,
					decodedInput);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
					response.getOutputStream());
			exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET,
					Boolean.FALSE);

			exporter.exportReport();
		}
		
		response.flushBuffer();
		is.close();
		os.close();		
	}

}
