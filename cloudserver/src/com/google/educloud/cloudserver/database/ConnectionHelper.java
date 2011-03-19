package com.google.educloud.cloudserver.database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class ConnectionHelper {

	private static Logger LOG = Logger.getLogger(ConnectionHelper.class);

	private static Connection connection;

	private static final String url = "jdbc:hsqldb:data/educloud;shutdown=true";

	public static Connection getConnection() {
		if (null == connection) {
			LOG.debug("creating new database connection");
			try {
				Class.forName("org.hsqldb.jdbcDriver").newInstance();
				connection = DriverManager.getConnection(url, "SA", "");
				connection.setAutoCommit(false);
			} catch (InstantiationException e) {
				LOG.debug("Error on get hsql driver", e);
			} catch (IllegalAccessException e) {
				LOG.debug("Error on get hsql driver", e);
			} catch (ClassNotFoundException e) {
				LOG.debug("Error on get hsql driver", e);
			} catch (SQLException e) {
				LOG.debug("Error on get hsql connection", e);
			}

			if (null != connection) {
				try {
					PreparedStatement ps = connection.prepareStatement("SELECT COUNT(1) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA = 'PUBLIC'");
					ResultSet rs = ps.executeQuery();

					if (rs.next()) {
						int tableCount = rs.getInt(1);
						if (tableCount < 1) {
							LOG.debug("Educloud not found, will create empty database");
							createDefaultSchema();
						}
					}

				} catch (SQLException e) {
					LOG.debug("Error on check schema", e);
				}
			}
		}

		return connection;
	}

	private static void createDefaultSchema() {
		ScriptRunner runner = new ScriptRunner(connection);
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader("scripts/create_empty_database.sql"));
			runner.runScript(in);
			connection.commit();
		} catch (FileNotFoundException e) {
			LOG.error(e);
		} catch (IOException e) {
			LOG.error(e);
		} catch (SQLException e) {
			LOG.error(e);
		}
	}
}
