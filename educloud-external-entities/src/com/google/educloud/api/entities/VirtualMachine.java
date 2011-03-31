package com.google.educloud.api.entities;


public class VirtualMachine {

	public enum VMState {PENDING, BOOT, RUNNING, SHUTDOWN, DONE, UNKNOWN};

	private int id;

	private int userId;

	private Template template;

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
}
