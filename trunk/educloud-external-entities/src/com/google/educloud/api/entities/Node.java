package com.google.educloud.api.entities;

/**
 * Classe para representar um nodo da nuvem.
 *
 */
public class Node {

	private int id;

	private String hostname;

	private int port;

	private boolean connectedToVBox;

	private String vboxVersion;

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
		this.hostname = ip;
	}
	/**
	 * @return the ip
	 */
	public String getHostName() {
		return hostname;
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
	public boolean isConnectedToVBox() {
		return connectedToVBox;
	}
	public void setConnectedToVBox(boolean connectedToVBox) {
		this.connectedToVBox = connectedToVBox;
	}
	public String getVboxVersion() {
		return vboxVersion;
	}
	public void setVboxVersion(String vboxVersion) {
		this.vboxVersion = vboxVersion;
	}
}
