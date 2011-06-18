package com.google.educloud.cloudnode.util;

import java.io.IOException;

public class OsUtil {

	public static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("win") >= 0);
	}

	public static boolean isLinux() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
	}

	public static void runScript(String command) throws IOException, InterruptedException {
		Runtime rtime = Runtime.getRuntime();

		if (isLinux()) {
			command = "/bin/bash " + command;
		}

		Process exec = rtime.exec(command);
		exec.waitFor();
	}
}
