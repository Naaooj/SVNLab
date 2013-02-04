<%@ page import="fr.free.naoj.svnlab.service.svn.Entry.Type"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<% pageContext.setAttribute("dir", Type.DIRECTORY); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Results</title>
</head>
<body>
	SVNLab
	
	<sec:authorize access="isAuthenticated()">
		<div><a href="<c:url value="/home" />">Home</a> | <a href="<c:url value="/j_spring_security_logout" />">Logout</a></div>
		
		<div>
		<c:if test="${!empty commits}">
			<c:forEach items="${commits}" var="commit">
				<div>${commit.title}</div>
			</c:forEach>
		</c:if>
		</div>
	</sec:authorize>

</body>
</html>