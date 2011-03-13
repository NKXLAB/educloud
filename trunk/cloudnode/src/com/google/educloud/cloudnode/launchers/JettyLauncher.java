package com.google.educloud.cloudnode.launchers;

import org.apache.log4j.Logger;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.thread.QueuedThreadPool;

import com.google.educloud.cloudnode.configuration.NodeConfig;
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

		int port = NodeConfig.getNodePort();

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

        /* configure external restfull services */
		ServletHolder sh = new ServletHolder(ServletContainer.class);
		sh.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
		sh.setInitParameter("com.sun.jersey.config.property.packages", "com.google.educloud.cloudnode.rs");
		Context context = new Context(server, "/rs", Context.SESSIONS);
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

