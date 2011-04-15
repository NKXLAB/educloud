package com.google.educloud.cloudnode.scheduler.tasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.apache.log4j.Logger;
import org.virtualbox.AccessMode;
import org.virtualbox.DeviceType;
import org.virtualbox.service.IMedium;
import org.virtualbox.service.IProgress;
import org.virtualbox.service.IVirtualBox;

import com.google.educloud.cloudnode.configuration.NodeConfig;
import com.google.educloud.cloudnode.serverclient.VirtualMachineClient;
import com.google.educloud.cloudnode.virtualbox.VirtualBoxConnector;
import com.google.educloud.internal.entities.Template;
import com.google.educloud.internal.entities.VirtualMachine;
import com.google.educloud.internal.entities.VirtualMachine.VMState;

public class CreateVmTask extends AbstractTask {

	private static Logger LOG = Logger.getLogger(CreateVmTask.class);

	private VirtualMachine vm;

	private Template template;

	public void setVirtualMachine(VirtualMachine vm) {
		this.vm = vm;
	}

	@Override
	public void run() {
		LOG.debug("Running create virtual machine task");

		// 1) clone template disk
		IVirtualBox vbox = VirtualBoxConnector.connect(NodeConfig.getVirtualBoxWebservicesUrl());

		try {
			cloneTemplate(vbox);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		vm.setState(VMState.DONE);

		// 2) change virtual machine state
		new VirtualMachineClient().changeState(vm);
	}

	private void cloneTemplate(IVirtualBox vbox) throws FileNotFoundException {

		String property = System.getProperty("file.separator");

		int idVm = vm.getId();
		int idTemplate = template.getId();
		String fileName = "disk-machine-"+idVm+"-template-"+ idTemplate + ".vdi";
		String locationSrc = NodeConfig.getTemplateDir() + property + template.getFilename();
		String locationTarget = NodeConfig.getStorageDir() + property + fileName;
		
		if( !new File(locationSrc).exists() ){
			throw new FileNotFoundException("Arquivo de template " + locationSrc + " n�o encontrado.");
		}			

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
				LOG.warn(e);
			}
		} while (!completed);
		long end = System.nanoTime();

		LOG.debug("end template clone. Elapsed time: " + (end-start) / 1000000000.0);

		target.close();
		progess.release();
		src.release();
		target.release();
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

	public void setTemplate(Template template) {
		this.template = template;
	}

}