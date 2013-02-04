package fr.free.naoj.svnlab.entity;

public class Modification {

	private Integer id;
	
	private Integer fkCommit;
	
	private String path;
	private Integer revisionNumber;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFkCommit() {
		return fkCommit;
	}

	public void setFkCommit(Integer fkCommit) {
		this.fkCommit = fkCommit;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getRevisionNumber() {
		return revisionNumber;
	}

	public void setRevisionNumber(Integer revisionNumber) {
		this.revisionNumber = revisionNumber;
	}	
}
