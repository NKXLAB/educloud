package com.google.educloud.cloudserver.selector;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.educloud.internal.entities.Node;

/**
 * Classe para implementar o algoritmo de bestfit.
 *
 */
public class BestFitNodeSelector implements INodeSelector {

	private static Logger LOG = Logger.getLogger(BestFitNodeSelector.class);

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
			LOG.debug("Register node: " + node.getHostname() + ':' + node.getPort() + " from " + this.getClass().getName());
			// Adiciona o novo nodo.
			nodes.add(node);
		}
	}

	@Override
	public void unregisterNode(Node node) {
		if (nodes.contains(node)) {
			LOG.debug("Unregister node: " + node.getHostname() + ':' + node.getPort() + " from " + this.getClass().getName());
			// Remove o nodo.
			nodes.remove(node);
		}
	}

	@Override
	public List<Node> getRegisteredNodes() {
		return nodes;
	}

	@Override
	public Node getNext() {

		// Para retorno.
		Node nodoSelecionado = null;

		for (Node n : nodes) {
			if (nodoSelecionado == null)
				nodoSelecionado = n;
			else if (nodoSelecionado.getMachinesReourcesInfo()
					.getAvaliableMemory() < n.getMachinesReourcesInfo()
					.getAvaliableMemory())
				nodoSelecionado = n;
		}

		if (null != nodoSelecionado) {
			LOG.debug("returning node #" + nodoSelecionado.getId());
		}

		return nodoSelecionado;
	}

}
