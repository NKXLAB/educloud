package com.google.educloud.cloudserver.monitor;

import java.util.Calendar;
import java.util.List;
import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.database.dao.NodeDao;
import com.google.educloud.cloudserver.nodecllient.ClientFactory;
import com.google.educloud.cloudserver.nodecllient.NodeClient;
import com.google.educloud.cloudserver.nodecllient.NodeComunicationException;
import com.google.educloud.cloudserver.selector.NodeSelectorManager;
import com.google.educloud.internal.entities.Node;

public class NodeMonitor implements Runnable{
	
	private static Logger LOG = Logger.getLogger(NodeMonitor.class);
	
	@Override
	public void run() {
		
		while(true){
			
			//Recupera os nodos registrados.
			List<Node> nodes =  NodeSelectorManager.getSelector().getRegisteredNodes();
			
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
					NodeSelectorManager.getSelector().updateNode(node);
				} catch (NodeComunicationException e) {
					NodeSelectorManager.getSelector().unregisterNode(node);
					LOG.error("Error on try contact node #"+ node.getId()+". server will unregister the node.");
					break;
				}

				// Update last ping if node was successful return
				node.setLastPing(Calendar.getInstance().getTime());
				NodeDao.getInstance().updateLastPing(node);	
				
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
