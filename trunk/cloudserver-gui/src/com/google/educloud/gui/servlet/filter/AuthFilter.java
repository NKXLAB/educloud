package com.google.educloud.gui.servlet.filter;

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

public class AuthFilter implements Filter {

	private static List<String> whiteList;

	static {
		whiteList = new ArrayList<String>();
		whiteList.add("/login.jsp");
		whiteList.add("/loginServlet");
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		HttpSession session = request.getSession();
		String path = request.getServletPath();

		if (!whiteList.contains(path) && session.getAttribute("CLOUD_AUTHENTICATION") == null) {
			session.setAttribute("AUTHENTICATION_ERROR", "Invalid user credentials");
			response.sendRedirect("login.jsp?redirect=" + request.getRequestURI());
		} else {
			chain.doFilter(req, resp);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
