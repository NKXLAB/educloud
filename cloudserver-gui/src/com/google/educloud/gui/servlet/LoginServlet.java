package com.google.educloud.gui.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.educloud.api.EduCloudAuthorization;
import com.google.educloud.api.EduCloudConfig;
import com.google.educloud.api.EduCloudFactory;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 2030542145114755912L;

	private static Logger LOG = Logger.getLogger(LoginServlet.class);

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {

		String login = request.getParameter("login");
		String password = request.getParameter("password");

		// TODO implements host configuration
		EduCloudConfig config = new EduCloudConfig();
		config.setHost("localhost");
		config.setPort(8000);
		config.setLogin(login);
		config.setPass(password);

		EduCloudAuthorization auth = null;

		try {
			auth = EduCloudFactory.createAuthorization(config);
			HttpSession session = request.getSession();
			session.setAttribute("CLOUD_AUTHENTICATION", auth);
			session.removeAttribute("CLOUD_AUTHENTICATION_ERROR");

			String redirect = request.getParameter("redirect");

			if (null == redirect) {
				redirect = "index.jsp";
			}

			response.sendRedirect(redirect);
		} catch (EduCloudServerException e) {
			LOG.error("Error on get client connection", e);
			HttpSession session = request.getSession();
			session.setAttribute("CLOUD_AUTHENTICATION_ERROR", e.getErrorMessage());

			try {
				response.sendRedirect("login.jsp");
				return;
			} catch (IOException e1) {
				LOG.debug(e);
				throw new ServletException(e);
			}
		} catch (IOException e) {
			LOG.debug(e);
			throw new ServletException(e);
		}
	}
}
