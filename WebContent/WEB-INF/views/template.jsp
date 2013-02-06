<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<html>
<head>
	<title>SVNLab</title>
	<script type="text/javascript" src="<c:url value="/resources/jquery-1.9.0.min.js" /> "></script>
	<link href="<c:url value="/resources/svnlab.css" />" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="header">
		<div id="headerTitle">
			<div>
				SVNLab
				<img id="loader" alt="loading" src="<c:url value="/resources/loader.gif" />" />
				<sec:authorize access="isAuthenticated()">
					<div id="logout"><a href="<c:url value="/j_spring_security_logout" />">Logout</a></div>
				</sec:authorize>
			</div>
		</div>
	</div>
	<div id="mainContainer">
		<div id="mainContent">
			<tiles:insertAttribute name="body"/>
		</div>
	</div>
</body>
</html>