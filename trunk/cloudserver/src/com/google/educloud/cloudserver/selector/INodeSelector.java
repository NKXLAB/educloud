package com.google.educloud.cloudserver.selector;

import com.google.educloud.internal.entities.Node;

/**
 * Interface para padronizar o gerenciador de nodos.
 *
 */
public interface INodeSelector {

	// Registra o nodo no manager.
	public void updateNode(Node node);

	// Registra o nodo no manager.
	public void registerNode(Node node);

	// Desregistra o nodo do manager.
	public void unregisterNode(Node node);

	/**
	 * Will return the next node to allocate a virtual machine
	 *
	 * @return null if no node is available
	 */
	public Node getNext();

}
