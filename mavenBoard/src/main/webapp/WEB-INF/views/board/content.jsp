<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- content.jsp -->
<html>
<head>
	<link rel="stylesheet" type="text/css" href="resources/css/style.css">
	<title>�ۻ󼼺���</title>
</head>
<body>
<div align="center">
	<b>�۳��� ����</b><br>
	<table border="1" width="500">
		<tr>
			<th bgcolor="yellow" width="20%">�۹�ȣ</th>
			<td align="center" width="30%">${content.num}</td>
			<th bgcolor="yellow" width="20%">��ȸ��</th>
			<td align="center" width="30%">${content.readcount}</td>
		</tr>
		<tr>
			<th bgcolor="yellow" width="20%">�ۼ���</th>
			<td align="center" width="30%">${content.writer}</td>
			<th bgcolor="yellow" width="20%">�ۼ���</th>
			<td align="center" width="30%">${content.reg_date}</td>
		</tr>
		<tr>
			<th bgcolor="yellow" width="20%">������</th>
			<td width="80%" colspan="3" align="center">${content.subject}</td>
		</tr>
		<tr>
			<th bgcolor="yellow" width="20%">�۳���</th>
			<td width="80%" colspan="3">${content.content}</td>
		</tr>
		<tr>
				<th bgcolor="yellow">÷������</th>
				
				<c:choose>
 					<c:when test="${content.filename eq 'forbidden.png'}">
					</c:when>
					<c:when test="${content.filename eq 'null'}">
					</c:when>
					<c:otherwise>
					<td>
					<img src="resources/img/${content.filename}" width="200px">
					</td>
					</c:otherwise>
				</c:choose>
				
				
		</tr>
		<tr bgcolor="yellow">
			<td colspan="4" align="right">
				<input type="button" value="��۾���" onclick="window.location='write.do?num=${content.num}&re_step=${content.re_step}&re_level=${content.re_level}'">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" value="�ۼ���" onclick="window.location='update.do?num=${content.num}'">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" value="�ۻ���" onclick="window.location='delete.do?num=${content.num}'">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" value="�۸��" onclick="window.location='board_list.do'">
			</td>
		</tr>
	</table>
	<hr>
</div>
</body>
</html>













