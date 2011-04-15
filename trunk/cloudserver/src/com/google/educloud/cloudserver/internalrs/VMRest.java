package com.google.educloud.cloudserver.internalrs;

import java.util.List;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.database.dao.NodeDao;
import com.google.educloud.cloudserver.database.dao.VirtualMachineDao;
import com.google.educloud.internal.entities.Node;
import com.google.educloud.internal.entities.VirtualMachine;
import com.google.educloud.to.RegisterVirtualMachineTO;
import com.google.gson.Gson;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/vm")
public class VMRest {

	private static Gson gson = new Gson();

	private static Logger LOG = Logger.getLogger(VMRest.class);

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/changeState")
	public Response getChangeState(String jsonMachine) {

		LOG.debug("Returning application status");

		VirtualMachine machine = gson.fromJson(jsonMachine, VirtualMachine.class);

		VirtualMachineDao.getInstance().changeState(machine);

		return Response.ok(gson.toJson(machine), MediaType.APPLICATION_JSON).build();
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/registerVms")
	public Response registerVms(String jsonRegister) {

		LOG.debug("Registering node Vms");

		//Deserializa o objeto TO.
		RegisterVirtualMachineTO register = gson.fromJson(jsonRegister, RegisterVirtualMachineTO.class); 
		
		//Deserializa o nodo.
		Node node = register.getNodo();
		List<Node> listaNodos = NodeDao.getInstance().findNodeByHostname(node.getHostname());
				
		if( listaNodos.size() > 0 ){
			
			//Pega o primeiro nodo registrado para o host.
			node = listaNodos.get(0);

			for( VirtualMachine machine : register.getMachines() ){
				VirtualMachine vm = VirtualMachineDao.getInstance().findByUuid(machine.getUUID());
							
				if( vm != null ){
					vm.setNodeId(node.getId());
					VirtualMachineDao.getInstance().updateNode(vm.getId(), node.getId());
				}					
			}		
		}
		
		return Response.ok().build();
	}
}
