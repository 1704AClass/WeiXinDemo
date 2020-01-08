<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
	<h1>欢迎您：</h1>
	${openid }
	<table>
		<tr>
			<td>头像</td>
			<td>昵称</td>
			<td>性别</td>
			<td>省份</td>
			<td>城市</td>
		</tr>
		<tr>
			<td><img src="${tx }"></td>
			<td>${name }</td>
			<td>${sex }</td>
			<td>${sheng }</td>
			<td>${shi }</td>
		</tr>
	</table>
</body>
</html>