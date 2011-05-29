package com.google.educloud.cloudserver.monitor;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.database.dao.VirtualMachineDao;
import com.google.educloud.cloudserver.nodecllient.ClientFactory;
import com.google.educloud.cloudserver.nodecllient.NodeClient;
import com.google.educloud.cloudserver.nodecllient.NodeComunicationException;
import com.google.educloud.internal.entities.Node;
import com.google.educloud.internal.entities.VirtualMachine;
import com.google.educloud.internal.entities.VirtualMachine.VMState;

public class VMMonitor implements Runnable {

	private static Logger LOG = Logger.getLogger(VMMonitor.class);

	@Override
	public void run() {

		while(true){

			// create webservice client
			NodeClient nodeClient = null;

			//Recupera todas as maquinas virtuais.
			List<VirtualMachine> machines = VirtualMachineDao.getInstance().getAll();

			for ( VirtualMachine vm : machines ) {

				int nodeId = vm.getNodeId();
				Node node = new Node();
				node.setId(nodeId);

				//So vai verificar as maquinas que possuem nodos.
				if (nodeId > 0 && vm.getUUID() != null) {

					LOG.debug("cloud server will try contact machine #" + vm.getId() + " on node: " + nodeId);

					// 	Create webservice client
					nodeClient = ClientFactory.createNodeClient(node);

					// Call node service
					try {
						LOG.debug("will call machine #"+ vm.getId()+ " name: '" + vm.getName());
						vm = nodeClient.checkVMStatus(vm);
					} catch (NodeComunicationException e) {
						LOG.error("Error on try contact node #"+ node.getId()+". server will change state of VM to 'UNKNOWN'.");

						//Coloca a maquina no estado UNKNOWN.
						vm.setState(VMState.UNKNOWN);
					}

					//Atualiza o estado da maquina virtual.
					VirtualMachineDao.getInstance().changeState(vm);

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// 	TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
