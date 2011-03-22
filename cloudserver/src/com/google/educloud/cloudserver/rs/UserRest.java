package com.google.educloud.cloudserver.rs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

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
	public Response login(String login, String pass) {
		User user = new User();
		user.setId(1);
		user.setLogin("dt");
		user.setPass("123");
		CloudSession cloudSession = new CloudSession();
		cloudSession.setId(1);
		cloudSession.setUser(user);
		request.getSession().setAttribute(CloudSession.HTTP_ATTR_NAME, cloudSession);

		LOG.debug("user "+ user.getLogin() +" logged");

		return Response.ok().build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/logout")
	public Response logout() {
		CloudSession cloudSession = (CloudSession)request.getSession().getAttribute(CloudSession.HTTP_ATTR_NAME);

		LOG.debug("user "+ cloudSession.getUser().getLogin() +" logoff");

		request.getSession().setAttribute(CloudSession.HTTP_ATTR_NAME, null);



		return Response.ok().build();
	}

}
