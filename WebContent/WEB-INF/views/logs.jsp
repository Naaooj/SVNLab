<%@page import="org.tmatesoft.sqljet.core.internal.lang.SqlParser.commit_stmt_return"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
	<title>Logs</title>
</head>
<body>

<c:choose>
<c:when test="${!empty holder.commits}">
	<table>
	<c:forEach items="${holder.commits}" var="commit">
	<tr>
		<td>${commit.date}</td>
		<td>${commit.principal}</td>
		<td>${commit.title}</td>
		<td></td>
	</tr>
	</c:forEach>
	</table>
	<c:if test="${holder.moreCommits}"><a id="moreResults" href="javascript:void(0);">More results (+)</a></c:if>
</c:when>
<c:otherwise>
No log found...
</c:otherwise>
</c:choose>

<script type="text/javascript">
function hasMoreResults(href) {
	return href.lastIndexOf("/") > href.indexOf("#");
}
function showMore(number) {
	var href = $(location).attr('href');
	
	if (hasMoreResults(href)) {
		href.substr(href, href.lastIndexOf("/"));
	} else {
		href += "/"+ number;
	}
	
	$.ajax({
		url: href,
		type: 'GET',
		success: function(result) {
			console.log("done");
		}
	});
}

$(document).ready(function() {
	var moreResults = $("#moreResults");
	if (moreResults != null) {
		moreResults.click(function(event) {
			var size = "${fn:length(holder.commits)}";
			console.log(size);
		});
	}
});
</script>
</body>
</html>