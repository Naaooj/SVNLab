package fr.free.naoj.svnlab.entity;

import java.util.List;


public class Commit {

	private Integer id;
	
	private String principal;
	private String title;
	private String description;
	
	private List<Modification> modifications;
	private Integer revisionNumber;
	
	private History history;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Modification> getModifications() {
		return modifications;
	}

	public void setModifications(List<Modification> modifications) {
		this.modifications = modifications;
	}

	public Integer getRevisionNumber() {
		return revisionNumber;
	}

	public void setRevisionNumber(Integer revisionNumber) {
		this.revisionNumber = revisionNumber;
	}

	public History getHistory() {
		return history;
	}

	public void setHistory(History history) {
		this.history = history;
	}
}
