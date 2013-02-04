package fr.free.naoj.svnlab.service.svn.impl;

import java.util.Date;

import fr.free.naoj.svnlab.service.svn.Entry;

/**
 * <p></p>
 *
 * @author Johann Bernez
 */
public abstract class AbstractEntry implements Entry {

	private String name;
	private String author;
	private Date date;
	private Long revision;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getRevision() {
		return revision;
	}

	public void setRevision(Long revision) {
		this.revision = revision;
	}
}
