package com.google.educloud.cloudserver.scheduler.tasks;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.database.dao.NodeDao;
import com.google.educloud.cloudserver.database.dao.VirtualMachineDao;
import com.google.educloud.cloudserver.nodecllient.ClientFactory;
import com.google.educloud.cloudserver.nodecllient.NodeComunicationException;
import com.google.educloud.cloudserver.nodecllient.VMNodeClient;
import com.google.educloud.internal.entities.Node;
import com.google.educloud.internal.entities.VirtualMachine;
import com.google.educloud.internal.entities.VirtualMachine.VMState;

/**
 * Classe para representar uma tarefa de parada de maquina virtual.
 *
 */
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
		VMNodeClient nodeClient = ClientFactory.createVMNodeClient(node);
		try {
			nodeClient.stopVM(vm);
		} catch (NodeComunicationException e) {
			LOG.error("An error when stop virtual machine: #" + vm.getId(), e);
		}

		vm.setState(VMState.DONE);
		vm.setVRDEPassword(null);
		vm.setVRDEPort(0);
		vm.setVRDEUsername(null);

		VirtualMachineDao.getInstance().changeState(vm, true);

		markAsCompleted();
	}

	@Override
	public String getType() {
		return "STOPVM";
	}
}