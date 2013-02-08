package fr.free.naoj.svnlab.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import fr.free.naoj.svnlab.utils.StringUtils;

/**
 * <p></p>
 *
 * @author Johann Bernez
 */
@Entity
@Table(name="svncommit")
public class Commit implements Serializable {

	/**  */
	private static final long serialVersionUID = 7736065852790177590L;

	private static final transient SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
	
	@Id
	@Column(name="id")
	@GeneratedValue
	private Long id;
	
	@Column(name="principal")
	private String principal;
	
	@Column(name="title")
	private String title;
	
	@Column(name="descritption")
	private String description;
	
//	@OneToMany(mappedBy="list", targetEntity=Modification.class, fetch=FetchType.LAZY)
	transient private List<Modification> modifications;
	
	@Column(name="revision")
	private Long revisionNumber;
	
	@Embedded
	private History history;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
