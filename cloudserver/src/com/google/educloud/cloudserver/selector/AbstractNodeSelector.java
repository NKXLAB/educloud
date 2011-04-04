package com.google.educloud.cloudserver.selector;

import java.util.ArrayList;
import java.util.Random;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.configuration.ServerConfig;
import com.google.educloud.cloudserver.rs.NodeRest;
import com.google.educloud.internal.entities.Node;
import com.sun.xml.internal.fastinfoset.sax.Properties;

//Classe para conter os metodos compartilhados entre os gerenciadores de nodos.
public abstract class AbstractNodeSelector implements INodeSelector{
	
	private static Logger LOG = Logger.getLogger(AbstractNodeSelector.class);
	
	private static INodeSelector nodeSelector;
	
	//Para armazenar os nodos.
	protected ArrayList<Node> Nodos = new ArrayList<Node>();

	@Override
	public void registerNode(Node node) {
		if( !Nodos.contains(node) ){
			//Adiciona o novo nodo.
			Nodos.add(node);
		}
		else{
			//Atualiza o nodo.
			Nodos.set(Nodos.indexOf(node) , node);
		}
	}

	@Override
	public void unregisterNode(Node node) {
		if( Nodos.contains(node) ){
			//Remove o nodo.
			Nodos.remove(node);
		}
	}
	
	//Cria a instancia do seletor de nodos de acordo com a configuração do server.
	public static INodeSelector getInstance() {
		
		if( nodeSelector == null ){
		
			if( ServerConfig.getPolice().equalsIgnoreCase("bestfit") ){
				LOG.debug("Creating a BEST-FIT class to control nodes.");
				nodeSelector = new BestFitNodeSelector();
			}
			else if( ServerConfig.getPolice().equalsIgnoreCase("worstfit") ){
				LOG.debug("Creating a WORST-FIT class to control nodes.");
				nodeSelector = new WorstFitNodeSelector();
			}
			else{
				LOG.debug("Creating a RandomNodeSelector class to control nodes.");
				nodeSelector = new RandomNodeSelector();
			}
			
		}		
		return nodeSelector;		
		
	}
}
