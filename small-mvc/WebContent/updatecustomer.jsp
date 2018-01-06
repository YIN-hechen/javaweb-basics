<%@page import="com.mvcapp.domain.Customer"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<% if(request.getAttribute("message")!=null){%>
	<br>
	<font color="red"><%=request.getAttribute("message")%></font>
	<br>
	<br>
	<%} %>

<%
Customer customer=(Customer)request.getAttribute("customer");


%>

	<form action="update.do" method="post">

		<input type="hidden" name="id" value="<%=customer.getId() %>" /> <input type="hidden"
			name="oldName" value="<%=customer.getName()%>" />

		<table>
			<tr>
				<td>^^CustomerName:</td>
				<td><input type="text" name="name" value="<%=customer.getName()%>" /></td>
			</tr>
			<tr>
				<td>Address:</td>
				<td><input type="text" name="address" value="<%=customer.getAddress()%>" /></td>
			</tr>
			<tr>
				<td>Phone:</td>
				<td><input type="text" name="phone" value="<%=customer.getPhone()%>" /></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="Submit" /></td>
			</tr>
		</table>
	</form>

</body>
</html>