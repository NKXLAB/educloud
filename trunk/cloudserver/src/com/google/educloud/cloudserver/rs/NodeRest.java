package com.google.educloud.cloudserver.rs;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.educloud.api.entities.Node;
import com.google.educloud.cloudserver.database.dao.NodeDao;
import com.sun.jersey.spi.container.servlet.PerSession;

@PerSession
@Path("/node")
public class NodeRest extends CloudResource {

	private static Logger LOG = Logger.getLogger(NodeRest.class);

	/**
	 * this method will retrive all registered nodes
	 *
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/describeNodes")
	public Response describeNodes() {

		LOG.debug("Application will list all nodes");

		//Recupera a lista de nodos da base de dados.
		List<com.google.educloud.internal.entities.Node> listaNodos =
			NodeDao.getInstance().getAllOnline();

		//Array para retorno
		Node[] nodes = new Node[listaNodos.size()];

		//Para controle do indice do array
		int indice = 0;

		// build external virtual machine list
		for( com.google.educloud.internal.entities.Node nodoInterno : listaNodos )
		{
			Node nodoRetorno = new Node();
			nodoRetorno.setId(nodoInterno.getId());
			nodoRetorno.setHostname(nodoInterno.getHostname());
			nodoRetorno.setPort(nodoInterno.getPort());
			nodes[indice] = nodoRetorno;

			indice++;
		}

		//Retorna o array de nodos.
		return Response.ok(gson.toJson(nodes), MediaType.APPLICATION_JSON).build();
	}
}
