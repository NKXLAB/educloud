package com.google.educloud.gui.beans;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

public class CloudUtilsBean {

	public List<String> getOsTypes(HttpSession session) {
		List<String> list = new ArrayList<String>();

		list.add("Ubuntu");
		list.add("WindowsXP");

		// TODO add logic to return all supported templates by virtual box

		return list;
	}
}
