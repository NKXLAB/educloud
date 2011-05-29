package com.google.educloud.cloudserver.selector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.educloud.internal.entities.Node;

//Classe para implementar a selecao randomica de nodo.
public class RandomNodeSelector implements INodeSelector {

	private List<Node> nodes = new ArrayList<Node>();

	@Override
	public void updateNode(Node node) {
		if (nodes.contains(node)) {
			nodes.set(nodes.indexOf(node), node);
		}
	}

	@Override
	public void registerNode(Node node) {
		if (!nodes.contains(node)) {
			// Adiciona o novo nodo.
			nodes.add(node);
		}
	}

	@Override
	public void unregisterNode(Node node) {
		if (nodes.contains(node)) {
			// Remove o nodo.
			nodes.remove(node);
		}
	}

	@Override
	public Node getNext() {
		Collections.shuffle(nodes);
		return nodes.get(0);
	}

	@Override
	public List<Node> getRegisteredNodes() {
		// TODO Auto-generated method stub
		return null;
	}

}
