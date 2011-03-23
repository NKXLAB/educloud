package com.google.educloud.cloudnode.scheduler.tasks;

import java.io.File;

import org.apache.log4j.Logger;
import org.virtualbox.IVRDEServerInfo;
import org.virtualbox.service.IConsole;
import org.virtualbox.service.IMachine;
import org.virtualbox.service.IProgress;
import org.virtualbox.service.ISession;
import org.virtualbox.service.IVirtualBox;
import org.virtualbox.service.IWebsessionManager;

import com.google.educloud.cloudnode.configuration.NodeConfig;
import com.google.educloud.cloudnode.serverclient.VirtualMachineClient;
import com.google.educloud.cloudnode.virtualbox.VirtualBoxConnector;
import com.google.educloud.internal.entities.VirtualMachine;
import com.google.educloud.internal.entities.VirtualMachine.VMState;

public class RemoveVmTask extends AbstractTask {
	
	private static Logger LOG = Logger.getLogger(RemoveVmTask.class);

	private VirtualMachine vm;

	public void setVirtualMachine(VirtualMachine vm) {
		this.vm = vm;
	}

	@Override
	public void run() {
		
		LOG.debug("Running remove virtual machine task");
		
		//Pega o separador de arquivos.
		String separador = System.getProperty("file.separator");

		//Gera o nome do arquivo da máquina virtual que deve ser removida.
		String mediumLocation = 
			NodeConfig.getStorageDir() + separador + vm.getBootableMedium();
		
		//Gera a referencia para o arquivo.
		File arquivo = new File(mediumLocation);
		
		if(arquivo.exists() ){			
			if( !arquivo.delete() ){
				//Não conseguiu remover o arquivo.
			}			
		}
		else {
			//Arquivo não existe.
		}
	}
}
