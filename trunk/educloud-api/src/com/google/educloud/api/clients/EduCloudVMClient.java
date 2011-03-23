package com.google.educloud.api.clients;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.google.educloud.api.entities.VirtualMachine;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


public class EduCloudVMClient extends AbstractClient {
	
	//Cria uma máquina virtual
	public VirtualMachine createVM(VirtualMachine machine) throws EduCloudServerException {
		
		WebResource service = getWebResouce();

		String json = gson.toJson(machine);

		ClientResponse response = service.path("vm").path("create").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, json);

		int status = response.getStatus();
		String entity = response.getEntity(String.class);

		handleError(status, entity);

		response.close();
		
		return gson.fromJson(entity, VirtualMachine.class);
	}

	//Inicia a execução de uma máquina virtual.
	public VirtualMachine startVM(VirtualMachine machine) throws EduCloudServerException {
		WebResource service = getWebResouce();

		String json = gson.toJson(machine);

		ClientResponse response = service.path("vm").path("start").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, json);

		int status = response.getStatus();
		String entity = response.getEntity(String.class);

		handleError(status, entity);

		response.close();

		return gson.fromJson(entity, VirtualMachine.class);
	}	

	//Para a execução de uma máquina virtual.
	public void stopVM(VirtualMachine machine) throws EduCloudServerException {
		WebResource service = getWebResouce();

		String json = gson.toJson(machine);

		ClientResponse response = service.path("vm").path("stop").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, json);

		int status = response.getStatus();
		String entity = response.getEntity(String.class);

		handleError(status, entity);

		response.close();
	}

	//Remove uma máquina virtual.
	public void removeVM( VirtualMachine machine ) throws EduCloudServerException{
		WebResource service = getWebResouce();

		String json = gson.toJson(machine);

		ClientResponse response = service.path("vm").path("remove").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, json);

		int status = response.getStatus();
		String entity = response.getEntity(String.class);

		handleError(status, entity);

		response.close();		
	}
	
	//Recupera todas as instâncias de máquinas virtuais.
	public List<VirtualMachine> describeInstances() throws EduCloudServerException {
		WebResource service = getWebResouce();

		ClientResponse response = service.path("vm").path("describeInstances").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		int status = response.getStatus();
		String entity = response.getEntity(String.class);

		handleError(status, entity);

		response.close();

		//Recupera o array de retorno.
		VirtualMachine[] virtualMachines =
			gson.fromJson(entity, VirtualMachine[].class);

		//Gera a lista de retorno.
		List<VirtualMachine> listaVirtualMachines =
			new ArrayList<VirtualMachine>();

		for( VirtualMachine vm : virtualMachines )
		{
			listaVirtualMachines.add(vm);
		}

		return listaVirtualMachines;
	}

}
