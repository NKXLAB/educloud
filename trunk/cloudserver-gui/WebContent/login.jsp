<%@page import="com.google.educloud.api.entities.EduCloudErrorMessage"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"></meta>
<title>EduCloud</title>
<style>
*					{ margin: 0; padding: 0; }
body				{ font-family: Arial; background: #c4c4c4; color: #3a3a3a;  }

.clear				{ clear: both; }

form				{ width: 406px; margin: 170px auto 0; }

legend				{ display: none; }

fieldset			{ border: 0; }

label				{ width: 115px; text-align: right; float: left; margin: 0 10px 0 0; padding: 9px 0 0 0; font-size: 16px; }

input				{ width: 220px; display: block; padding: 4px; margin: 0 0 10px 0; font-size: 18px; color: #3a3a3a; font-family: Arial;}
.button				{ repeat-x top center; border: 1px solid #999;
					  -moz-border-radius: 5px; padding: 5px; color: black; font-weight: bold;
					  -webkit-border-radius: 5px; font-size: 13px;  width: 70px; }
.button:hover		{ background: white; color: black; }
</style>
</head>
<body>
<div id="container">
	<form id="login-form" action="loginServlet" method="post"> 
<%

if (session.getAttribute("CLOUD_AUTHENTICATION_ERROR") != null) {
	EduCloudErrorMessage error = (EduCloudErrorMessage) session.getAttribute("CLOUD_AUTHENTICATION_ERROR");
	%>
		<div style="color: red;text-align:center;"><%=error.getText()%></div>
		<div class="clear"></div>
		<br /> 
	<% 
}
if (session.getAttribute("AUTHENTICATION_ERROR") != null) {
	String authError = (String) session.getAttribute("AUTHENTICATION_ERROR");
	%>
		<div style="color: red;text-align:center;"><%=authError%></div>
		<div class="clear"></div>
		<br /> 
	<%
}
%>
		<fieldset> 
		
			<legend>Log in</legend>
			
			<label for="login">username</label> 
			<input type="text" id="login" name="login"/> 
			<div class="clear"></div> 
			
			<label for="password">password</label> 
			<input type="password" id="password" name="password"/> 
			<input type="hidden" id="redirect" name="redirect" value="<%=request.getParameter("redirect")%>"/>
			<div class="clear"></div> 
			
			<br /> 
			
			<input type="submit" style="margin: -20px 0 0 287px;" class="button" name="commit" value="Log in"/>	
		</fieldset> 
	</form> 
</div>
</body>
</html>