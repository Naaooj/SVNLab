<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
	<title>${projectName}</title>
</head>
<body>
	<sec:authorize access="isAuthenticated()">
		<div>
			<c:choose>
				<c:when test="${empty projects}">
					No project found in repository
				</c:when>
				<c:otherwise>
					<div class="area">
						<div class="areaContent">
							List of projects found : 
							<c:forEach items="${projects}" var="project">
								<div><a href="<c:url value="project/${project.name}" />">${project.name}</a></div>
							</c:forEach>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</sec:authorize>
</body>
</html>