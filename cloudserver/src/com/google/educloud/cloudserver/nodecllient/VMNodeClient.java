package com.google.educloud.cloudserver.nodecllient;

import javax.ws.rs.core.MediaType;

import com.google.educloud.internal.entities.VirtualMachine;
import com.sun.jersey.api.client.ClientResponse;

public class VMNodeClient extends AbstractNodeClient {

	public VirtualMachine startVM(VirtualMachine machine) throws NodeComunicationException {

		String jsonMachine = gson.toJson(machine);

		ClientResponse response = getResouce().path("vm").path("start").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, jsonMachine);

		if (response.getStatus() != 200) {
			throw new NodeComunicationException("Error on start a new virtual machine from node");
		}

		return machine;
	}

}
