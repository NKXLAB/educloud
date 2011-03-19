package com.google.educloud.cloudserver.database;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * Tool to run database scripts
 */
public class ScriptRunner {

	private static Logger LOG = Logger.getLogger(ScriptRunner.class);

    private Connection connection;

    public ScriptRunner(Connection connection) {
        this .connection = connection;
    }

    public void runScript(Reader reader) throws IOException, SQLException {
        StringBuffer command = null;
        try {
            LineNumberReader lineReader = new LineNumberReader(reader);
            String line = null;
            while ((line = lineReader.readLine()) != null) {
                if (command == null) {
                    command = new StringBuffer();
                }
                String trimmedLine = line.trim();
                if (trimmedLine.startsWith("--")) {
                    LOG.debug(trimmedLine);
                } else if (trimmedLine.length() < 1
                        || trimmedLine.startsWith("//")) {
                    //Do nothing
                } else if (trimmedLine.length() < 1
                        || trimmedLine.startsWith("--")) {
                    //Do nothing
                } else if (trimmedLine.endsWith(";")) {
                    command.append(line.substring(0, line
                            .lastIndexOf(";")));
                    command.append(" ");
                    Statement statement = connection.createStatement();

                    LOG.debug(command);

                    try {
                        statement.execute(command.toString());
                    } catch (SQLException e) {
                        e.fillInStackTrace();
                        LOG.error("Error executing: " + command, e);
                        throw e;
                    }

                    command = null;
                    try {
                        statement.close();
                    } catch (Exception e) {}
                } else {
                    command.append(line);
                    command.append(" ");
                }
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
            LOG.error(e);
            throw e;
        } catch (IOException e) {
            e.fillInStackTrace();
            LOG.error(e);
            throw e;
        }
    }

}