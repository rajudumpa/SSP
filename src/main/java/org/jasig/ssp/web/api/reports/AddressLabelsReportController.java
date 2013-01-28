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
package org.jasig.ssp.web.api.reports; // NOPMD

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang.StringUtils;
import org.jasig.ssp.factory.PersonTOFactory;
import org.jasig.ssp.model.ObjectStatus;
import org.jasig.ssp.model.Person;
import org.jasig.ssp.security.permissions.Permission;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.service.PersonService;
import org.jasig.ssp.service.external.TermService;
import org.jasig.ssp.service.reference.ProgramStatusService;
import org.jasig.ssp.service.reference.ReferralSourceService;
import org.jasig.ssp.service.reference.SpecialServiceGroupService;
import org.jasig.ssp.service.reference.StudentTypeService;
import org.jasig.ssp.transferobject.PersonTO;
import org.jasig.ssp.transferobject.reports.AddressLabelSearchTO;
import org.jasig.ssp.util.DateTerm;
import org.jasig.ssp.util.sort.SortingAndPaging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;

/**
 * Service methods for manipulating data about people in the system.
 * <p>
 * Mapped to URI path <code>report/AddressLabels</code>
 */
@Controller
@RequestMapping("/1/report/AddressLabels")
public class AddressLabelsReportController extends ReportBaseController { // NOPMD

	private static String REPORT_URL = "/reports/addressLabels.jasper";
	private static String REPORT_FILE_TITLE = "General_Student_Report";
	private static String STUDENT_COUNT = "studentCount";
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AddressLabelsReportController.class);

	@Autowired
	private transient PersonService personService;
	@Autowired
	private transient PersonTOFactory personTOFactory;
	@Autowired
	private transient SpecialServiceGroupService ssgService;
	@Autowired
	private transient ReferralSourceService referralSourcesService;
	@Autowired
	private transient TermService termService;
	@Autowired
	private transient ProgramStatusService programStatusService;
	@Autowired
	protected transient StudentTypeService studentTypeService;		

	// @Autowired
	// private transient PersonTOFactory factory;

	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT,
				Locale.US);
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize(Permission.SECURITY_REPORT_READ)
	@ResponseBody
	public void getAddressLabels(
			final HttpServletResponse response,
			final @RequestParam(required = false) ObjectStatus status,
			final @RequestParam(required = false) UUID coachId,			
			final @RequestParam(required = false) UUID programStatus,
			final @RequestParam(required = false) List<UUID> specialServiceGroupIds,
			final @RequestParam(required = false) List<UUID> referralSourcesIds,
			final @RequestParam(required = false) List<UUID> studentTypeIds,
			final @RequestParam(required = false) Integer anticipatedStartYear,
			final @RequestParam(required = false) String anticipatedStartTerm,
			final @RequestParam(required = false) Date createDateFrom,
			final @RequestParam(required = false) Date createDateTo,
			final @RequestParam(required = false) String termCode,
			final @RequestParam(required = false, defaultValue = DEFAULT_REPORT_TYPE) String reportType)
			throws ObjectNotFoundException, JRException, IOException {
		
		final Map<String, Object> parameters = Maps.newHashMap();
		
		DateTerm dateTerm = new DateTerm(createDateFrom, createDateTo, termCode, termService);
		PersonTO coach = SearchParameters.getPerson(coachId, personService, personTOFactory);
		SearchParameters.addCoachNameToMap(coach, parameters);
		
		List<UUID> cleanSpecialServiceGroupIds = SearchParameters.cleanUUIDListOfNulls(specialServiceGroupIds);
		List<UUID> cleanStudentTypeIds = SearchParameters.cleanUUIDListOfNulls(studentTypeIds);
		List<UUID> cleanReferralSourcesIds = SearchParameters.cleanUUIDListOfNulls(referralSourcesIds);

		final AddressLabelSearchTO searchForm = new AddressLabelSearchTO(
				coach,
				programStatus, cleanSpecialServiceGroupIds, cleanReferralSourcesIds,
				StringUtils.trimToNull(anticipatedStartTerm),
				anticipatedStartYear, cleanStudentTypeIds, dateTerm.getStartDate(),
				dateTerm.getEndDate());

		// TODO Specifying person name sort fields in the SaP doesn't seem to
		// work... end up with empty results need to dig into actual query
		// building
		final List<Person> people = personService.peopleFromCriteria(
				searchForm, SortingAndPaging.createForSingleSort(status, null,
						null, null, null, null));
		Collections.sort(people, Person.PERSON_NAME_AND_ID_COMPARATOR);
		final List<PersonTO> peopleTO = personTOFactory.asTOList(people);

		SearchParameters.addSpecialGroupsNamesToMap(cleanSpecialServiceGroupIds, parameters, ssgService);
		SearchParameters.addStudentTypesToMap(cleanStudentTypeIds, parameters, studentTypeService);
		SearchParameters.addReferralSourcesToMap(cleanReferralSourcesIds, parameters, referralSourcesService);
		SearchParameters.addProgramStatusToMap(programStatus, parameters, programStatusService);
		SearchParameters.addDateTermToMap(dateTerm, parameters);
		SearchParameters.addReportDateToMap(parameters);
		SearchParameters.addCohortTerm(anticipatedStartTerm, anticipatedStartYear, parameters);
		
		parameters.put(STUDENT_COUNT, peopleTO == null ? 0 : peopleTO.size());

		
		generateReport(response, parameters, peopleTO, REPORT_URL, reportType, REPORT_FILE_TITLE);

	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
}