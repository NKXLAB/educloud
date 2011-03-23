package com.google.educloud.cloudserver.nodecllient;

import javax.ws.rs.core.MediaType;

import com.google.educloud.internal.entities.VirtualMachine;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;

public class VMNodeClient extends AbstractNodeClient {

	public VirtualMachine startVM(VirtualMachine machine) throws NodeComunicationException {

		String jsonMachine = gson.toJson(machine);

		ClientResponse response;

		try {
			response = getResouce().path("vm").path("start").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, jsonMachine);
		} catch (ClientHandlerException e) {
			throw new NodeComunicationException("Error on start a new virtual machine from node", e);
		}

		if (response.getStatus() != 200) {
			throw new NodeComunicationException("Error on start a new virtual machine from node");
		}

		return machine;
	}

	public void stopVM(VirtualMachine machine) throws NodeComunicationException {

		String jsonMachine = gson.toJson(machine);

		ClientResponse response;

		try {
			response = getResouce().path("vm").path("stop").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, jsonMachine);
		} catch (ClientHandlerException e) {
			throw new NodeComunicationException("Error on stop the virtual machine from node", e);
		}

		if (response.getStatus() != 200) {
			throw new NodeComunicationException("Error on stop the virtual machine from node");
		}
	}

	public void removeVM(VirtualMachine machine) throws NodeComunicationException {
		
		String jsonMachine = gson.toJson(machine);

		ClientResponse response;

		try {
			response = getResouce().path("vm").path("remove").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, jsonMachine);
		} catch (ClientHandlerException e) {
			throw new NodeComunicationException("Error on remove the virtual machine from node", e);
		}

		if (response.getStatus() != 200) {
			throw new NodeComunicationException("Error on remove the virtual machine from node");
		}
		
	}
}
