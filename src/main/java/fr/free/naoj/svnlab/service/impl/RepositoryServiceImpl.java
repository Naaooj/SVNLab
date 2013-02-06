package fr.free.naoj.svnlab.service.impl;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import fr.free.naoj.svnlab.entity.Commit;
import fr.free.naoj.svnlab.service.CommitsDetailsHolder;
import fr.free.naoj.svnlab.service.RepositoryService;
import fr.free.naoj.svnlab.service.UserService;
import fr.free.naoj.svnlab.service.svn.Entry;
import fr.free.naoj.svnlab.service.svn.impl.AbstractEntry;
import fr.free.naoj.svnlab.service.svn.impl.DirectoryImpl;
import fr.free.naoj.svnlab.service.svn.impl.FileImpl;

@Service
public class RepositoryServiceImpl implements RepositoryService {
	
	/** You should call {@link #getRepository()} and not access this variable directly*/
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
			
				Collections.sort(entries, new EntryNameComparator());
			}
		} catch (SVNException e) {
			log.error("Unable to list svn projects", e);
		}
		
		if (entries == null) {
			entries = Collections.<Entry> emptyList();
		}
		
		return entries;
	}
	
	@Override
	public CommitsDetailsHolder getCommits(String path, int limit) {
		CommitsDetailsHolder commitHolder = null;
		
		long startRevision = 0;

		// Avoid strange behaviour
		if (limit <= 0) {
			limit = 10;
		}
		int step = limit + 100;
		
		try {
			long endRevision = getRepository().getLatestRevision();
			startRevision = endRevision - step;
			
			final List<Commit> logs = new ArrayList<Commit>();
			final CommitsDetailsHolder holder = new CommitsDetailsHolder(logs);
			final LimitValidator validator = new LimitValidator();
			validator.limit = limit;
			validator.list = new WeakReference<List<?>>(logs);
			
			do {
				getRepository().log(new String[]{path}, startRevision, endRevision, true, true, -1, new ISVNLogEntryHandler() {
					@Override
					public void handleLogEntry(SVNLogEntry logEntry) throws SVNException {
						if (!validator.isLimitReached()) {
							logs.add(convertSVNLogEntry(logEntry));
						} else {
							holder.setMoreCommits(true);
							validator.noMoreResult = true;
						}
					}
				});
				
				// Less result that expected, inform the validator
				if (startRevision > 0 && logs.size() < limit) {
					endRevision -= step;
					startRevision -= step;
				}
			} while (!validator.isLimitReached());
			
			Collections.sort(logs, new DescendingCommitComparator());
			
			commitHolder = holder;
		} catch (Throwable t) {
			log.error("Unable to list the logs for path [" + path + "]", t);
		}
		
		if (commitHolder == null) {
			commitHolder = new CommitsDetailsHolder(null);
		}
		
		return commitHolder;
	}

	private <E extends AbstractEntry> E convertDirectory(SVNDirEntry directory, E entry) {
		entry.setAuthor(directory.getAuthor());
		entry.setDate(directory.getDate());
		entry.setName(directory.getName());
		entry.setRevision(new Long(directory.getRevision()));
		
		return entry;
	}
	
	private Commit convertSVNLogEntry(SVNLogEntry entry) {
		Commit c = new Commit();
		
		if (entry != null) {
			c.setPrincipal(entry.getAuthor());
			c.setTitle(entry.getMessage());
			Calendar commitedAd = Calendar.getInstance();
			commitedAd.setTime(entry.getDate());
			c.getHistory().setCreatedAt(commitedAd);
			c.setRevisionNumber(entry.getRevision());
		}
		
		return c;
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
	
	private class LimitValidator {
		
		public int limit = 0;
		public boolean noMoreResult = false;
		public WeakReference<List<?>> list;
		
		public boolean isLimitReached() {
			return noMoreResult || limit <= list.get().size();
		}
	}
	
	private class EntryNameComparator implements Comparator<Entry> {
		@Override
		public int compare(Entry e1, Entry e2) {
			int result = 0;
			
			String name1 = e1.getName();
			String name2 = e2.getName();
			
			if (name1 != null && name2 != null) {
				result = name1.compareTo(name2);
			} else if (name1 == null && name2 != null) {
				result = 1;
			} else if (name1 != null && name2 == null) {
				result = -1;
			}
			
			return result;
		}
	}
	
	private class DescendingCommitComparator implements Comparator<Commit> {
		@Override
		public int compare(Commit o1, Commit o2) {
			Calendar c1 = o1.getHistory().getCreatedAt();
			Calendar c2 = o2.getHistory().getCreatedAt();
			int result = 0;
			
			if (c1 != null && c2 != null) {
				result = -c1.compareTo(c2);
			} else if (c1 == null && c2 != null) {
				result = -1;
			} else if (c1 != null && c2 == null) {
				result = 1;
			}
			
			return result;
		}
	}
}
