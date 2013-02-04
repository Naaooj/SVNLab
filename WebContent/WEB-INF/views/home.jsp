<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Home</title>
	
</head>
<body>
	SVNLab
	<sec:authorize access="isAuthenticated()">
		<div><a href="<c:url value="/j_spring_security_logout" />">Logout</a></div>
		
		<div>
			<c:choose>
				<c:when test="${empty projects}">
					No project found in repository
				</c:when>
				<c:otherwise>
					List of projects found : 
					<c:forEach items="${projects}" var="project">
						<div><a href="<c:url value="project/${project.name}" />">${project.name}</a></div>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</div>
	</sec:authorize>
</body>
</html>