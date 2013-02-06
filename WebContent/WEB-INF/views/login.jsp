<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
	<title>Login</title>
</head>
<body>

	<c:if test="${error}">
	<div>
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
</body>
</html>