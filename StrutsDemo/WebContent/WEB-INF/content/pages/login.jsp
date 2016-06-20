<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

	<form action="loginAction" method="post" >
		<table>
			<tr>
				<td>User name: </td>
				<td><input type="text" name="userName" size="20" maxlength="20"></td>
			</tr>
			<tr>
				<td></td>
				<td>	
					<s:if test="hasFieldErrors()">
						<label class="invalidMsg" style="float: none">
							<s:property value="fieldErrors.get('userName').get(0)" />
						</label>
			        </s:if>
		         </td>
			</tr>
			<tr>
				<td>Password: </td>
				<td><input type="password" name="password" size="20" maxlength="50"/></td>
			</tr>
			<tr>
				<td></td>
				<td>
					<s:if test="hasFieldErrors()">
						<label class="invalidMsg" style="float: none">
							<s:property value="fieldErrors.get('password').get(0)" />
						</label>
					</s:if>
				</td>
			</tr>	
				
			<tr>
				<td colspan="2" style="text-align: center;">			
				<input  type="submit" name="Login" value="Login">
				</td>
			</tr>
		</table>
	</form>
