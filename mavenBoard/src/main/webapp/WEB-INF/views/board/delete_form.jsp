<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!-- deleteForm.jsp -->
<html>
<head>
	<title>글삭제</title>
	<link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>	
<div align="center">
	<b>글삭제</b>
	<form name="f" action="delete.do" method="post">
		<input type="hidden" name="num" value="${param.num}"/>
		<table border="1" width="300">
			<tr bgcolor="yellow">
				<th>비밀번호를 입력해 주세요</th>
			</tr>
			<tr>
				<td align="center">비밀번호 : <input type="password" name="passwd"></td>
			</tr>
			<tr bgcolor="yellow">
				<td align="center">
					<input type="submit" value="글삭제">
					<input type="button" value="글목록" onclick="window.location='board_list.do'">
				</td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>