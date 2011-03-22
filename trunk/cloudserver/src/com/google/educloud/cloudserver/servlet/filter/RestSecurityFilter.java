package com.google.educloud.cloudserver.servlet.filter;

import java.io.IOException;
import java.util.ArrayList;
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

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.entity.CloudSession;

public class RestSecurityFilter implements Filter {

	private static Logger LOG = Logger.getLogger(RestSecurityFilter.class);
	private List<String> list;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		LOG.debug("will filter request from: " + request.getRemoteHost());

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		boolean allowedPublicRequest = false;
		boolean authenticated = false;

		String pathInfo = httpRequest.getPathInfo();

		if (list.contains(pathInfo)) {
			allowedPublicRequest = true;
		}

		HttpSession session = httpRequest.getSession(true);
		LOG.debug("sessionId: " + session.getId());

		if (!allowedPublicRequest) {
			CloudSession cloudSession = (CloudSession)session.getAttribute(CloudSession.HTTP_ATTR_NAME);
			if (null == cloudSession) {
				LOG.debug("user not authorized");
				httpResponse.getOutputStream().print("nao autenticado");
			} else {
				LOG.debug("user authorized");
				authenticated = true;
			}
		}

		if (allowedPublicRequest || authenticated) {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		list = new ArrayList<String>();
		list.add("/application.wadl");
		list.add("/user/login");
	}

}
