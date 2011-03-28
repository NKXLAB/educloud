package com.google.educloud.cloudserver.rs;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.educloud.api.entities.EduCloudErrorMessage;
import com.google.educloud.api.entities.VirtualMachine;
import com.google.educloud.api.entities.VirtualMachine.VMState;
import com.google.educloud.cloudserver.database.dao.TemplateDao;
import com.google.educloud.cloudserver.database.dao.VirtualMachineDao;
import com.google.educloud.cloudserver.entity.CloudSession;
import com.google.educloud.cloudserver.entity.User;
import com.google.educloud.cloudserver.entity.User.UserType;
import com.google.educloud.cloudserver.managers.VMManager;
import com.google.educloud.internal.entities.Template;
import com.sun.jersey.spi.container.servlet.PerSession;

@PerSession
@Path("/vm")
public class VMRest extends CloudResource {

	private static Logger LOG = Logger.getLogger(VMRest.class);

	/**
	 * this method will create a virtual machine.
	 *
	 * @param machine
	 * @return
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/create")
	public Response createVM(String machine) {
		
		LOG.debug("Application will create a new VM");
		
		//Recupera a cloud session.
		CloudSession cloudSession = 
			(CloudSession)request.getSession().getAttribute(CloudSession.HTTP_ATTR_NAME);
		
		//Recupera o usuario logado.
		User usuario = cloudSession.getUser();		

		VirtualMachine externalMachine = gson.fromJson(machine, VirtualMachine.class);

		//Cria uma entidade interna (Virtual Machine) baseada na recebida.
		com.google.educloud.internal.entities.VirtualMachine vm =
			new com.google.educloud.internal.entities.VirtualMachine();

		//Cria uma entidade interna (Template) baseada na recebida
		com.google.educloud.internal.entities.Template tpt =
			new com.google.educloud.internal.entities.Template();
		tpt.setId(externalMachine.getTemplate().getId());
		
		//Recupera o template
		tpt = TemplateDao.getInstance().findById(tpt.getId());
		
		if( tpt == null ){
			
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-001");
			error.setHint("Set template id and try again");
			error.setText("Apparently you are trying to start a nonexistent template");

			// return error message
			return Response.status(400).entity(gson.toJson(error)).build();			
		}		

		//Ajusta os valores da m�quina virtual.
		vm.setTemplate(tpt);
		vm.setName(externalMachine.getName());
		
		if( usuario.getType() == UserType.ADMIN )
			vm.setUserId(externalMachine.getUserId());
		else
			vm.setUserId(usuario.getId());
		
		vm.setState(com.google.educloud.internal.entities.VirtualMachine.VMState.DONE);		

		//Logica de inicializa��o da VM.
		VMManager vmManager = new VMManager();
		vmManager.CreateVM(vm);

		//Atualiza o ID para retornar ao client.
		externalMachine.setId(vm.getId());
		externalMachine.setState(VMState.valueOf(vm.getState().name()));

		//Retorna a VirtualMachine criada.
		return Response.ok(gson.toJson(externalMachine), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * this method will schedule a new allocation of a virtual machine
	 *
	 * @param machine
	 * @return
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/start")
	public Response startVM(String machine) {

		LOG.debug("Application will start a new VM");
		LOG.debug(machine);
		
		//Recupera a cloud session.
		CloudSession cloudSession = 
			(CloudSession)request.getSession().getAttribute(CloudSession.HTTP_ATTR_NAME);
		
		//Recupera o usuario logado.
		User usuario = cloudSession.getUser();

		//Recupera a entidade externa.
		VirtualMachine externalMachine = gson.fromJson(machine, VirtualMachine.class);

		int id = externalMachine.getId();

		/* validations */
		if (id == 0) {
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-001");
			error.setHint("Set virtual machine id and try again");
			error.setText("Apparently you are trying to start a nonexistent virtual machine");

			// return error message
			return Response.status(400).entity(gson.toJson(error)).build();
		}

		/* create internal entity (Virtual Machine) based on received */
		com.google.educloud.internal.entities.VirtualMachine vm = new com.google.educloud.internal.entities.VirtualMachine();
		vm.setId(externalMachine.getId());
		vm.setUserId(externalMachine.getUserId());
		
		//Recupera a instancia da maquina virtual de acordo com o usuario.
		if( usuario.getType() == UserType.ADMIN )
			vm = VirtualMachineDao.getInstance().findById(vm.getId());
		else
			vm = VirtualMachineDao.getInstance().findByIdAndUser(vm);
		
		if( vm == null ){
			
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-006");
			error.setHint("That machine does not exists or the user has not access");
			error.setText("That machine does not exists or the user has not access");

			// return error message
			return Response.status(400).entity(gson.toJson(error)).build();			
		}
		else if( vm.getState() != 
			com.google.educloud.internal.entities.VirtualMachine.VMState.DONE ){
			
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-006");
			error.setHint("Invalid state to start this virtual machine");
			error.setText("Invalid state to start this virtual machine");

			// return error message
			return Response.status(400).entity(gson.toJson(error)).build();			
		}

		/* vm start logic */
		VMManager vmManager = new VMManager();
		vm = vmManager.scheduleStartVM(vm);

		/* update external machine to return for client */
		externalMachine.setId(vm.getId());
		externalMachine.setState(VMState.valueOf(vm.getState().name()));

		// return a new created virtual machine
		return Response.ok(gson.toJson(externalMachine), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * this method will schedule a new allocation of a virtual machine
	 *
	 * @param machine
	 * @return
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/stop")
	public Response stopVM(String machine) {

		LOG.debug("Application will stop a VM");
		LOG.debug(machine);
		
		//Recupera a cloud session.
		CloudSession cloudSession = 
			(CloudSession)request.getSession().getAttribute(CloudSession.HTTP_ATTR_NAME);
		
		//Recupera o usuario logado.
		User usuario = cloudSession.getUser();

		VirtualMachine externalMachine = gson.fromJson(machine, VirtualMachine.class);

		int id = externalMachine.getId();

		/* validations */
		if (id == 0) {
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-003");
			error.setHint("Set virtual machine id and try again");
			error.setText("Apparently you are trying to stop a nonexistent virtual machine");

			// return error message
			return Response.status(400).entity(gson.toJson(error)).build();
		}

		com.google.educloud.internal.entities.VirtualMachine vm = new com.google.educloud.internal.entities.VirtualMachine();
		vm.setId(externalMachine.getId());
		vm.setUserId(externalMachine.getUserId());
		
		//Recupera a instancia da maquina virtual de acordo com o usuario.
		if( usuario.getType() == UserType.ADMIN )
			vm = VirtualMachineDao.getInstance().findById(vm.getId());
		else
			vm = VirtualMachineDao.getInstance().findByIdAndUser(vm);
		
		if( vm == null ){
			
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-006");
			error.setHint("That machine does not exists or the user has not access");
			error.setText("That machine does not exists or the user has not access");

			// return error message
			return Response.status(400).entity(gson.toJson(error)).build();			
		}
		else if( vm.getState() != 
			com.google.educloud.internal.entities.VirtualMachine.VMState.RUNNING ){
			
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-006");
			error.setHint("Invalid state to stop this virtual machine");
			error.setText("Invalid state to stop this virtual machine");

			// return error message
			return Response.status(400).entity(gson.toJson(error)).build();			
		}

		/* vm start logic */
		VMManager vmManager = new VMManager();
		vm = vmManager.scheduleStopVM(vm);
		externalMachine.setState(VMState.valueOf(vm.getState().name()));

		// return a new created virtual machine
		return Response.ok(gson.toJson(externalMachine), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * this method will schedule a task to remove a virtual machine.
	 *
	 * @param machine
	 * @return
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/remove")
	public Response removeVM(String machine) {

		LOG.debug("Application will remove a VM");
		LOG.debug(machine);
		
		//Recupera a cloud session.
		CloudSession cloudSession = 
			(CloudSession)request.getSession().getAttribute(CloudSession.HTTP_ATTR_NAME);
		
		//Recupera o usuario logado.
		User usuario = cloudSession.getUser();

		VirtualMachine externalMachine = gson.fromJson(machine, VirtualMachine.class);

		int id = externalMachine.getId();

		/* validations */
		if (id == 0) {
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-004");
			error.setHint("Set virtual machine id and try again");
			error.setText("Apparently you are trying to remove a nonexistent virtual machine");

			// return error message
			return Response.status(400).entity(gson.toJson(error)).build();
		}

		com.google.educloud.internal.entities.VirtualMachine vm =
			new com.google.educloud.internal.entities.VirtualMachine();
		vm.setId(externalMachine.getId());
		vm.setUserId(externalMachine.getUserId());

		//Recupera a instancia da maquina virtual de acordo com o usuario.
		if( usuario.getType() == UserType.ADMIN )
			vm = VirtualMachineDao.getInstance().findById(vm.getId());
		else
			vm = VirtualMachineDao.getInstance().findByIdAndUser(vm);
		
		if( vm == null ){
			
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-006");
			error.setHint("That machine does not exists or the user has not access");
			error.setText("That machine does not exists or the user has not access");

			// return error message
			return Response.status(400).entity(gson.toJson(error)).build();			
		}
		else if( vm.getState() != 
			com.google.educloud.internal.entities.VirtualMachine.VMState.DONE ){
			
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-006");
			error.setHint("Invalid state to remove this virtual machine");
			error.setText("Invalid state to remove this virtual machine");

			// return error message
			return Response.status(400).entity(gson.toJson(error)).build();			
		}
		
		/* vm start logic */
		VMManager vmManager = new VMManager();
		vmManager.scheduleRemoveVM(vm);		

		// return ok.
		return Response.ok().build();
	}

	/**
	 * this method will retrive all started vms
	 *
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/describeInstances")
	public Response describeInstances() {

		LOG.debug("Application will list all machines");
		
		//Recupera a cloud session.
		CloudSession cloudSession = 
			(CloudSession)request.getSession().getAttribute(CloudSession.HTTP_ATTR_NAME);
		
		//Recupera o usuario logado.
		User usuario = cloudSession.getUser();

		//Recupera a lista de maquinas virtuais da base de dados.
		List<com.google.educloud.internal.entities.VirtualMachine> listaVirtualMachines = null;
		
		//Recupera as inst�ncias de maquina virtuais de acordo com o usuario.
		if( usuario.getType() == UserType.ADMIN )
			listaVirtualMachines = VirtualMachineDao.getInstance().getAll();
		else
			listaVirtualMachines = VirtualMachineDao.getInstance().getAllByUser(usuario.getId());			

		//Array para retorno
		VirtualMachine[] virtualMachines = new VirtualMachine[listaVirtualMachines.size()];

		//Para controle do indice do array
		int indice = 0;

		//Coloca a lista interna no array de maquinas externas
		for( com.google.educloud.internal.entities.VirtualMachine vmInterna : listaVirtualMachines )
		{
			VirtualMachine vmRetorno = new VirtualMachine();
			vmRetorno.setId(vmInterna.getId());
			vmRetorno.setName(vmInterna.getName());
			vmRetorno.setState(VMState.valueOf(vmInterna.getState().name()));
			Template template = vmInterna.getTemplate();
			com.google.educloud.api.entities.Template templateRetorno = new com.google.educloud.api.entities.Template();
			templateRetorno.setId(template.getId());
			templateRetorno.setName(template.getName());
			templateRetorno.setOsType(template.getOsType());
			vmRetorno.setTemplate(templateRetorno);
			virtualMachines[indice] = vmRetorno;

			indice++;
		}

		//Retorna o array de maquinas virtuais.
		return Response.ok(gson.toJson(virtualMachines), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * this method will return a virtual machine.
	 *
	 * @param machine
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get/{id: [0-9]*}")
	public Response getVM(@PathParam("id") String vmId) {

		int id = Integer.valueOf(vmId);

		com.google.educloud.internal.entities.VirtualMachine vm = VirtualMachineDao.getInstance().findById(id);

		HttpSession session = request.getSession();
		CloudSession cloudSession = (CloudSession)session.getAttribute(CloudSession.HTTP_ATTR_NAME);

		/* validations */
		if (vm == null) {
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-303");
			error.setHint("Set virtual machine id and try again");
			error.setText("Apparently you are trying to get a nonexistent virtual machine");

			// return error message
			return Response.status(400).entity(gson.toJson(error)).build();
		}

		if (cloudSession.getUser().getType() != User.UserType.ADMIN && vm.getUserId() != cloudSession.getUser().getId()) {
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-500");
			error.setHint("");
			error.setText("You do not have permission to access this feature");

			// return error message
			return Response.status(400).entity(gson.toJson(error)).build();
		}

		com.google.educloud.api.entities.Template template = new com.google.educloud.api.entities.Template();
		template.setId(vm.getTemplate().getId());
		template.setName(vm.getTemplate().getName());
		template.setOsType(vm.getTemplate().getOsType());
 
		VirtualMachine virtualMachine = new VirtualMachine();
		virtualMachine.setId(id);
		virtualMachine.setName(vm.getName());
		virtualMachine.setState(VMState.valueOf(vm.getState().name()));
		virtualMachine.setTemplate(template);

		// return virtual machine
		return Response.ok(gson.toJson(virtualMachine), MediaType.APPLICATION_JSON).build();
	}

}
