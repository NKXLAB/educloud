package com.google.educloud.cloudserver.selector;

import com.google.educloud.internal.entities.Node;

//Classe para implementar o algoritmo de bestfit.
public class BestFitNodeSelector extends AbstractNodeSelector implements INodeSelector {
	
	@Override
	public Node getNext() {
		
		//Para retorno.
		Node nodoSelecionado = null;
		
		for( Node n : Nodos ){
			
			if( nodoSelecionado == null )
				nodoSelecionado = n;
			else if( nodoSelecionado.getMachinesReourcesInfo().getAvaliableMemory() < 
					n.getMachinesReourcesInfo().getAvaliableMemory())
				nodoSelecionado = n;
		}
		
		return nodoSelecionado;
	}	
}
