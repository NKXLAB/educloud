package com.google.educloud.cloudserver.internalrs;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.database.dao.NodeDao;
import com.google.educloud.internal.entities.Node;
import com.google.gson.Gson;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/node")
public class NodeRegisterRest {

	private static Gson gson = new Gson();

	private static Logger LOG = Logger.getLogger(NodeRegisterRest.class);

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/resgister")
	public Response register(String jsonNode) {

		LOG.debug("Cloud server will register a new node");

		Node node = gson.fromJson(jsonNode, Node.class);

		NodeDao.getInstance().insert(node);

		jsonNode = gson.toJson(node);

		return Response.ok(jsonNode, MediaType.APPLICATION_JSON).build();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/unresgister")
	public Response unregister(String jsonNode) {

		Node node = gson.fromJson(jsonNode, Node.class);

		LOG.debug("Cloud server will unregister node #" + node.getId());

		NodeDao.getInstance().remove(node);

		return Response.ok(gson.toJson(true), MediaType.APPLICATION_JSON).build();
	}

}