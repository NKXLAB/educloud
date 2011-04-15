package com.google.educloud.cloudserver.monitor;

import java.util.Calendar;
import java.util.List;
import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.database.dao.NodeDao;
import com.google.educloud.cloudserver.database.dao.VirtualMachineDao;
import com.google.educloud.cloudserver.nodecllient.ClientFactory;
import com.google.educloud.cloudserver.nodecllient.NodeClient;
import com.google.educloud.cloudserver.nodecllient.NodeComunicationException;
import com.google.educloud.cloudserver.selector.NodeSelectorManager;
import com.google.educloud.internal.entities.Node;
import com.google.educloud.internal.entities.VirtualMachine;

public class NodeMonitor implements Runnable{
	
	private static Logger LOG = Logger.getLogger(NodeMonitor.class);
	
	@Override
	public void run() {
		
		while(true){
			
			//Recupera os nodos cadastrados na base de dados.
			List<Node> nodes =  NodeDao.getInstance().getAll();
			
			// create webservice client
			NodeClient nodeClient = null;
			
			for( Node node : nodes ){
				
				LOG.debug("cloud server will try contact node #" + node.getId() + " on " + node.getHostname());
				
				// Create webservice client
				nodeClient = ClientFactory.createNodeClient(node);

				// Call node service
				try {
					
					LOG.debug("will call node #"+ node.getId()+ ", hostname '" +node.getHostname()+ ':' +node.getPort()+"'");
					node = nodeClient.checkNodeStatus(node);
					
					//Atualiza o nodo no seletor.
					NodeSelectorManager.getSelector().updateNode(node);
					
					//Update last ping if node was successful return
					node.setLastPing(Calendar.getInstance().getTime());
					NodeDao.getInstance().updateLastPing(node);
					
				} catch (NodeComunicationException e) {
					
					LOG.error("Error on try contact node #"+ node.getId()+". server will unregister the node.");
					
					NodeSelectorManager.getSelector().unregisterNode(node);				
					
					//Retira o nodo de todas as maquinas
					VirtualMachineDao.getInstance().clearNode(node.getId());
					
					//Remove o nodo da base de dados.
					NodeDao.getInstance().remove(node);
					
					break;
				}					
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
}
