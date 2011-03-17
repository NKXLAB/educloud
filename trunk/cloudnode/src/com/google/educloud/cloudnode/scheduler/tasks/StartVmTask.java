package com.google.educloud.cloudnode.scheduler.tasks;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.virtualbox.DeviceType;
import org.virtualbox.LockType;
import org.virtualbox.StorageBus;
import org.virtualbox.StorageControllerType;
import org.virtualbox.service.IMachine;
import org.virtualbox.service.IMedium;
import org.virtualbox.service.IProgress;
import org.virtualbox.service.ISession;
import org.virtualbox.service.IStorageController;
import org.virtualbox.service.IVirtualBox;
import org.virtualbox.service.IWebsessionManager;

import com.google.educloud.cloudnode.configuration.NodeConfig;
import com.google.educloud.cloudnode.serverclient.VirtualMachineClient;
import com.google.educloud.cloudnode.virtualbox.VirtualBoxConnector;
import com.google.educloud.internal.entities.Template;
import com.google.educloud.internal.entities.VirtualMachine;
import com.google.educloud.internal.entities.VirtualMachine.VMState;

public class StartVmTask extends AbstractTask {

	private static Logger LOG = Logger.getLogger(StartVmTask.class);

	private VirtualMachine vm;

	public void setVirtualMachine(VirtualMachine vm) {
		this.vm = vm;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		LOG.debug("Running start virtual machine task");
		LOG.debug(vm);

		// 1) clone template disk
		IVirtualBox vbox = VirtualBoxConnector.connect(NodeConfig.getVirtualBoxWebservicesUrl());
		String mediumLocation = cloneTemplate(vbox);

		// 2) create a virtual machine
		IMachine machine = createMachine(vbox, mediumLocation);
		machine.release();

		// 4) start virtual machine
		ISession sessionObject = new IWebsessionManager(vbox.port).getSessionObject(vbox);
		machine = vbox.findMachine(vm.getName());
		machine.launchVMProcess(sessionObject, NodeConfig.getVboxFrontendType(), "");

		// 5) notify server that machine was started
		String vmUUID = machine.getId().toString();

		vm.setUUID(vmUUID);
		vm.setVboxSession(vbox._this);
		vm.setState(VMState.RUNNING);

		new VirtualMachineClient().changeState(vm);

	}

	private IMachine createMachine(IVirtualBox vbox, String mediumLocation) {
		IMachine machine = vbox.createMachine(null, vm.getName(), vm.getTemplate().getOsType(), UUID.randomUUID(), false);

		// 3) attach new cloned disk to new virtual machine
		ISession sessionObject = new IWebsessionManager(vbox.port).getSessionObject(vbox);

		String name = "SATA Controller";
		IStorageController sc = machine.addStorageController(name, StorageBus.SATA);
		sc.setControllerType(StorageControllerType.INTEL_AHCI);
		sc.setUseHostIOCache(false);
		sc.setPortCount(30);
		sc.release();

		machine.saveSettings();
		vbox.registerMachine(machine);
		machine.lockMachine(sessionObject, LockType.WRITE);

		machine = sessionObject.getMachine();
		IMedium medium = vbox.findMedium(mediumLocation, DeviceType.HARD_DISK);
		machine.attachDevice(name, 0, 0, DeviceType.HARD_DISK, medium);
		medium.release();

		machine.saveSettings();
		sessionObject.unlockMachine();

		return machine;
	}

	private String cloneTemplate(IVirtualBox vbox) {

		Template template = vm.getTemplate();

		String property = System.getProperty("file.separator");

		int idVm = vm.getId();
		int idTemplate = template.getId();
		String fileName = "disk-machine-"+idVm+"-template-"+ idTemplate + ".vdi";
		String locationSrc = NodeConfig.getTemplateDir() + property + template.getFilename();
		String locationTarget = NodeConfig.getMachinesDir() + property + fileName;

		LOG.debug("will clone: " + locationSrc + " to " + locationTarget);

		LOG.debug("waiting template clone...");
		long start = System.nanoTime();

		IMedium target = vbox.createHardDisk("VDI", locationTarget);
		/* support only standard variant type */
		IProgress progess = target.createBaseStorage(template.getSize(), 0);

		boolean completed = false;
		do {
			completed = progess.getCompleted();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				LOG.debug("create based storage was interrupted");
			}
		} while (!completed);
		progess.release();

		IMedium src = vbox.findMedium(locationSrc, DeviceType.HARD_DISK);

		progess = src.cloneTo(target, 0, null);

		completed = false;
		do {
			completed = progess.getCompleted();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (!completed);
		long end = System.nanoTime();

		LOG.debug("end template clone. Elapsed time: " + (end-start) / 1000000000.0);

		progess.release();
		src.release();
		target.release();

		return locationTarget;
	}

}
