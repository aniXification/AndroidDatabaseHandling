/**
 * 
 */
package com.aniXification.databasehadling.dto;

/**
 * @author aniXification Jul 23, 2014 2014 Note.java
 */

public class Note {

	private Long id;
	private String name;
	private String description;
	private String isFav;
	private String type;
	private Long reminderTimestamp;
	private Long timestamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsFav() {
		return isFav;
	}

	public void setIsFav(String isFav) {
		this.isFav = isFav;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getReminderTimestamp() {
		return reminderTimestamp;
	}

	public void setReminderTimestamp(Long reminderTimestamp) {
		this.reminderTimestamp = reminderTimestamp;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

}
