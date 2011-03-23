<%@page import="com.google.educloud.api.entities.Node"%>
<%@page import="com.google.educloud.api.entities.VirtualMachine"%>
<%@page import="com.google.educloud.api.clients.EduCloudVMClient"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.google.educloud.api.entities.Template"%>
<%@page import="java.util.List"%>
<%@page import="com.google.educloud.api.EduCloudFactory"%>
<jsp:useBean id="cloudBean" scope="session"
	class="com.google.educloud.gui.beans.CloudAPIBean" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"></meta>
<title>EduCloud</title>
<jsp:include page="scripts.jsp" />
</head>
<body>
<div id="container"><jsp:include page="header.jsp" />
<style>
#side {
	float: left;
	width: 240px;
	height: 340px;
	padding: 20px 0;
	margin: 0 20px 0 0;
	display: inline;
	background-color: #ccc;
	overflow: auto;
}
#content #vmDetails {float: left}
</style>
<div id="content">
	<div id="side">
		<%
			EduCloudVMClient vmClient = cloudBean.getVMClient();
			List<VirtualMachine> vms = vmClient.describeInstances();
	
			for (VirtualMachine vm : vms) {
				Template tpl = vm.getTemplate();
		%>
		<div>
			<%=vm.getId()%>
			<%=vm.getName()%>
			<%=vm.getState()%>
			<%=tpl.getName()%>
		</div>
		<%
			}
		%>
	</div>
	<div id="vmDetails">
		Details of VM
	</div>
</div>
<jsp:include page="footer.jsp" />
</div>
</body>
</html>
