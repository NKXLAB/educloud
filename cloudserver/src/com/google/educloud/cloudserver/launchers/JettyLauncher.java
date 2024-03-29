package com.google.educloud.cloudserver.launchers;

import java.net.URL;

import org.apache.log4j.Logger;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.FilterHolder;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.thread.QueuedThreadPool;

import com.google.educloud.cloudserver.configuration.ServerConfig;
import com.google.educloud.cloudserver.servlet.filter.RestSecurityFilter;
import com.sun.jersey.spi.container.servlet.ServletContainer;

/**
 * Start a instance of Jetty web server
 *
 * @author TremperD
 */
public class JettyLauncher {

	private static Logger LOG = Logger.getLogger(JettyLauncher.class);

	public static void main(String[] args) {
		/* start jetty configuration */
		Server server = new Server();

		int port = ServerConfig.getServerPort();

		LOG.info("Configurating jetty webserver");

		/* configure connectors */
		SelectChannelConnector cConnector = new SelectChannelConnector();
		cConnector.setPort(port);
		cConnector.setMaxIdleTime(30000);
		cConnector.setAcceptors(2);
		cConnector.setStatsOn(false);
		cConnector.setLowResourcesConnections(5000);
		cConnector.setConfidentialPort(8443);
		Connector[] connectors = {cConnector};
		server.setConnectors(connectors);

		/* configure tread pool */
		QueuedThreadPool queuedThreadPool = new QueuedThreadPool();
		queuedThreadPool.setMinThreads(10);
		queuedThreadPool.setMaxThreads(200);
		queuedThreadPool.setLowThreads(20);
		queuedThreadPool.setSpawnOrShrinkAt(2);
		server.setThreadPool(queuedThreadPool);

		/* configure gui application */
		String warUrlString;
		if (!ServerConfig.useUIExpanded()) {
	        final URL warUrl = JettyLauncher.class.getClassLoader().getResource("lib/educloud-gui.war");
	        warUrlString = warUrl.toExternalForm();
		} else {
	        warUrlString = ServerConfig.getExpandedUI();
		}

        WebAppContext webAppContext = new WebAppContext(warUrlString, "/ui");
        webAppContext.getInitParams().put("useFileMappedBuffer", false);
		server.setHandler(webAppContext);

        /* configure external restfull services */
		ServletHolder sh = new ServletHolder(ServletContainer.class);
		sh.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
		sh.setInitParameter("com.sun.jersey.config.property.packages", "com.google.educloud.cloudserver.rs");

		Context context = new Context(server, "/rs", Context.SESSIONS);

		FilterHolder filterHolder = new FilterHolder(RestSecurityFilter.class);
		context.addFilter(filterHolder, "/*", org.mortbay.jetty.Handler.DEFAULT);
		context.addServlet(sh, "/*");

		/* configure internal restfull services */
		sh = new ServletHolder(ServletContainer.class);
		sh.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
		sh.setInitParameter("com.sun.jersey.config.property.packages", "com.google.educloud.cloudserver.internalrs");
		context = new Context(server, "/internalrs", Context.NO_SESSIONS);
		context.addServlet(sh, "/*");

		try {
			LOG.info("Application will be started...");
			/* start jetty */
			server.start();
			LOG.info("Waiting requisitions... \\0/");
		} catch (Exception e) {
			LOG.error("unexpected error on start Jetty Server...");
			LOG.error("Exit with error -1");
			System.exit(-1);
		}

	}

}

