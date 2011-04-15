package com.google.educloud.cloudnode.scheduler.tasks;

import java.util.List;

import org.apache.log4j.Logger;
import org.virtualbox.service.IMachine;
import org.virtualbox.service.IVirtualBox;

import com.google.educloud.cloudnode.configuration.NodeConfig;
import com.google.educloud.cloudnode.serverclient.RegistrationClient;
import com.google.educloud.cloudnode.serverclient.VirtualMachineClient;
import com.google.educloud.cloudnode.virtualbox.VirtualBoxConnector;
import com.google.educloud.internal.entities.Node;
import com.google.educloud.internal.entities.VirtualMachine;
import com.google.educloud.to.RegisterVirtualMachineTO;

public class RegistrationVmTask extends AbstractTask {
	
	private static Logger LOG = Logger.getLogger(RegistrationVmTask.class);

	@Override
	public void run() {
		
		//Gera o objeto para acesso ao virtual box.
		IVirtualBox vbox = VirtualBoxConnector.connect(NodeConfig.getVirtualBoxWebservicesUrl());
		
		//Recupera a lista de maquinas registradas no virtual box.
		List<IMachine> listaMaquinas = vbox.getMachines();
		
		//Gera o array para armazenar as maquinas.
		VirtualMachine[] maquinas = new VirtualMachine[listaMaquinas.size()];
		
		//Passa as maquinas da lista para o array.
		for( int indice = 0; indice < listaMaquinas.size(); indice++ ){
			maquinas[indice] = new VirtualMachine();
			maquinas[indice].setUUID(listaMaquinas.get(indice).getId().toString());			
		}
		
		//Gera o nodo com o host e porta.
		Node nodo = new Node();
		nodo.setHostname(NodeConfig.getNodeAddress());
		nodo.setPort(NodeConfig.getNodePort());
		
		//Objeto para ser transferido ao server.
		RegisterVirtualMachineTO register = new RegisterVirtualMachineTO();
		register.setMachines(maquinas);
		register.setNodo(nodo);
		
		//Faz o registro das maquinas no servidor.
		new VirtualMachineClient().registerVms(register);

		LOG.debug("cloud node will register the vms");
	}

}
