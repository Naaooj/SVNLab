<%@ page import="fr.free.naoj.svnlab.service.svn.Entry.Type"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<% pageContext.setAttribute("dir", Type.DIRECTORY); %>

<html>
<head>
	<title>Project</title>
</head>
<body>	
	<sec:authorize access="isAuthenticated()">
		<div><a href="<c:url value="/home" />" style="font-weight: bold;">Home</a></div>
		
		<div>
		<c:url var="searchUrl" value="/search/" />
		<form:form commandName="search" action="${searchUrl}" method="post">
			<form:hidden path="path" />
			<form:input id="description" path="description" /><input type="submit" value="Search" />
		</form:form>
		</div>
		
		<div id="projectContainer" class="area">
			<div id="path">
				<div style="margin-top: 3px; margin-left: 3px;">
				<c:forEach items="${paths}" var="p" varStatus="status">
					<c:set var="pathAsURL" value="${status.first ? '' : pathAsURL}${p}${!status.last ? '/' : ''}" />
					<c:url var="pathLink" value="${pathAsURL}" context="${pageContext.request.contextPath}/project" />
					<c:choose>
						<c:when test="${status.last}"><span id="last">${p}</span></c:when>
						<c:otherwise>
							<a href="${pathLink}">${p}</a><c:if test="${!status.last}"> / </c:if>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				</div>
			</div>
			<div class="areaContent" id="projectContent">
				<c:if test="${!empty entries}">
				<c:forEach items="${entries}" var="entry">
					<c:if test="${entry.type==dir}">
						<c:url var="link" value="${path}/${entry.name}" context="${pageContext.request.contextPath}/project" />
						<c:set var="viewLog" value="${path}/${entry.name}" />
						<div><a href="${link}">${entry.name}</a> | <a id="#${viewLog}" href="#viewLogs/${entry.name}" class="logs">view logs</a></div>
					</c:if>
				</c:forEach>
				</c:if>
			</div>
			<div id="logs" style="position: absolute;" class="areaContent"></div>
		</div>
		
		<script type="text/javascript">
			function getPadding() {
				var padding = $(".area").css("padding-left");
				padding = padding != null ? parseInt(padding) : 0;
				return padding;
			}
			
			function slide(result) {
				var padding = getPadding();
				var ec = $("#projectContent");
				var cw = parseInt(ec.css('width'));
				var el = $("#logs");
				
				if (result != null) {
					el.append(result);
				}
				ec.animate({
					left: parseInt(ec.css('left'), 10) == padding ? -ec.outerWidth() : padding
				}, {
					step: function(i, o) {
						var left = parseInt(ec.css('left')) + cw;
						el.css('left', left);
					}, 
					complete: function() {
						el.css('left', parseInt(ec.css('left')) + cw + 3*padding);
						if (result == null) {
							el.empty();
						}
					}
				});
			}
			
			function showLoader() {
				$("#loader").css("visibility", "visible");
			}
			
			function hideLoader() {
				$("#loader").css("visibility", "hidden");
			}
			
			function loadLogs(path) {
				showLoader();
				var req = "${pageContext.request.contextPath}/viewLogs" + path;
				$.ajax({
					url: req,
					type: 'GET',
					success: function(result) {
						hideLoader();
						slide(result);
					},
					error: function(query, status, error) {
						hideLoader();
					}
				});
			}
			
			$(document).ready(function() {
				$(".logs").click(function(event) {
					event.stopPropagation();
					loadLogs($(this).attr("id").substring(1));
				});

				var padding = getPadding();
				var w = $("#projectContainer").width() - (2*padding);
				var h = $("#projectContainer").height() - (2*padding) - $("#path").height() - 3;

				$("#projectContent").css('left', padding);
				$("#projectContent").css('width', w);
				$("#projectContent").css('height', h);
				
				$("#logs").css('left', w + (4*padding));
				$("#logs").css('width', w);
				$("#logs").css('height', h);
				
				var hash = "#viewLogs";
				var ctx = "/project";
				var href = $(location).attr("href");
				href = href.substr(href.indexOf(ctx) + ctx.length);
				var index = 0;
				if ((index=href.indexOf(hash)) !== -1) {
					var startPath = href.substr(0, index);
					var endPath = href.substr(index + hash.length);
					loadLogs(startPath + endPath);
				}
			});
			
			function hashChanged(hash) {
				if (!hash || hash.length == 0) {
					slide(null);	
				}
			}
			
			if ("onhashchange" in window) {
			    window.onhashchange = function () {
			    	hashChanged(window.location.hash);
			    };
			} else {
			    var storedHash = window.location.hash;
			    window.setInterval(function () {
			        if (window.location.hash != storedHash) {
			            storedHash = window.location.hash;
			            hashChanged(storedHash);
			        }
			    }, 100);
			}
			
			$(window).on('beforeunload', function() {
				slide(" ");
			});
		</script>
	</sec:authorize>

</body>
</html>