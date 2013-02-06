package fr.free.naoj.svnlab.service;

import java.util.Collections;
import java.util.List;

import fr.free.naoj.svnlab.entity.Commit;

/**
 * <p></p>
 *
 * @author Johann Bernez
 */
public class CommitsDetailsHolder {

	private boolean moreCommits = false;
	private final List<Commit> commits;
	
	public CommitsDetailsHolder(List<Commit> commits) {
		if (commits == null) {
			this.commits = Collections.<Commit> emptyList();
		} else {
			this.commits = commits;
		}
	}

	public boolean isMoreCommits() {
		return moreCommits;
	}

	public void setMoreCommits(boolean moreCommits) {
		this.moreCommits = moreCommits;
	}

	public List<Commit> getCommits() {
		return commits;
	}
}
