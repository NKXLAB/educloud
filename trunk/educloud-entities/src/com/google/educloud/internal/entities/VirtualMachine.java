package com.google.educloud.internal.entities;

public class VirtualMachine {

	public enum VMState {PENDING, BOOT, RUNNING, SHUTDOWN, DONE, UNKNOWN};

	private int id;

	private int userId;

	private int nodeId;

	private VMState state;

	private String name;

	private String bootableMedium;

	private String uuid;

	private String vbox;

	private String osType;

	private String description;

	private String vrdePassword;

	private String vrdeUsername;

	private int vrdePort;

	private long memorySize;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
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

	public String getBootableMedium() {
		return bootableMedium;
	}

	public void setBootableMedium(String bootableMedium) {
		this.bootableMedium = bootableMedium;
	}

	public String getUUID() {
		return uuid;
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
	}

	public String getVboxSession() {
		return vbox;
	}

	public void setVboxSession(String vbox) {
		this.vbox = vbox;
	}

	public boolean equals(Object obj){
		if( obj == null )
			return false;
		else if( !(obj instanceof VirtualMachine) )
			return false;
		else
			return ((VirtualMachine)obj).getId() == this.getId();
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

	public String getVRDEPassword() {
		return vrdePassword;
	}

	public void setVRDEPassword(String vrdePassword) {
		this.vrdePassword = vrdePassword;
	}

	public String getVRDEUsername() {
		return vrdeUsername;
	}

	public void setVRDEUsername(String vrdeUsername) {
		this.vrdeUsername = vrdeUsername;
	}

	public void setVRDEPort(int port) {
		this.vrdePort = port;
	}

	public int getVRDEPort() {
		return vrdePort;
	}

	public void setMemorySize(long memorySize) {
		this.memorySize= memorySize;
	}

	public long getMemorySize() {
		return memorySize;
	}

}
