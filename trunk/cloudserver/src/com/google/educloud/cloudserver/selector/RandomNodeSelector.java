package com.google.educloud.cloudserver.selector;

import java.util.Collections;

import com.google.educloud.internal.entities.Node;

//Classe para implementar a seleção randomica de nodo.
public class RandomNodeSelector extends AbstractNodeSelector implements INodeSelector {

	@Override
	public Node getNext() {
		
		Collections.shuffle(Nodos);
		return Nodos.get(0);
	}

}
