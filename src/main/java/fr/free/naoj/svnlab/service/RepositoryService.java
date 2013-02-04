package fr.free.naoj.svnlab.service;

import java.util.List;

import fr.free.naoj.svnlab.service.svn.Entry;

public interface RepositoryService {

	List<Entry> getProjects();
	
	List<Entry> getEntries(String path);
}
