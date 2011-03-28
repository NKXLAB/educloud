package com.google.educloud.cloudnode.scheduler.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.virtualbox.AccessMode;
import org.virtualbox.CleanupMode;
import org.virtualbox.DeviceType;
import org.virtualbox.IVRDEServerInfo;
import org.virtualbox.LockType;
import org.virtualbox.StorageBus;
import org.virtualbox.StorageControllerType;
import org.virtualbox.service.IBIOSSettings;
import org.virtualbox.service.IConsole;
import org.virtualbox.service.IMachine;
import org.virtualbox.service.IMedium;
import org.virtualbox.service.IProgress;
import org.virtualbox.service.ISession;
import org.virtualbox.service.IStorageController;
import org.virtualbox.service.ISystemProperties;
import org.virtualbox.service.IVRDEServer;
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

		// 1) clone template disk
		IVirtualBox vbox = VirtualBoxConnector.connect(NodeConfig.getVirtualBoxWebservicesUrl());

		String bootableMedium = vm.getBootableMedium();

		String mediumLocation;
		if (null == bootableMedium) {
			mediumLocation = cloneTemplate(vbox);
		} else {
			String property = System.getProperty("file.separator");

			File arquivoOrigem = new File( NodeConfig.getStorageDir() + property + bootableMedium );
			File arquivoDestino = new File( NodeConfig.getMachinesDir() + property + bootableMedium );

			if( arquivoOrigem.exists() )
			{
				if( !arquivoDestino.exists() ){
					//Copia do diretorio de storage para o diretorio de maquinas.
					arquivoOrigem.renameTo(arquivoDestino);
				}
			}
			else
			{
				//Arquivo da maquina nao existe.
			}

			mediumLocation = arquivoDestino.getAbsolutePath();
		}

		// 2) create a virtual machine
		IMachine machine = createMachine(vbox, mediumLocation);
		machine.release();

		// 4) start virtual machine
		ISession sessionObject = new IWebsessionManager(vbox.port).getSessionObject(vbox);
		machine = vbox.findMachine(vm.getName());
		IProgress process = machine.launchVMProcess(sessionObject, NodeConfig.getVboxFrontendType(), "");

		boolean completed = false;
		do {
			completed = process.getCompleted();
		} while (!completed);

		IConsole console = sessionObject.getConsole();
		IVRDEServerInfo vrdeServerInfo = console.getVRDEServerInfo();

		boolean active = vrdeServerInfo.isActive();
		int port = vrdeServerInfo.getPort();

		// 5) notify server that machine was started
		String vmUUID = machine.getId().toString();

		vm.setUUID(vmUUID);
		vm.setVboxSession(vbox._this);
		vm.setState(VMState.RUNNING);

		new VirtualMachineClient().changeState(vm);

	}

	private IMachine createMachine(IVirtualBox vbox, String mediumLocation) {
		IMachine machine;

		List<IMachine> machines = vbox.getMachines();

		for (IMachine iMachine : machines) {
			String name = iMachine.getName();
			if (name.equals(vm.getName())) {
				LOG.warn("Virtual machine '" + vm.getName() + "' already exists, cloudnode will remove");

				iMachine.unregister(CleanupMode.FULL);
				iMachine.delete(new ArrayList<IMedium>());
			}
			iMachine.release();
		}

		machine = vbox.createMachine(null, vm.getName(), vm.getTemplate().getOsType(), UUID.randomUUID(), false);

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
		machine.release();

		machine = sessionObject.getMachine();

		ISystemProperties systemProperties = vbox.getSystemProperties();
		String defaultVRDEExtPack = systemProperties.getDefaultVRDEExtPack();
		/* add support for vrde server */
		IVRDEServer vrdeServer = machine.getVRDEServer();
		vrdeServer.setEnabled(true);
		vrdeServer.setAuthTimeout(5000);
		vrdeServer.setVRDEProperty("TCP/Ports", "3358");
		vrdeServer.setVRDEExtPack(defaultVRDEExtPack);
		vrdeServer.release();

		IBIOSSettings biosSettings = machine.getBIOSSettings();
		biosSettings.setACPIEnabled(true);
		biosSettings.release();

		IMedium medium = getSrcMedium(vbox, mediumLocation);
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

		vm.setBootableMedium(fileName);

		LOG.debug("waiting template clone...");
		long start = System.nanoTime();

		IMedium target = vbox.createHardDisk("VDI", locationTarget);
		/* support only standard variant type */
		IProgress progess = null;
		boolean completed = false;

		IMedium src = getSrcMedium(vbox, locationSrc);
		locationSrc = src.getLocation();

		LOG.debug("will clone: " + locationSrc + " to " + locationTarget);

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

	/**
	 * Gets src medium registered or not
	 *
	 * @param vbox
	 * @param locationSrc
	 * @return
	 */
	private IMedium getSrcMedium(IVirtualBox vbox, String locationSrc) {
		List<IMedium> hardDisks = vbox.getHardDisks();

		String uuid = null;
		for (IMedium iMedium : hardDisks) {
			String location = iMedium.getLocation();

			String registeredName = new File(location).getName();
			String srcName = new File(locationSrc).getName();

			if (registeredName.equals(srcName)) {
				uuid = iMedium.getId().toString();
			}

			iMedium.release();
		}

		IMedium medium = null;
		if (uuid != null) {
			medium = vbox.findMedium(uuid, DeviceType.HARD_DISK);
		} else {
			medium = vbox.openMedium(locationSrc, DeviceType.HARD_DISK, AccessMode.READ_ONLY);
		}

		return medium;
	}

}
