package com.google.educloud.cloudserver.selector;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.configuration.ServerConfig;

/**
 * Classe para conter os metodos compartilhados entre os gerenciadores de nodos.
 */
public class NodeSelectorManager {

	private static Logger LOG = Logger.getLogger(NodeSelectorManager.class);

	private static INodeSelector nodeSelector;

	// Cria a instancia do seletor de nodos de acordo com a configuração do
	// server.
	synchronized public static INodeSelector getSelector() {

		if (nodeSelector == null) {

			String police = ServerConfig.getPolice();

			Class<INodeSelector> cls;
			try {
				LOG.debug("Creating class "+police+" to control allocation.");
				cls = (Class<INodeSelector>) Class.forName(police);
				Object arglist[] = new Object[0];
				nodeSelector = cls.newInstance();
			} catch (ClassNotFoundException e) {
				LOG.error(e);
			} catch (InstantiationException e) {
				LOG.error(e);
			} catch (IllegalAccessException e) {
				LOG.error(e);
			}
		}

		return nodeSelector;
	}

}
