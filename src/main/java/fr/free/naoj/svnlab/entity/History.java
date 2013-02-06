package fr.free.naoj.svnlab.entity;

import java.util.Calendar;

/**
 * <p></p>
 *
 * @author Johann Bernez
 */
public class History {

	private String createdBy;
	private String updatedBy;
	private Calendar createdAt;
	private Calendar updatedAt;
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

	public Calendar getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Calendar updatedAt) {
		this.updatedAt = updatedAt;
	}
}
