package com.google.educloud.api.entities;

//Classe para representar um nodo da nuvem.
public class Node {
	
	private int id;
	private String ip;
	private int port;
	private String templateDir;
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setHostname(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param templateDir the templateDir to set
	 */
	public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}
	/**
	 * @return the templateDir
	 */
	public String getTemplateDir() {
		return templateDir;
	}
}
