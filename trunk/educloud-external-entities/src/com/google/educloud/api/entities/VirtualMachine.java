package com.google.educloud.api.entities;


public class VirtualMachine {

	public enum VMState {PENDING, BOOT, RUNNING, SHUTDOWN, DONE, UNKNOWN};

	private int id;

	private int userId;

	private VMState state;

	private String name;

	private String osType;

	private String description;

	private RDPConfig rdpConfig;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public VMState getState() {
		return state;
	}

	public void setState(VMState state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getUserId(){
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public boolean isStartable() {
		return state == VMState.DONE;
	}

	public boolean isStoppable() {
		return state == VMState.RUNNING;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public RDPConfig getRDPConfig() {
		return rdpConfig;
	}

	public void setRDPConfig(RDPConfig rdpConfig) {
		this.rdpConfig = rdpConfig;
	}
}
