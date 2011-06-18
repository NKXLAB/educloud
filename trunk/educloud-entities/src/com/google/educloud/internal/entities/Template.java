package com.google.educloud.internal.entities;


public class Template {

	private int id;

	private String osType;

	private String name;

	private String description;

	private String filename;

	private long memorySize;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
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

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setMemorySize(long memorySize) {
		this.memorySize = memorySize;
	}

	public long getMemorySize() {
		return memorySize;
	}
}
