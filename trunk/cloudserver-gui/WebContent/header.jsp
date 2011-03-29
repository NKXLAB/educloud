<%@page import="com.google.educloud.api.entities.User.UserType"%>
<%@page import="com.google.educloud.api.EduCloudAuthorization"%>
<jsp:useBean id="menuControl" scope="request"
	class="com.google.educloud.gui.menu.MenuControl" />
<jsp:setProperty property="urlPath" name="menuControl"
	value="<%=request.getRequestURI()%>" />
<%
	EduCloudAuthorization auth = (EduCloudAuthorization)session.getAttribute("CLOUD_AUTHENTICATION");
%>
<div id="header">
<h1>educloud</h1>
<div id="tabMenu">
<ul>
	<li><a href="index.jsp"
		class="<%=menuControl.getCSSMenuClass("home")%>">EduCloud Home</a></li>
	<li><a href="vms.jsp"
		class="<%=menuControl.getCSSMenuClass("vms")%>">Virtual Machines</a></li>
	<li><a href="resources.jsp"
		class="<%=menuControl.getCSSMenuClass("nodes")%>">Cloud Resources</a></li>
	<li><a href="templates.jsp"
		class="<%=menuControl.getCSSMenuClass("templates")%>">Templates</a></li>
	<%
		if (auth.getUser().getType() == UserType.ADMIN) {
	%>
	<li><a href="users.jsp"
		class="<%=menuControl.getCSSMenuClass("users")%>">Users</a></li>
	<%
		}
	%>
</ul>
<div id="tab1" class="submenu"
	style="display: <%=menuControl.getCSSSubMenuClass("home")%>;">Dashboard</div>
<div id="tab2" class="submenu"
	style="display: <%=menuControl.getCSSSubMenuClass("vms")%>;"><a
	href="newvm.jsp">New Virtual Machine</a></div>
<div id="tab3" class="submenu"
	style="display: <%=menuControl.getCSSSubMenuClass("nodes")%>;">All
resources</div>
<div id="tab4" class="submenu"
	style="display: <%=menuControl.getCSSSubMenuClass("templates")%>;"><a
	href="newtemplate.jsp">New Template</a></div>
	<%
		if (auth.getUser().getType() == UserType.ADMIN) {
	%>
<div id="tab5" class="submenu"
	style="display: <%=menuControl.getCSSSubMenuClass("users")%>;"><a
	href="newuser.jsp">New user</a></div>
	<%
		}
	%>
</div>
</div>
