package com.google.educloud.cloudserver.rs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.educloud.api.entities.EduCloudErrorMessage;
import com.google.educloud.api.entities.User.UserType;
import com.google.educloud.cloudserver.database.dao.SessionDao;
import com.google.educloud.cloudserver.database.dao.UserDao;
import com.google.educloud.cloudserver.entity.CloudSession;
import com.google.educloud.cloudserver.entity.User;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.spi.container.servlet.PerSession;

@PerSession
@Path("/user")
public class UserRest extends CloudResource {

	private static Logger LOG = Logger.getLogger(VMRest.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/login")
	public Response login(@QueryParam("login") String login, @QueryParam("pass") String pass) {

		User user = UserDao.getInstance().findByLogin(login);

		boolean validUser = false;

		if (null != user && user.getPass().equals(pass)) {
			validUser = true;
		}

		if (validUser) {
			CloudSession cloudSession = new CloudSession();
			cloudSession.setUser(user);

			SessionDao.getInstance().insert(cloudSession);

			request.getSession().setAttribute(CloudSession.HTTP_ATTR_NAME, cloudSession);

			LOG.debug("Created session for user '"+ user.getLogin() + "'");
		} else {
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-404");
			error.setHint("Set the correct credentials to start a new cloud session");
			error.setText("You are try start a new session to a invalid user");

			LOG.debug("Refuse login of user '"+ login + "'");

			return Response.status(400).entity(gson.toJson(error)).build();
		}

		com.google.educloud.api.entities.User extUser = new com.google.educloud.api.entities.User();
		extUser.setId(user.getId());
		extUser.setLogin(user.getLogin());
		extUser.setName(user.getName());
		extUser.setType(UserType.valueOf(user.getType().name()));

		return Response.ok(gson.toJson(user), MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/logout")
	public Response logout() {
		CloudSession cloudSession = (CloudSession)request.getSession().getAttribute(CloudSession.HTTP_ATTR_NAME);

		request.getSession().removeAttribute(CloudSession.HTTP_ATTR_NAME);

		LOG.debug("Destroy session '"+cloudSession.getId()+"' for user '"+ cloudSession.getUser().getLogin() +"'");

		SessionDao.getInstance().remove(cloudSession);

		return Response.ok().build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/describe")
	public Response listUsers() {
		CloudSession cloudSession = (CloudSession)request.getSession().getAttribute(CloudSession.HTTP_ATTR_NAME);

		if (cloudSession.getUser().getType() != User.UserType.ADMIN) {
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-530");
			error.setHint("");
			error.setText("You need be a admin user to access this feature");

			return Response.status(400).entity(gson.toJson(error)).build();
		}

		List<User> users = UserDao.getInstance().getAll();
		List<com.google.educloud.api.entities.User> extUsers = new ArrayList<com.google.educloud.api.entities.User>();

		for (User user : users) {
			com.google.educloud.api.entities.User extUser = new com.google.educloud.api.entities.User();
			extUser.setId(user.getId());
			extUser.setLogin(user.getLogin());
			extUser.setName(user.getName());
			extUser.setType(UserType.valueOf(user.getType().name()));
			extUsers.add(extUser);
		}

		return Response.ok(gson.toJson(extUsers), MediaType.APPLICATION_JSON).build();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/create")
	public Response createUser(String jsonUser) {
		CloudSession cloudSession = (CloudSession)request.getSession().getAttribute(CloudSession.HTTP_ATTR_NAME);

		if (cloudSession.getUser().getType() != User.UserType.ADMIN) {
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-530");
			error.setHint("");
			error.setText("You need be a admin user to access this feature");

			return Response.status(400).entity(gson.toJson(error)).build();
		}

		com.google.educloud.api.entities.User extUser = gson.fromJson(jsonUser, com.google.educloud.api.entities.User.class);

		User user = new User();
		user.setLogin(extUser.getLogin());
		user.setName(extUser.getName());
		user.setType(com.google.educloud.cloudserver.entity.User.UserType.valueOf(extUser.getType().name()));
		user.setPass(extUser.getPassword());

		UserDao.getInstance().insert(user);

		extUser.setId(user.getId());

		return Response.ok(gson.toJson(extUser), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * this method will delete users
	 *
	 * @return
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/delete")
	public Response deleteUsers(String jsonUsers) {

		CloudSession cloudSession = (CloudSession)request.getSession().getAttribute(CloudSession.HTTP_ATTR_NAME);

		if (cloudSession.getUser().getType() != User.UserType.ADMIN) {
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-530");
			error.setHint("");
			error.setText("You need be a admin user to access this feature");

			return Response.status(400).entity(gson.toJson(error)).build();
		}

		Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
		List<Integer> users = gson.fromJson(jsonUsers, type);

		for (Integer userId : users) {
			UserDao.getInstance().remove(userId);
		}

		return Response.ok().build();
	}
}
