package com.google.educloud.cloudserver.managers;

import com.google.educloud.cloudserver.database.dao.TaskDao;
import com.google.educloud.cloudserver.database.dao.VirtualMachineDao;
import com.google.educloud.cloudserver.scheduler.tasks.AbstractTask;
import com.google.educloud.cloudserver.scheduler.tasks.CloudTask.Status;
import com.google.educloud.cloudserver.scheduler.tasks.RemoveVmTask;
import com.google.educloud.cloudserver.scheduler.tasks.StartVmTask;
import com.google.educloud.cloudserver.scheduler.tasks.StopVMTask;
import com.google.educloud.internal.entities.VirtualMachine;
import com.google.educloud.internal.entities.VirtualMachine.VMState;

public class VMManager {
	
	public void CreateVM(VirtualMachine vm) {
		
		VirtualMachineDao.getInstance().insert(vm);		
	}

	public VirtualMachine scheduleStartVM(VirtualMachine vm) {

		vm = VirtualMachineDao.getInstance().findById(vm.getId());
		vm.setState(VMState.PENDING);

		AbstractTask startVmTask = new StartVmTask();
		startVmTask.setStatus(Status.PENDING);
		startVmTask.setParameter(StartVmTask.VM_ID, String.valueOf(vm.getId()));

		VirtualMachineDao.getInstance().changeState(vm);

		TaskDao.getInstance().insert(startVmTask);

		return vm;
	}

	public VirtualMachine scheduleStopVM(VirtualMachine vm) {

		vm = VirtualMachineDao.getInstance().findById(vm.getId());

		vm.setState(VMState.SHUTDOWN);

		AbstractTask stopVmTask = new StopVMTask();
		stopVmTask.setStatus(Status.PENDING);
		stopVmTask.setParameter(StopVMTask.VM_ID, String.valueOf(vm.getId()));

		VirtualMachineDao.getInstance().changeState(vm);

		TaskDao.getInstance().insert(stopVmTask);

		return vm;
	}

	//Escalona uma tarefa de remo��o de m�quina virtual.
	public void scheduleRemoveVM(VirtualMachine vm) {
		
		vm = VirtualMachineDao.getInstance().findById(vm.getId());

		AbstractTask removeVmTask = new RemoveVmTask();
		removeVmTask.setStatus(Status.PENDING);
		removeVmTask.setParameter(RemoveVmTask.VM_ID, String.valueOf(vm.getId()));

		//Criamos um status para remo��o?
		//VirtualMachineDao.getInstance().changeState(vm);

		TaskDao.getInstance().insert(removeVmTask);		
	}	
}