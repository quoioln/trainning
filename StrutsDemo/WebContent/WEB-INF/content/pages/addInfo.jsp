<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add info</title>
</head>
<body>
	<h4>Hello ${userName}</h4>
	<h4>Type: ${typeUser}</h4>
	<form action="addInfo" method="get">
		<table>
			<tr>
				<td>Full name: </td>
				<td>
					<input type="text" name="fullName" value="${fullName }">
					<br>
					<s:if test="hasFieldErrors()">
						<label class="invalidMsg" style="float: none">
							<s:property value="fieldErrors.get('fullName').get(0)" />
						</label>
			        </s:if>
				</td>
				
			</tr>
			
			<tr>
				<td>Age: </td>
				<td>
					<input type="text" name="age" value="${age }">
					<br>
					<s:if test="hasFieldErrors()">
						<label class="invalidMsg" style="float: none">
							<s:property value="fieldErrors.get('age').get(0)" />
						</label>
			        </s:if>
				</td>
			</tr>
			
			
			<tr>
				<td>Email: </td>
				<td>
					<input type="text" name="email" value="${email }">
					<br>
					<s:if test="hasFieldErrors()">
						<label class="invalidMsg" style="float: none">
							<s:property value="fieldErrors.get('email').get(0)" />
						</label>
			        </s:if>
				</td>
			</tr>

			
			<tr>
				<td>Phone number: </td>
				<td>
					<input type="text" name="phoneNum" value="${phoneNum }">
					<br>
					<s:if test="hasFieldErrors()">
						<label class="invalidMsg" style="float: none">
							<s:property value="fieldErrors.get('phoneNum').get(0)" />
						</label>
			        </s:if>
				</td>
			</tr>
			
			<tr>
				<td colspan="2">
					<input type="submit" value="Login">				
				</td>
			</tr>
		</table>
<%-- 		<s:textfield name="fullName" label="Full name" size="50"/> --%>
<%-- 		<s:textfield name="age" label="Age"/> --%>
<%-- 		<s:textfield name="email" label="Email"/> --%>
<%-- 		<s:textfield name="phoneNum" label="Phone"/> --%>
<%-- 		<dir style="text-align: center"><s:submit name="submit" label="Add"/></dir> --%>
	</form>
</body>
</html>