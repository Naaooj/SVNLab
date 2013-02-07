<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
	<title>Login</title>
</head>
<body>
	<div class="area">
		<div class="areaContent">
			<c:if test="${error}">
				<div style="padding: 2px;">
				Login failed, please try again : ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
				</div>
			</c:if>
			<form name='f' action="<c:url value='j_spring_security_check' />" method='POST'>
				<table>
				<tr>
					<td>User:</td>
					<td><input type='text' name='j_username' value=''></td>
				</tr>
				<tr>
					<td>Password:</td>
					<td><input type='password' name='j_password' /></td>
				</tr>
				<tr>
					<td colspan='2'>
						<input name="submit" type="submit" value="Submit" />
					</td>
				</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>