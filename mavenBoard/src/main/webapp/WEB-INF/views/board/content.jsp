<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!-- content.jsp -->
<html>
<head>
	<link rel="stylesheet" type="text/css" href="style.css">
	<title>글상세보기</title>
</head>
<body>
<div align="center">
	<b>글내용 보기</b><br>
	<table border="1" width="500">
		<tr>
			<th bgcolor="yellow" width="20%">글번호</th>
			<td align="center" width="30%">${content.num}</td>
			<th bgcolor="yellow" width="20%">조회수</th>
			<td align="center" width="30%">${content.readcount}</td>
		</tr>
		<tr>
			<th bgcolor="yellow" width="20%">작성자</th>
			<td align="center" width="30%">${content.writer}</td>
			<th bgcolor="yellow" width="20%">작성일</th>
			<td align="center" width="30%">${content.reg_date}</td>
		</tr>
		<tr>
			<th bgcolor="yellow" width="20%">글제목</th>
			<td width="80%" colspan="3">${content.subject}</td>
		</tr>
		<tr>
			<th bgcolor="yellow" width="20%">글내용</th>
			<td width="80%" colspan="3">${content.content}</td>
		</tr>
		<tr>
				<th>첨부파일</th>
				<td>
				<img src="resources/img/${content.filename }" width="200px">
				</td>
		</tr>
		<tr bgcolor="yellow">
			<td colspan="4" align="right">
				<input type="button" value="답글쓰기" onclick="window.location='write.do?num=${content.num}&re_step=${content.re_step}&re_level=${content.re_level}'">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" value="글수정" onclick="window.location='update.do?num=${content.num}'">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" value="글삭제" onclick="window.location='delete.do?num=${content.num}'">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" value="글목록" onclick="window.location='board_list.do'">
			</td>
		</tr>
	</table>
</div>
</body>
</html>













