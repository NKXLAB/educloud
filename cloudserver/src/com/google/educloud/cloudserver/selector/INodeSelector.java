package com.google.educloud.cloudserver.selector;

import com.google.educloud.internal.entities.Node;

//Interface para padronizar o gerenciador de nodos.
public interface INodeSelector {
	
	//Registra o nodo no manager.
	public void registerNode(Node node);
	
	//Desregistra o nodo do manager.
	public void unregisterNode(Node node);
	
	//Recupera o próximo nodo de acordo com algoritmo implementado.
	public Node getNext();

}
