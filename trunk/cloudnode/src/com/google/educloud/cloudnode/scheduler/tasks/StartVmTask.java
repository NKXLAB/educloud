package com.google.educloud.cloudnode.scheduler.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.virtualbox.AccessMode;
import org.virtualbox.CPUPropertyType;
import org.virtualbox.CleanupMode;
import org.virtualbox.DeviceType;
import org.virtualbox.IVRDEServerInfo;
import org.virtualbox.LockType;
import org.virtualbox.NetworkAdapterType;
import org.virtualbox.StorageBus;
import org.virtualbox.StorageControllerType;
import org.virtualbox.service.IAudioAdapter;
import org.virtualbox.service.IBIOSSettings;
import org.virtualbox.service.IConsole;
import org.virtualbox.service.IMachine;
import org.virtualbox.service.IMedium;
import org.virtualbox.service.INetworkAdapter;
import org.virtualbox.service.IProgress;
import org.virtualbox.service.ISession;
import org.virtualbox.service.IStorageController;
import org.virtualbox.service.ISystemProperties;
import org.virtualbox.service.IUSBController;
import org.virtualbox.service.IVRDEServer;
import org.virtualbox.service.IVirtualBox;
import org.virtualbox.service.IWebsessionManager;

import com.google.educloud.cloudnode.configuration.NodeConfig;
import com.google.educloud.cloudnode.serverclient.VirtualMachineClient;
import com.google.educloud.cloudnode.virtualbox.VirtualBoxConnector;
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
		LOG.debug("Running start virtual machine task");

		// 1) create new session from vbox
		IVirtualBox vbox = VirtualBoxConnector.connect(NodeConfig.getVirtualBoxWebservicesUrl());

		String bootableMedium = vm.getBootableMedium();

		String mediumLocation;

		// 2) copy bootable medium to machine dir
		String property = System.getProperty("file.separator");

		File arquivoOrigem = new File(NodeConfig.getStorageDir() + property + bootableMedium);
		File arquivoDestino = new File(NodeConfig.getMachinesDir() + property + bootableMedium);

		if (arquivoOrigem.exists()) {
			if (!arquivoDestino.exists()) {
				// Copia do diretorio de storage para o diretorio de maquinas.
				arquivoOrigem.renameTo(arquivoDestino);
			}
		} else {
			// Arquivo da maquina nao existe.
		}

		mediumLocation = arquivoDestino.getAbsolutePath();

		// 3) create a virtual machine from vbox
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

		LOG.debug("VRDE active " + active);
		LOG.debug("VRDE port " + port);

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
				LOG.warn("Virtual machine '" + vm.getName()
						+ "' already exists, cloudnode will remove");

				iMachine.unregister(CleanupMode.FULL);
				iMachine.delete(new ArrayList<IMedium>());
			}
			iMachine.release();
		}

		machine = vbox.createMachine(null, vm.getName(), vm.getOsType(),
				UUID.randomUUID(), false);

		// 3) attach new cloned disk to new virtual machine
		ISession sessionObject = new IWebsessionManager(vbox.port)
				.getSessionObject(vbox);

		String name = "SATA Controller";
		IStorageController sc = machine.addStorageController(name,
				StorageBus.SATA);
		sc.setControllerType(StorageControllerType.INTEL_AHCI);
		sc.setUseHostIOCache(false);
		sc.setPortCount(1);
		sc.release();

		ISystemProperties systemProperties = vbox.getSystemProperties();
		String defaultVRDEExtPack = systemProperties.getDefaultVRDEExtPack();
		/* add support for vrde server */
		IVRDEServer vrdeServer = machine.getVRDEServer();
		vrdeServer.setEnabled(true);
		vrdeServer.setAuthTimeout(5000);
		vrdeServer.setVRDEProperty("TCP/Ports", "5040");
		vrdeServer.setVRDEExtPack(null);
		vrdeServer.release();

		IBIOSSettings biosSettings = machine.getBIOSSettings();
		biosSettings.setACPIEnabled(true);
		biosSettings.release();

		INetworkAdapter networkAdapter = machine.getNetworkAdapter(0);
		networkAdapter.setMACAddress(networkAdapter.getMACAddress());
		networkAdapter.setHostInterface("Dell Wireless 1397 WLAN Mini-Card");
		networkAdapter.setAdapterType(NetworkAdapterType.AM_79_C_973);
		networkAdapter.setCableConnected(true);
		networkAdapter.setEnabled(true);

		networkAdapter.attachToBridgedInterface();
		networkAdapter.release();

		machine.saveSettings();
		vbox.registerMachine(machine);
		machine.lockMachine(sessionObject, LockType.WRITE);
		machine.release();

		machine = sessionObject.getMachine();

		IAudioAdapter audioAdapter = machine.getAudioAdapter();
		audioAdapter.setEnabled(true);
		audioAdapter.release();
		machine.setRTCUseUTC(true);
		IUSBController usbController = machine.getUSBController();
		usbController.setEnabled(true);
		usbController.release();

		machine.setCPUProperty(CPUPropertyType.PAE, false);
		machine.setMemorySize(192);

		IMedium medium = getSrcMedium(vbox, mediumLocation);
		machine.attachDevice(name, 0, 0, DeviceType.HARD_DISK, medium);
		medium.release();

		machine.saveSettings();
		sessionObject.unlockMachine();

		return machine;
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

			if (location.equals(locationSrc)) {
				uuid = iMedium.getId().toString();
			}

			iMedium.release();
		}

		IMedium medium = null;
		if (uuid != null) {
			medium = vbox.findMedium(uuid, DeviceType.HARD_DISK);
		} else {
			medium = vbox.openMedium(locationSrc, DeviceType.HARD_DISK,
					AccessMode.READ_ONLY);
		}

		return medium;
	}

}
