package com.google.educloud.cloudnode.scheduler.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
		IVirtualBox vbox = VirtualBoxConnector.connect(NodeConfig
				.getVirtualBoxWebservicesUrl());

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
		String fileName = "disk-machine-" + idVm + "-template-" + idTemplate
				+ ".vdi";
		String templateSrc = NodeConfig.getTemplateDir() + property
				+ template.getFilename();
		String storageTarget = NodeConfig.getStorageDir() + property + fileName;
		String localStorage = NodeConfig.getLocalStorageDir();
		String templateSrcOnLocalStorage = localStorage + property
				+ template.getFilename();

		if (!new File(templateSrc).exists()) {
			throw new FileNotFoundException("Invalid template location '"
					+ templateSrc + "'.");
		}

		// check (and copy as need) if template is on local storage
		checkLocalTemplate(templateSrc, templateSrcOnLocalStorage);

		// make clone of medium inside local storage
		processLocalClone(vbox, fileName, templateSrcOnLocalStorage, localStorage + property + fileName);

		// remove template from local storage
		try {
			new File(templateSrcOnLocalStorage).delete();
			LOG.warn("Local template '" + templateSrcOnLocalStorage + "' removed");
		} catch (SecurityException e) {
			LOG.warn("Impossible delete '" + templateSrcOnLocalStorage + "'");
		}

		// transfer cloned medium to cloud storage
		File fromFile = new File(localStorage + property + fileName);
		File toFile = new File(storageTarget);

		if (fromFile.exists()) {
			if (!toFile.exists()) {
				fromFile.renameTo(toFile);
			}
		}
	}

	private void processLocalClone(IVirtualBox vbox, String fileName,
			String templateSrc, String storageTarget) {
		vm.setBootableMedium(fileName);

		LOG.debug("waiting template clone...");
		long start = System.nanoTime();

		IMedium target = vbox.createHardDisk("VDI", storageTarget);
		/* support only standard variant type */
		IProgress progess = null;
		boolean completed = false;

		IMedium src = getSrcMedium(vbox, templateSrc);
		templateSrc = src.getLocation();

		LOG.debug("will clone: " + templateSrc + " to " + storageTarget);

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

		LOG.debug("end template clone. Elapsed time: " + (end - start)
				/ 1000000000.0);

		target.close();
		progess.release();
		src.release();
		target.release();
	}

	synchronized private void checkLocalTemplate(String templateSrc,
			String templateSrcOnLocalStorage) {
		// will copy template to local storage dir
		if (!new File(templateSrcOnLocalStorage).exists()) {
			// copy template to local storage
			copyFile(templateSrc, templateSrcOnLocalStorage);
		}
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
			medium = vbox.openMedium(locationSrc, DeviceType.HARD_DISK,
					AccessMode.READ_ONLY);
		}

		return medium;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	private void copyFile(String srFile, String dtFile) {
		try {
			File f1 = new File(srFile);
			File f2 = new File(dtFile);
			f2.createNewFile();
			InputStream in = new FileInputStream(f1);

			// For Overwrite the file.
			OutputStream out = new FileOutputStream(f2);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();

			LOG.debug("File '" + srFile + " copied to '" + dtFile + "'");
		} catch (FileNotFoundException ex) {
			LOG.error("Error on copy '" + srFile + " to '" + dtFile + "'", ex);
		} catch (IOException e) {
			LOG.error("Error on copy '" + srFile + " to '" + dtFile + "'", e);
		}
	}

}
