package org.jasig.ssp.dao.reference;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.UUID;

import org.jasig.ssp.model.ObjectStatus;
import org.jasig.ssp.model.Person;
import org.jasig.ssp.model.reference.JournalTrackJournalStep;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.service.impl.SecurityServiceInTestEnvironment;
import org.jasig.ssp.util.sort.PagingWrapper;
import org.jasig.ssp.util.sort.SortingAndPaging;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("dao-testConfig.xml")
@TransactionConfiguration(defaultRollback = false)
@Transactional
public class JournalTrackJournalStepDaoTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(JournalTrackJournalStepDaoTest.class);

	@Autowired
	private transient JournalTrackJournalStepDao dao;

	@Autowired
	private transient JournalStepDao journalStepDao;

	@Autowired
	private transient JournalTrackDao journalTrackDao;

	@Autowired
	private transient SecurityServiceInTestEnvironment securityService;

	@Before
	public void setUp() {
		securityService.setCurrent(new Person(Person.SYSTEM_ADMINISTRATOR_ID));
	}

	@Test
	public void testSaveNew() throws ObjectNotFoundException {
		UUID saved;

		JournalTrackJournalStep obj = new JournalTrackJournalStep();
		obj.setJournalStep(journalStepDao.getAll(ObjectStatus.ACTIVE).getRows()
				.iterator().next());
		obj.setJournalTrack(journalTrackDao.get(UUID
				.fromString("5d24743a-a11e-11e1-a9a6-0026b9e7ff4c")));
		obj.setObjectStatus(ObjectStatus.ACTIVE);
		dao.save(obj);

		assertNotNull(obj.getId());
		saved = obj.getId();

		LOGGER.debug(obj.toString());

		obj = dao.get(saved);
		assertNotNull(obj);
		assertNotNull(obj.getId());

		final Collection<JournalTrackJournalStep> all = dao.getAll(
				ObjectStatus.ACTIVE)
				.getRows();
		assertNotNull(all);
		assertTrue(all.size() > 0);
		assertList(all);

		dao.delete(obj);
	}

	@Test(expected = ObjectNotFoundException.class)
	public void testNull() throws ObjectNotFoundException {
		final UUID id = UUID.randomUUID();
		final JournalTrackJournalStep journalStepJournalTrack = dao.get(id);

		assertNull(journalStepJournalTrack);
	}

	private void assertList(final Collection<JournalTrackJournalStep> objects) {
		for (JournalTrackJournalStep object : objects) {
			assertNotNull(object.getId());
		}
	}

	@Test
	public void uuidGeneration() throws ObjectNotFoundException {
		final JournalTrackJournalStep obj = new JournalTrackJournalStep();
		obj.setJournalStep(journalStepDao.getAll(ObjectStatus.ACTIVE).getRows()
				.iterator().next());
		obj.setJournalTrack(journalTrackDao.get(UUID
				.fromString("5d24743a-a11e-11e1-a9a6-0026b9e7ff4c")));
		obj.setObjectStatus(ObjectStatus.ACTIVE);
		dao.save(obj);

		dao.delete(obj);
	}

	@Test
	public void getAllforJournalTrack() {
		final PagingWrapper<JournalTrackJournalStep> wrapper = dao
				.getAllForJournalTrack(
						UUID.randomUUID(), new SortingAndPaging(
								ObjectStatus.ACTIVE));
		assertList(wrapper.getRows());
	}

	@Test
	public void getAllforJournalStepAndJournalTrack() {
		final PagingWrapper<JournalTrackJournalStep> wrapper = dao
				.getAllForJournalTrackAndJournalStep(
						UUID.randomUUID(), UUID.randomUUID(),
						new SortingAndPaging(ObjectStatus.ACTIVE));
		assertList(wrapper.getRows());
	}

}
