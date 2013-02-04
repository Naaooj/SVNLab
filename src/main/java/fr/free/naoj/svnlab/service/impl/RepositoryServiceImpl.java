package fr.free.naoj.svnlab.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import fr.free.naoj.svnlab.service.RepositoryService;
import fr.free.naoj.svnlab.service.UserService;
import fr.free.naoj.svnlab.service.svn.Entry;
import fr.free.naoj.svnlab.service.svn.impl.AbstractEntry;
import fr.free.naoj.svnlab.service.svn.impl.DirectoryImpl;
import fr.free.naoj.svnlab.service.svn.impl.FileImpl;

@Service
public class RepositoryServiceImpl implements RepositoryService {
	
	private SVNRepository repository;
	
	@Value(value="${svn.repository}")
	private String url;
	
	@Autowired
	private UserService userService;
	
	private Log log = LogFactory.getLog(getClass());
		
	public List<Entry> getProjects() {
		List<Entry> projects = null;
		
		projects = getEntries("/");
		
		if (projects == null) {
			projects = Collections.<Entry> emptyList();
		}
		
		return projects;
	}
	
	public List<Entry> getEntries(String path) {
		List<Entry> entries = null;
		
		try {
			SVNNodeKind nodeKind = getRepository().checkPath(path, -1);
			
			if (nodeKind == SVNNodeKind.DIR) {
				@SuppressWarnings("unchecked")
				Collection<SVNDirEntry> directories = getRepository().getDir(path, -1, null, (Collection<?>) null);
				entries = new ArrayList<Entry>(directories.size());
				
				for (SVNDirEntry directory : directories) {
					if (SVNNodeKind.DIR == directory.getKind()) {
						entries.add(convertDirectory(directory, new DirectoryImpl()));
					}
					if (SVNNodeKind.FILE == directory.getKind()) {
						entries.add(convertDirectory(directory, new FileImpl()));
					}
				}
				
			}
		} catch (SVNException e) {
			log.error("Unable to list svn projects", e);
		}
		
		if (entries == null) {
			entries = Collections.<Entry> emptyList();
		}
		
		return entries;
	}
	
	private <E extends AbstractEntry> E convertDirectory(SVNDirEntry directory, E entry) {
		entry.setAuthor(directory.getAuthor());
		entry.setDate(directory.getDate());
		entry.setName(directory.getName());
		entry.setRevision(new Long(directory.getRevision()));
		
		return entry;
	}

	private void checkAuthAndConnectIfNecessary() throws SVNException {
		if (!isAuthenticated()) {
			initializeRepository();
			
			authenticate();
		}
	}
	
	private SVNRepository getRepository() throws SVNException {
		checkAuthAndConnectIfNecessary();
		
		return repository;
	}
	
	private boolean isAuthenticated() {
		return repository != null && repository.getAuthenticationManager() != null;
	}
	
	private void authenticate() throws SVNException {
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userService.getUser().getUsername(), userService.getUser().getPassword());
		
		repository.setAuthenticationManager(authManager);
	}
	
	private void initializeRepository() {
		DAVRepositoryFactory.setup();
		SVNRepositoryFactoryImpl.setup();
		FSRepositoryFactory.setup();
		
		try {
			repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
		} catch (SVNException e) {
			log.error("Unable to create repository for the given url [" + url + "]", e);
		}
	}
}
