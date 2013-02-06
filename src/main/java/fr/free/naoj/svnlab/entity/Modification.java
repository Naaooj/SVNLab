package fr.free.naoj.svnlab.entity;

/**
 * <p></p>
 *
 * @author Johann Bernez
 */
public class Modification {

	private Integer id;
	
	private Integer fkCommit;
	
	private String path;
	private Long revisionNumber;
	
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

	public Long getRevisionNumber() {
		return revisionNumber;
	}

	public void setRevisionNumber(Long revisionNumber) {
		this.revisionNumber = revisionNumber;
	}	
}
