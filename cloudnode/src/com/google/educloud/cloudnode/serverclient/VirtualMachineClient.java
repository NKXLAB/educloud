package com.google.educloud.cloudnode.serverclient;


import javax.ws.rs.core.MediaType;

import com.google.educloud.internal.entities.VirtualMachine;
import com.google.educloud.to.RegisterVirtualMachineTO;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class VirtualMachineClient extends AbstractServerClient {

	public void changeState(VirtualMachine machine) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());

		String json = gson.toJson(machine);

		ClientResponse response = service.path("vm").path("changeState").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, json);

		response.close();
	}
	
	public void registerVms( RegisterVirtualMachineTO register ){
		
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());

		String json = gson.toJson(register);

		ClientResponse response = service.path("vm").path("registerVms").
		accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, json);
		response.close();
		
	}

}
