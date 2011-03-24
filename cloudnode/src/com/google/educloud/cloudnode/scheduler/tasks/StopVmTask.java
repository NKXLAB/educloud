package com.google.educloud.cloudnode.scheduler.tasks;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.virtualbox.CleanupMode;
import org.virtualbox.service.IConsole;
import org.virtualbox.service.IMachine;
import org.virtualbox.service.IMedium;
import org.virtualbox.service.IProgress;
import org.virtualbox.service.ISession;
import org.virtualbox.service.IVirtualBox;
import org.virtualbox.service.IWebsessionManager;

import com.google.educloud.cloudnode.configuration.NodeConfig;
import com.google.educloud.cloudnode.virtualbox.VirtualBoxConnector;
import com.google.educloud.internal.entities.VirtualMachine;

public class StopVmTask extends AbstractTask {

	private static Logger LOG = Logger.getLogger(StopVmTask.class);

	private VirtualMachine vm;

	public void setVirtualMachine(VirtualMachine vm) {
		this.vm = vm;
	}

	@Override
	public void run() {
		LOG.debug("Running stop virtual machine task");

		// 1) Stop virtual machine process
		IVirtualBox vbox = VirtualBoxConnector.restoreSession(vm.getVboxSession());
		IWebsessionManager manager = new IWebsessionManager(vbox.port);

		ISession session = manager.getSessionObject(vbox);
		IConsole console = session.getConsole();

		IProgress progress = console.powerDown();

		boolean completed = false;
		do {
			completed = progress.getCompleted();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				LOG.error("Virtual machine process was interrupted", e);
			}
		} while (!completed);

		progress.release();
		console.release();

		// 2) Detach virtual machine medium
		IMachine machine = vbox.findMachine(vm.getUUID());
		machine.unregister(CleanupMode.FULL);
		machine.delete(new ArrayList<IMedium>());
		machine.release();
		
		// 3) put the virtual machine back to machine storage dir
		String separador = System.getProperty("file.separator");
		File arquivoOrigem = new File(NodeConfig.getMachinesDir() + separador + vm.getBootableMedium());
		File arquivoDestino = new File( NodeConfig.getStorageDir() + separador + vm.getBootableMedium());
		
		if( arquivoOrigem.exists() ){
			
			if( arquivoDestino.exists() ){
				//Remove o arquivo de destino.
				arquivoDestino.delete();
			}
			
			//Move para o storage.
			arquivoOrigem.renameTo(arquivoDestino);
		}
		else
		{
			//Arquivo de origem n�o existe.
		}

		// 4) notify server that machine was dropped
		session.release();
		vbox.release();
	}

}