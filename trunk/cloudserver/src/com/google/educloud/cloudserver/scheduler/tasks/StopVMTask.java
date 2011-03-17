package com.google.educloud.cloudserver.scheduler.tasks;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.database.dao.NodeDao;
import com.google.educloud.cloudserver.database.dao.VirtualMachineDao;
import com.google.educloud.cloudserver.nodecllient.ClientFactory;
import com.google.educloud.cloudserver.nodecllient.NodeComunicationException;
import com.google.educloud.cloudserver.nodecllient.VMNodeClient;
import com.google.educloud.internal.entities.Node;
import com.google.educloud.internal.entities.VirtualMachine;

//Classe para representar uma tarefa de parada de máquina virtual.
public class StopVMTask extends AbstractTask{
	
	public static final String VM_ID = "VM_ID";
	
	private static Logger LOG = Logger.getLogger(StopVMTask.class);

	@Override
	public void run() {
		
		markAsRunning();

		// 1) load virtual machine from database
		String vmId = getParameter(VM_ID);
		VirtualMachine vm = VirtualMachineDao.getInstance().findById(Integer.parseInt(vmId));
		
		// 2) select the node of respective VM
		Node node = NodeDao.getInstance().findNodeById(vm.getNodeId());
		LOG.debug("Selected node of the virtual machine: #" + node.getId());

		// 3) send requisition for host
		VMNodeClient createVMNodeClient = ClientFactory.createVMNodeClient(node);
		try {
			createVMNodeClient.stopVM(vm);
		} catch (NodeComunicationException e) {
			LOG.error("An error when stop virtual machine: #" + vm.getId(), e);
		}

		markAsCompleted();		
	}
}