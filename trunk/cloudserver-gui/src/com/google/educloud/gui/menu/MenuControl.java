package com.google.educloud.gui.menu;

public class MenuControl {

	private String urlPath;

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public String getCSSMenuClass(String page) {
		if ("/ui/vms.jsp".equals(this.urlPath) || "/ui/newvm.jsp".equals(this.urlPath)) {
			if (page.equals("vms")) {
				return "selected";
			}
		} else if ("/ui/nodes.jsp".equals(this.urlPath)) {
			if (page.equals("nodes")) {
				return "selected";
			}
		} else if ("/ui/templates.jsp".equals(this.urlPath) || "/ui/newtemplate.jsp".equals(this.urlPath)) {
			if (page.equals("templates")) {
				return "selected";
			}
		} else if ("/ui/users.jsp".equals(this.urlPath) || "newuser.jsp".equals(this.urlPath)) {
			if (page.equals("users")) {
				return "selected";
			}
		} else if ("/ui/index.jsp".equals(this.urlPath)) {
			if (page.equals("home")) {
				return "selected";
			}
		}

		return "";
	}

	public String getCSSSubMenuClass(String page) {
		if ("/ui/vms.jsp".equals(this.urlPath) || "/ui/newvm.jsp".equals(this.urlPath)) {
			if (page.equals("vms")) {
				return "block";
			} else {
				return "none";
			}
		} else if ("/ui/nodes.jsp".equals(this.urlPath)) {
			if (page.equals("nodes")) {
				return "block";
			} else {
				return "none";
			}
		} else if ("/ui/templates.jsp".equals(this.urlPath) || "/ui/newtemplate.jsp".equals(this.urlPath)) {
			if (page.equals("templates")) {
				return "block";
			} else {
				return "none";
			}
		} else if ("/ui/users.jsp".equals(this.urlPath) || "newuser.jsp".equals(this.urlPath)) {
			if (page.equals("users")) {
				return "block";
			} else {
				return "none";
			}
		} else if ("/ui/index.jsp".equals(this.urlPath)) {
			if (page.equals("home")) {
				return "block";
			} else {
				return "none";
			}
		}

		return "none";

	}
}
