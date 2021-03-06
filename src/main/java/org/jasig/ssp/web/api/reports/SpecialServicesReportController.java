/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.ssp.web.api.reports;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import org.jasig.ssp.factory.PersonTOFactory;
import org.jasig.ssp.model.ObjectStatus;
import org.jasig.ssp.security.permissions.Permission;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.service.PersonService;
import org.jasig.ssp.service.reference.CampusService;
import org.jasig.ssp.service.reference.ServiceReasonService;
import org.jasig.ssp.service.reference.SpecialServiceGroupService;
import org.jasig.ssp.service.reference.StudentTypeService;
import org.jasig.ssp.transferobject.reports.BaseStudentReportTO;
import org.jasig.ssp.transferobject.reports.PersonSearchFormTO;
import org.jasig.ssp.util.sort.PagingWrapper;
import org.jasig.ssp.util.sort.SortingAndPaging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.common.collect.Maps;


/**
 * Service methods for manipulating data about people in the system.
 * <p>
 * Mapped to URI path <code>/1/report/SpecialServices</code>
 */
@Controller
@RequestMapping("/1/report/SpecialServices")
public class SpecialServicesReportController extends ReportBaseController<BaseStudentReportTO> {

	private static final String REPORT_URL = "/reports/specialServicesReport.jasper";
	private static final String REPORT_FILE_TITLE = "Special_Services_Report";
	private static final String REPORT_TITLE = "Special Services Report";
	private static final String DATA_FILE = "SpecialServicesReportingTO.java - Bean Array";
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SpecialServicesReportController.class);

	@Autowired
	private transient PersonService personService;
	@Autowired
	private transient PersonTOFactory personTOFactory;
	@Autowired
	private transient SpecialServiceGroupService ssgService;
	@Autowired
	protected transient ServiceReasonService serviceReasonService;	
	@Autowired
	private transient StudentTypeService studentTypeService;
	@Autowired
	protected transient CampusService campusService;

	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize(Permission.SECURITY_REPORT_READ)
	public @ResponseBody
	void getSpecialServices(
			final HttpServletResponse response,
			final @RequestParam(required = false) ObjectStatus status,
			final @RequestParam(required = false) List<UUID> specialServiceGroupIds,
			final @RequestParam(required = false) List<UUID> studentTypeIds,
			final @RequestParam(required = false) List<UUID> homeCampusIds,
			final @RequestParam(required = false) List<UUID> serviceReasonIds,
			final @RequestParam(required = false, defaultValue = DEFAULT_REPORT_TYPE) String reportType)
			throws ObjectNotFoundException, IOException {

		final Map<String, Object> parameters = Maps.newHashMap();
		final PersonSearchFormTO personSearchForm = new PersonSearchFormTO();
		
		SearchParameters.addReferenceLists(studentTypeIds,
				homeCampusIds,
				specialServiceGroupIds, 
				null,
				serviceReasonIds,
				parameters, 
				personSearchForm, 
				studentTypeService,
				campusService,
				ssgService, 
				null,
				serviceReasonService);

		personSearchForm.setSpecialServiceGroupRequired(true);

        final SortingAndPaging sortingAndPaging = SearchParameters.getReportPersonSortingAndPagingAll(status);
		final PagingWrapper<BaseStudentReportTO> people = personService
				.getStudentReportTOsFromCriteria(personSearchForm, sortingAndPaging);

		List<BaseStudentReportTO> compressedReports = processStudentReportTOs(people,sortingAndPaging);
		LOGGER.debug("Number of personTOs: " + compressedReports.size());

		
		SearchParameters.addReportTitleToMap(REPORT_TITLE, parameters);
		SearchParameters.addDataFIleToMap(DATA_FILE, parameters);
		
		renderReport(response, parameters, compressedReports, REPORT_URL, reportType, REPORT_FILE_TITLE);
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
}
