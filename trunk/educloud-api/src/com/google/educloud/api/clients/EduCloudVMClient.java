package com.google.educloud.api.clients;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.google.educloud.api.entities.VirtualMachine;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;


public class EduCloudVMClient extends AbstractClient {

	public VirtualMachine startVM(VirtualMachine machine) throws EduCloudServerException {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());

		String json = gson.toJson(machine);

		ClientResponse response = service.path("vm").path("start").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, json);

		int status = response.getStatus();
		String entity = response.getEntity(String.class);

		handleError(status, entity);

		response.close();

		return gson.fromJson(entity, VirtualMachine.class);
	}
	
	public void stopVM(VirtualMachine machine) throws EduCloudServerException {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());

		String json = gson.toJson(machine);

		ClientResponse response = service.path("vm").path("stop").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, json);

		int status = response.getStatus();
		String entity = response.getEntity(String.class);

		handleError(status, entity);

		response.close();
	}
	
	//Ajustar para retornar um array de virtual machines.
	public List<VirtualMachine> getAll() throws EduCloudServerException {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());

		ClientResponse response = service.path("vm").path("getAll").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class);

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
