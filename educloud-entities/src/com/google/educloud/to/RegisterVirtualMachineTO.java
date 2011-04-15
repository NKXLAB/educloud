package com.google.educloud.to;

import com.google.educloud.internal.entities.Node;
import com.google.educloud.internal.entities.VirtualMachine;

/* Classe para conter o nodo e as maquinas que estao sendo registradas */
public class RegisterVirtualMachineTO {
	
	private VirtualMachine[] machines;
	private Node nodo;
	
	public VirtualMachine[] getMachines() {
		return machines;
	}
	public void setMachines(VirtualMachine[] machines) {
		this.machines = machines;
	}
	public Node getNodo() {
		return nodo;
	}
	public void setNodo(Node nodo) {
		this.nodo = nodo;
	}
}
