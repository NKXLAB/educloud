package com.google.educloud.cloudserver.managers;

import com.google.educloud.cloudserver.database.dao.TaskDao;
import com.google.educloud.cloudserver.database.dao.VirtualMachineDao;
import com.google.educloud.cloudserver.scheduler.tasks.AbstractTask;
import com.google.educloud.cloudserver.scheduler.tasks.CloudTask.Status;
import com.google.educloud.cloudserver.scheduler.tasks.CreateVmTask;
import com.google.educloud.cloudserver.scheduler.tasks.RemoveVmTask;
import com.google.educloud.cloudserver.scheduler.tasks.StartVmTask;
import com.google.educloud.cloudserver.scheduler.tasks.StopVMTask;
import com.google.educloud.internal.entities.Template;
import com.google.educloud.internal.entities.VirtualMachine;
import com.google.educloud.internal.entities.VirtualMachine.VMState;

public class VMManager {

	public void CreateVM(VirtualMachine vm, Template template) {

		vm.setState(VMState.PENDING);

		VirtualMachineDao.getInstance().insert(vm, template);

		AbstractTask createVmTask = new CreateVmTask();
		createVmTask.setStatus(Status.PENDING);
		createVmTask.setParameter(CreateVmTask.PARAM_MACHINE_ID, String.valueOf(vm.getId()));
		createVmTask.setParameter(CreateVmTask.PARAM_TEMPLATE_ID, String.valueOf(template.getId()));

		TaskDao.getInstance().insert(createVmTask);
	}

	public VirtualMachine scheduleStartVM(VirtualMachine vm) {

		vm.setState(VMState.PENDING);

		AbstractTask startVmTask = new StartVmTask();
		startVmTask.setStatus(Status.PENDING);
		startVmTask.setParameter(StartVmTask.PARAM_MACHINE_ID, String.valueOf(vm.getId()));

		VirtualMachineDao.getInstance().changeState(vm);

		TaskDao.getInstance().insert(startVmTask);

		return vm;
	}

	public VirtualMachine scheduleStopVM(VirtualMachine vm) {

		vm.setState(VMState.SHUTDOWN);

		AbstractTask stopVmTask = new StopVMTask();
		stopVmTask.setStatus(Status.PENDING);
		stopVmTask.setParameter(StopVMTask.VM_ID, String.valueOf(vm.getId()));

		VirtualMachineDao.getInstance().changeState(vm);

		TaskDao.getInstance().insert(stopVmTask);

		return vm;
	}

	//Escalona uma tarefa de remocao de maquina virtual.
	public void scheduleRemoveVM(VirtualMachine vm) {

		AbstractTask removeVmTask = new RemoveVmTask();
		removeVmTask.setStatus(Status.PENDING);
		removeVmTask.setParameter(RemoveVmTask.VM_ID, String.valueOf(vm.getId()));

		// Robson: Criamos um status para remocao?
		// Tremper: Sim. :)
		//VirtualMachineDao.getInstance().changeState(vm);

		TaskDao.getInstance().insert(removeVmTask);
	}
}