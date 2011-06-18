<%@page 
	language="java" 
	contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="java.util.List"
%>
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
		
		<label for="memorySize">Memory Size (in megabytes):</label>
		<input type="text" id="memorySize" name="memorySize" value=""/> 
		<div class="clear"></div>
		
		<label for="ostype">OS Type:</label> 
		<select id="ostype" name="ostype">
			<option value="Other">Other/Unknown</option>
			<option value="Windows31">Windows 3.1</option>
			<option value="Windows95">Windows 95</option>
			<option value="Windows98">Windows 98</option>
			<option value="WindowsMe">Windows Me</option>
			<option value="WindowsNT4">Windows NT 4</option>
			<option value="Windows2000">Windows 2000</option>
			<option value="WindowsXP">Windows XP</option>
			<option value="WindowsXP_64">Windows XP (64 bit)</option>
			<option value="Windows2003">Windows 2003</option>
			<option value="Windows2003_64">Windows 2003 (64 bit)</option>
			<option value="WindowsVista">Windows Vista</option>
			<option value="WindowsVista_64">Windows Vista (64 bit)</option>
			<option value="Windows2008">Windows 2008</option>
			<option value="Windows2008_64">Windows 2008 (64 bit)</option>
			<option value="Windows7">Windows 7</option>
			<option value="Windows7_64">Windows 7 (64 bit)</option>
			<option value="WindowsNT">Other Windows</option>
			<option value="Linux22">Linux 2.2</option>
			<option value="Linux24">Linux 2.4</option>
			<option value="Linux24_64">Linux 2.4 (64 bit)</option>
			<option value="Linux26">Linux 2.6</option>
			<option value="Linux26_64">Linux 2.6 (64 bit)</option>
			<option value="ArchLinux">Arch Linux</option>
			<option value="ArchLinux_64">Arch Linux (64 bit)</option>
			<option value="Debian">Debian</option>
			<option value="Debian_64">Debian (64 bit)</option>
			<option value="OpenSUSE">openSUSE</option>
			<option value="OpenSUSE_64">openSUSE (64 bit)</option>
			<option value="Fedora">Fedora</option>
			<option value="Fedora_64">Fedora (64 bit)</option>
			<option value="Gentoo">Gentoo</option>
			<option value="Gentoo_64">Gentoo (64 bit)</option>
			<option value="Mandriva">Mandriva</option>
			<option value="Mandriva_64">Mandriva (64 bit)</option>
			<option value="RedHat">Red Hat</option>
			<option value="RedHat_64">Red Hat (64 bit)</option>
			<option value="Turbolinux">Turbolinux</option>
			<option value="Turbolinux">Turbolinux (64 bit)</option>
			<option value="Ubuntu">Ubuntu</option>
			<option value="Ubuntu_64">Ubuntu (64 bit)</option>
			<option value="Xandros">Xandros</option>
			<option value="Xandros_64">Xandros (64 bit)</option>
			<option value="Oracle">Oracle</option>
			<option value="Oracle_64">Oracle (64 bit)</option>
			<option value="Linux">Other Linux</option>
			<option value="Solaris">Oracle Solaris 10 5/09 and earlier</option>
			<option value="Solaris_64">Oracle Solaris 10 5/09 and earlier (64 bit)</option>
			<option value="OpenSolaris">Oracle Solaris 10 10/09 and later</option>
			<option value="OpenSolaris_64">Oracle Solaris 10 10/09 and later (64 bit)</option>
			<option value="FreeBSD">FreeBSD</option>
			<option value="FreeBSD_64">FreeBSD (64 bit)</option>
			<option value="OpenBSD">OpenBSD</option>
			<option value="OpenBSD_64">OpenBSD (64 bit)</option>
			<option value="NetBSD">NetBSD</option>
			<option value="NetBSD_64">NetBSD (64 bit)</option>
			<option value="OS2Warp3">OS/2 Warp 3</option>
			<option value="OS2Warp4">OS/2 Warp 4</option>
			<option value="OS2Warp45">OS/2 Warp 4.5</option>
			<option value="OS2eCS">eComStation</option>
			<option value="OS2">Other OS/2</option>
			<option value="MacOS">Mac OS X Server</option>
			<option value="MacOS_64">Mac OS X Server (64 bit)</option>
			<option value="DOS">DOS</option>
			<option value="Netware">Netware</option>
			<option value="L4">L4</option>
			<option value="QNX">QNX</option>
			<option value="JRockitVE">JRockitVE</option>
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
