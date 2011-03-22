package com.google.educloud.cloudserver.rs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.educloud.api.entities.EduCloudErrorMessage;
import com.google.educloud.cloudserver.database.dao.SessionDao;
import com.google.educloud.cloudserver.database.dao.UserDao;
import com.google.educloud.cloudserver.entity.CloudSession;
import com.google.educloud.cloudserver.entity.User;
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

		return Response.ok().build();
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

}
