package fr.free.naoj.svnlab.entity;

import java.text.SimpleDateFormat;
import java.util.List;

import fr.free.naoj.svnlab.utils.StringUtils;

/**
 * <p></p>
 *
 * @author Johann Bernez
 */
public class Commit {

	private static final transient SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
	
	private Integer id;
	
	private String principal;
	private String title;
	private String description;
	
	private List<Modification> modifications;
	private Long revisionNumber;
	
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

	public Long getRevisionNumber() {
		return revisionNumber;
	}

	public void setRevisionNumber(Long revisionNumber) {
		this.revisionNumber = revisionNumber;
	}

	public History getHistory() {
		if (history == null) {
			history = new History();
		}
		return history;
	}

	public void setHistory(History history) {
		this.history = history;
	}
	
	public String getDate() {
		String date = StringUtils.EMPTY_STRING;
		
		if (getHistory().getCreatedAt() != null) {
			date = sdf.format(getHistory().getCreatedAt().getTime());
		}
		
		return date;
	}
}
