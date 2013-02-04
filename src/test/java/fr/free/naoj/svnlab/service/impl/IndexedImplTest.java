package fr.free.naoj.svnlab.service.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.free.naoj.svnlab.entity.Commit;
import fr.free.naoj.svnlab.entity.SearchCriteria;
import fr.free.naoj.svnlab.service.SearchEngine;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/test-context.xml"})
public class IndexedImplTest {

	@Autowired
	@Qualifier("searchEngine")
	private SearchEngine engine;
	
	private Commit commit;
	
	@Before
	public void setUp() {
		commit = new Commit();
		commit.setId(1);
		commit.setTitle("Implementing simple test");
		commit.setDescription("this is some content to be indexed by lucene for this test case");
	}
	
	@Test
	public void testIndex() {
		try {
			engine.index(commit);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSearch() {
		try {
			SearchCriteria criterias = new SearchCriteria();
			criterias.setDescription("lucene");
			
			List<Commit> commits = engine.search(criterias);
			assertEquals(1, commits.size());
			assertEquals(commits.get(0).getTitle(), commit.getTitle());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
