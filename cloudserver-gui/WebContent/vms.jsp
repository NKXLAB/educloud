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
<script type="text/javascript" src="js/vms.js"></script>
</head>
<body>
<div id="container"><jsp:include page="header.jsp" />
<style>
#side {
	float: left;
	width: 240px;
	height: 340px;
	padding: 10px 0;
	margin: 0 20px 0 0;
	display: inline;
	background-color: #ccc;
	overflow: auto;
}
#content #vmDetails {float: left}

#selectable .selected {
	background: #ebebeb; 
	border-bottom: 1px solid #333;
	border-top: 1px solid #333;
	border-left: 1px solid #333;
	border-right: 1px solid #333;
}
#selectable .unselected { background: #f5f5f5; color: black; }
#selectable { list-style-type: none; margin: 0; padding: 0; }
#selectable .vmitem { margin: 10px; padding: 10px; border: 1px solid #333; cursor: pointer; }
#selectable .vmitem:hover { margin: 10px; padding: 10px; background: #333; color: white; border: 1px solid #333; }
#vmDetails { margin-top: 10px; }
</style>
<div id="content">
	<div id="side">
		<ol id="selectable">
		<%
			EduCloudVMClient vmClient = cloudBean.getVMClient();
			List<VirtualMachine> vms = vmClient.describeInstances();
			int i = 0;
			String clazz = "unselected";
			int firstId = 0;
			for (VirtualMachine vm : vms) {
				clazz = i == 0 ? "selected" : "unselected";
				if (i == 0) { firstId = vm.getId(); }
				Template tpl = vm.getTemplate();
		%>
		<li onclick="selectMachine(<%=vm.getId()%>);" id="vmItem_<%=vm.getId()%>" class="vmitem <%=clazz%>"><div><%=vm.getName()%></div></li>
		<%
			i++;
			}
		%>
		</ol>
	</div>
	<div id="vmDetails">
	</div>
</div>
<script> $(document).ready(function() {selectMachine(<%=firstId%>);}); </script>
<jsp:include page="footer.jsp" />
</div>
</body>
</html>
