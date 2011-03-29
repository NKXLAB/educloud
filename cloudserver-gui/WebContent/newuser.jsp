<%@page 
	language="java" 
	contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
%>
<jsp:useBean id="userBean" class="com.google.educloud.gui.beans.UserBean" />
<jsp:setProperty name="userBean" property="*"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"></meta>
<title>EduCloud</title>
<jsp:include page="scripts.jsp" />
</head>
<body>
<div id="container"><jsp:include page="header.jsp" />
<div id="content">
<%
	if (request.getMethod().equals("POST")) {
		userBean.createUser(session);
		response.sendRedirect("users.jsp");
	}
%>
<form name="createUserForm" action="newuser.jsp" method="post">
	<fieldset> 
	
		<legend>Create new user</legend>
		
		<label for="login">Name:</label> 
		<input type="text" id="name" name="name" value=""/> 
		<div class="clear"></div> 
		
		<label for="login">Login:</label>
		<input type="text" id="login" name="login" value=""/>
		<div class="clear"></div>
		
		<label for="password">Password:</label>
		<input type="password"" id="password" name="password" value=""/>
		<div class="clear"></div>
		
		<label for="type">Type:</label> 
		<select id="type" name="type">
			<option value="ADMIN">Admin</option>
			<option value="USER">User</option>
		</select>
		<div class="clear"></div> 
		 
		<br /> 
		
		<input type="submit" style="margin: 10px 0 0 125px;" class="button" name="commit" value="Create"/>	
	</fieldset> 
</form>
</div>
<jsp:include page="footer.jsp" />
</div>
</body>
</html>