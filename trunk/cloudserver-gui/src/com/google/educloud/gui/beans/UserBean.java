package com.google.educloud.gui.beans;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.google.educloud.api.EduCloudAuthorization;
import com.google.educloud.api.EduCloudFactory;
import com.google.educloud.api.clients.EduCloudUserClient;
import com.google.educloud.api.entities.User;
import com.google.educloud.api.entities.User.UserType;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;

public class UserBean {

	private String login;

	private String name;

	private String password;

	private String type;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void createUser(HttpSession session) throws EduCloudServerException {
		EduCloudAuthorization auth = (EduCloudAuthorization)session.getAttribute("CLOUD_AUTHENTICATION");
		EduCloudUserClient userClient = EduCloudFactory.createUserClient(auth);

		User user = new User();
		user.setLogin(login);
		user.setName(name);
		user.setPassword(password);
		user.setType(UserType.valueOf(type));

		userClient.createUser(user);
	}

	public List<User> getUsers(HttpSession session) throws EduCloudServerException {
		EduCloudAuthorization auth = (EduCloudAuthorization)session.getAttribute("CLOUD_AUTHENTICATION");
		EduCloudUserClient userClient = EduCloudFactory.createUserClient(auth);

		return userClient.describeUsers();
	}
}
