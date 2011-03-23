<jsp:useBean id="menuControl" scope="request"
	class="com.google.educloud.gui.menu.MenuControl" />
<style>
<jsp:setProperty property="urlPath" name="menuControl" value="<%=request.getRequestURI()%>"/>
* {
margin: 0px;
padding: 0px;
}
body {
  font: 12px Arial;
}
h1 {font: 60px Arial;padding: 10px;}
#container {}
#header {}
#footer {}
#tabMenu div { text-align:left; }
#tabMenu ul { background: #d6d6d6; height: 32px; border-bottom: 1px solid #333;}
#tabMenu ul li a.selected {
  background: #ebebeb;
  padding-top: 6px;
  padding-bottom: 11px;
  padding-left: 10px;
  padding-right: 10px;
  border-top: 1px solid #333;
  border-left: 1px solid #333;
  border-right: 1px solid #333;
}
#tabMenu ul a { color: #333333;}
#tabMenu li { 
  line-height: 2.5;
  padding: 10px;
  display: inline;
  list-style-type: none;
}
#tabMenu .submenu { 
  line-height: 2.5;
  display: block;
  list-style-type: none;
  border-bottom: 1px solid #333;
  background: #ebebeb;
  padding-left: 10px;
}
</style>
<div id="header">
  <h1>educloud</h1>
  <div id="tabMenu">
    <ul>
    	<li><a href="index.jsp" class="<%=menuControl.getCSSMenuClass("home") %>">EduCloud Home</a></li>
    	<li><a href="vms.jsp" class="<%=menuControl.getCSSMenuClass("vms") %>">Virtual Machines</a></li>
    	<li><a href="resources.jsp" class="<%=menuControl.getCSSMenuClass("nodes") %>">Cloud Resources</a></li>
      <li><a href="templates.jsp" class="<%=menuControl.getCSSMenuClass("templates") %>">Templates</a></li>
      <li><a href="users.jsp"" class="<%=menuControl.getCSSMenuClass("users") %>">Users</a></li>
    </ul>
    <div id="tab1" class="submenu" style="display: <%=menuControl.getCSSSubMenuClass("home") %>;">Dashboard</div>
    <div id="tab2" class="submenu" style="display: <%=menuControl.getCSSSubMenuClass("vms") %>;">New Virtual Machine</div>
    <div id="tab3" class="submenu" style="display: <%=menuControl.getCSSSubMenuClass("nodes") %>;">All resources</div>
    <div id="tab4" class="submenu" style="display: <%=menuControl.getCSSSubMenuClass("templates") %>;">New Template</div>
    <div id="tab5" class="submenu" style="display: <%=menuControl.getCSSSubMenuClass("users") %>;">New user</div>
  </div>
</div>
