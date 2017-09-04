<%@page import="java.io.PrintWriter"%>
<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@ page import="org.apache.log4j.Logger" %>
<%	
	//记录日志
	Logger logger = Logger.getLogger("500.jsp");
	logger.error(exception.getMessage(), exception);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>500 - 系统内部错误</title>
</head>

<body>
	<h2>500 - 系统发生内部错误.</h2>
	<%
		out.println(exception.getMessage());
//		exception.printStackTrace(new PrintWriter(out));
	%>
</body>
</html>