<%@page 
	language="java" 
	contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="java.util.List"
%>
<jsp:useBean id="cloudUtilsBean" class="com.google.educloud.gui.beans.CloudUtilsBean" />
<jsp:useBean id="templateBean" class="com.google.educloud.gui.beans.TemplateBean" />
<jsp:setProperty name="templateBean" property="*"/>
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
		templateBean.createTemplate(session);
		response.sendRedirect("templates.jsp");
	}
%>
<form name="createTemplateForm" action="newtemplate.jsp" method="post">
	<fieldset> 
	
		<legend>Create new cloud template</legend>
		
		<label for="login">Name:</label> 
		<input type="text" id="name" name="name" value=""/> 
		<div class="clear"></div> 
		
		<label for="description">Description:</label>
		<textarea rows="5" cols="60" id="description" name="description"></textarea>
		<div class="clear"></div>
		
		<label for="filename">Filename:</label>
		<input type="text" id="filename" name="filename" value=""/> 
		<div class="clear"></div>
		
		<label for="size">File size:</label>
		<input type="text" id="size" name="size" value=""/> 
		<div class="clear"></div>

		<label for="ostype">OS Type:</label> 
		<select id="ostype" name="ostype">
			<%
			List<String> list = cloudUtilsBean.getOsTypes(session);
			
			for (String osType : list) {
			%>
				<option value="<%=osType%>"><%=osType%></option>
			<%
			}
			%>
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
