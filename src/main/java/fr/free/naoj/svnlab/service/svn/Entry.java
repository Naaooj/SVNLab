package fr.free.naoj.svnlab.service.svn;

import java.util.Date;

public interface Entry {

	public enum Type {
		FILE,
		DIRECTORY;
	}
	
	Type getType();
	
	String getName();
	
	String getAuthor();
	
	Date getDate();
	
	Long getRevision();
}
