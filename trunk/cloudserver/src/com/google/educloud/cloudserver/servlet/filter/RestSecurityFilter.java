package com.google.educloud.cloudserver.servlet.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.google.educloud.api.entities.EduCloudErrorMessage;
import com.google.educloud.cloudserver.database.dao.SessionDao;
import com.google.educloud.cloudserver.entity.CloudSession;
import com.google.gson.Gson;

public class RestSecurityFilter implements Filter {

	private static Logger LOG = Logger.getLogger(RestSecurityFilter.class);
	private static List<String> list;
	private Gson gson = new Gson();

	static {
		list = new ArrayList<String>();
		list.add("/application.wadl");
		list.add("/user/login");
	}

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		boolean allowedPublicRequest = false;
		boolean authenticated = false;

		String pathInfo = httpRequest.getPathInfo();

		if (list.contains(pathInfo)) {
			allowedPublicRequest = true;
		}

		HttpSession session = httpRequest.getSession(true);

		if (!allowedPublicRequest) {
			CloudSession cloudSession = (CloudSession)session.getAttribute(CloudSession.HTTP_ATTR_NAME);
			if (null == cloudSession) {
				// session expired
				EduCloudErrorMessage error = new EduCloudErrorMessage();
				error.setCode("CS-300");
				error.setHint("Create a new session and try again");
				error.setText("You need a valid session");

				LOG.debug("A client from '" + httpRequest.getRemoteAddr() + "' is try access resource '" + httpRequest.getPathInfo() + "' without start a session.");

				httpResponse.setContentType(MediaType.APPLICATION_JSON);
				httpResponse.getOutputStream().print(gson.toJson(error));
			} else {
				Date currentTime = Calendar.getInstance().getTime();

				long lastTimestamp = cloudSession.getLastUpdate().getTime();
				long currentTimestamp = currentTime.getTime();

				// 1 hour
				long expirationTime = 3600 * 1000;
				if ((currentTimestamp - lastTimestamp) > expirationTime) {
					// session expired
					EduCloudErrorMessage error = new EduCloudErrorMessage();
					error.setCode("CS-300");
					error.setHint("Create a new session");
					error.setText("Your session was expired");

					LOG.debug("Session '" + cloudSession.getId() + "' is expired");
					httpResponse.setContentType(MediaType.APPLICATION_JSON);
					httpResponse.getOutputStream().print(gson.toJson(error));
				} else {
					SessionDao.getInstance().updateLastUpdate(cloudSession);
					authenticated = true;
				}

			}
		}

		if (allowedPublicRequest || authenticated) {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {}

}
