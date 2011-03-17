package com.google.educloud.internal.entities;

public class VirtualMachine {

	public enum VMState {PENDING, BOOT, RUNNING, SHUTDOWN, DONE};

	private int id;

	private Template template;

	private int userId;

	private int nodeId;

	private VMState state;

	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
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
	
	public boolean equals(Object obj){
		if( obj == null )
			return false;
		else if( !(obj instanceof VirtualMachine) ) 
			return false;
		else
			return ((VirtualMachine)obj).getId() == this.getId(); 		
	}	
}
