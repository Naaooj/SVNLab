package fr.free.naoj.svnlab.service;

import java.util.List;

import fr.free.naoj.svnlab.entity.Commit;
import fr.free.naoj.svnlab.entity.SearchCriteria;

/**
 * <p></p>
 *
 * @author Johann Bernez
 */
public interface SearchEngine {

	void index(Commit commit);
	
	List<Commit> search(SearchCriteria criterias);
}
