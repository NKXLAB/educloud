package com.google.educloud.cloudserver.scheduler.tasks;

import java.util.Calendar;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.database.dao.TemplateDao;
import com.google.educloud.cloudserver.database.dao.VirtualMachineDao;
import com.google.educloud.cloudserver.database.dao.VirtualMachineLogDao;
import com.google.educloud.cloudserver.nodecllient.ClientFactory;
import com.google.educloud.cloudserver.nodecllient.NodeComunicationException;
import com.google.educloud.cloudserver.nodecllient.VMNodeClient;
import com.google.educloud.cloudserver.selector.NodeSelectorManager;
import com.google.educloud.internal.entities.Node;
import com.google.educloud.internal.entities.Template;
import com.google.educloud.internal.entities.VirtualMachine;
import com.google.educloud.internal.entities.VirtualMachine.VMState;


public class CreateVmTask extends AbstractTask {

	public static final String PARAM_MACHINE_ID = "VM_ID";

	public static final String PARAM_TEMPLATE_ID = "TPL_ID";

	private static Logger LOG = Logger.getLogger(CreateVmTask.class);

	@Override
	public void run() {

		markAsRunning();

		// 1) load virtual machine from database
		String vmId = getParameter(PARAM_MACHINE_ID);
		VirtualMachine vm = VirtualMachineDao.getInstance().findById(Integer.parseInt(vmId));

		String tplId = getParameter(PARAM_TEMPLATE_ID);
		Template template = TemplateDao.getInstance().findById(Integer.parseInt(tplId));

		// 2) select a registered host
		Node node = NodeSelectorManager.getSelector().getNext(vm);

		if (null == node) {
			LOG.warn("The virtual machine #" + vmId + " cannot be created, no instance of cloudnode available");
			VirtualMachineLogDao.getInstance().insert(Integer.parseInt(vmId), "Error on create virtual machine " + vmId + ", no instance of cloudnode is available.");

			// will try start virtual machine after 10 min.
			vm.setState(VMState.PENDING);

			// change virtual machine state to pending
			VirtualMachineDao.getInstance().changeState(vm);

			// reschedule this task
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MINUTE, 10);
			reschedule(calendar.getTime());
			return;
		}

		vm.setNodeId(node.getId());
		LOG.debug("Selected node for new virtual machine: #" + node.getId());

		// 3) send requisition for host
		VMNodeClient nodeClient = ClientFactory.createVMNodeClient(node);
		try {
			nodeClient.createVM(vm, template);
		} catch (NodeComunicationException e) {
			LOG.error("An error when start virtual machine: #" + vm.getId(), e);
		}

		VirtualMachineDao.getInstance().updateNode(vm.getId(), vm.getNodeId());

		markAsCompleted();
	}

	@Override
	public String getType() {
		return "CREATEVM";
	}

}
