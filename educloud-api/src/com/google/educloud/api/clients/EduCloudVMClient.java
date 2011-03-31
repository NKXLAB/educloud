package com.google.educloud.api.clients;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.google.educloud.api.entities.Template;
import com.google.educloud.api.entities.VirtualMachine;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;
import com.google.educloud.api.to.NewVirtualMachineTO;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


public class EduCloudVMClient extends AbstractClient {

	/**
	 * Create a new virtual machine
	 *
	 * @param machine
	 * @param template
	 * @return
	 * @throws EduCloudServerException
	 */
	public VirtualMachine createVM(VirtualMachine machine, Template template) throws EduCloudServerException {

		NewVirtualMachineTO newVirtualMachineTO = new NewVirtualMachineTO();
		newVirtualMachineTO.setTemplate(template);
		newVirtualMachineTO.setVirtualMachine(machine);

		WebResource service = getWebResouce();

		String json = gson.toJson(newVirtualMachineTO);

		ClientResponse response = service.path("vm").path("create").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, json);

		int status = response.getStatus();
		String entity = response.getEntity(String.class);

		handleError(status, entity);

		response.close();

		return gson.fromJson(entity, VirtualMachine.class);
	}

	/**
	 * Start a new virtual machine
	 *
	 * @param machine
	 * @return
	 * @throws EduCloudServerException
	 */
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

	/**
	 * Stop a virtual machine
	 *
	 * @param machine
	 * @throws EduCloudServerException
	 */
	public void stopVM(VirtualMachine machine) throws EduCloudServerException {
		WebResource service = getWebResouce();

		String json = gson.toJson(machine);

		ClientResponse response = service.path("vm").path("stop").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, json);

		int status = response.getStatus();
		String entity = response.getEntity(String.class);

		handleError(status, entity);

		response.close();
	}

	/**
	 * remove a virtual machine
	 *
	 * @return
	 * @throws EduCloudServerException
	 */
	public void removeVM( VirtualMachine machine ) throws EduCloudServerException{
		WebResource service = getWebResouce();

		String json = gson.toJson(machine);

		ClientResponse response = service.path("vm").path("remove").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, json);

		int status = response.getStatus();
		String entity = response.getEntity(String.class);

		handleError(status, entity);

		response.close();
	}

	/**
	 * Return list of virtual machines
	 *
	 * @return
	 * @throws EduCloudServerException
	 */
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

	/**
	 * Return a virtual machines
	 *
	 * @return
	 * @throws EduCloudServerException
	 */
	public VirtualMachine getVirtualMachine(int id) throws EduCloudServerException {
		WebResource service = getWebResouce();

		ClientResponse response = service.path("vm").path("get").path(String.valueOf(id)).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		int status = response.getStatus();
		String entity = response.getEntity(String.class);

		handleError(status, entity);

		response.close();

		return gson.fromJson(entity, VirtualMachine.class);
	}

	/**
	 * Delete a list of virtual machines
	 *
	 * @param vms
	 * @throws EduCloudServerException
	 */
	public void delete(ArrayList<Integer> vms) throws EduCloudServerException {
		WebResource service = getWebResouce();

		String json = gson.toJson(vms);

		ClientResponse response = service.path("vm").path("delete").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, json);

		int status = response.getStatus();
		String entity = response.getEntity(String.class);

		handleError(status, entity);
	}
}
