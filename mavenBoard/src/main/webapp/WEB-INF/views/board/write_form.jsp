<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!-- writeForm.jsp -->
<html>
<head>
	<title>글쓰기</title>
	<link rel="stylesheet" type="text/css" href="resources/css/style.css">
	<script type="text/javascript">
		function checkBoard(){
			if (f.writer.value==""){
				alert("이름을 입력해 주세요!!")
				f.writer.focus()
				return false
			}
			if (!f.subject.value){
				alert("제목을 입력해 주세요!!")
				f.subject.focus()
				return false
			}
			if (f.email.value==""){
				alert("이메일을 입력해 주세요!!")
				f.email.focus()
				return false
			}
			if (f.content.value==""){
				alert("내용을 입력해 주세요!!")
				f.content.focus()
				return false
			}
			if (f.passwd.value==""){
				alert("비밀번호를 입력해 주세요!!")
				f.passwd.focus()
				return false
			}
			return true		
		}
	</script>
</head>
<body>
<div align="center">
	<form name="f" action="write.do" method="post" onsubmit="return checkBoard()" enctype="multipart/form-data">
		<input type="hidden" name="num" value="${param.num}"/>
		<input type="hidden" name="re_step" value="${param.re_step}"/>
		<input type="hidden" name="re_level" value="${param.re_level}"/>
		<table border="1" width="500">
			<tr bgcolor="yellow">
				<td colspan="2" align="center">글 쓰 기</td>
			</tr>
			<tr>
				<th bgcolor="yellow">이 름</th>
				<td><input type="text" name="writer"></td>
			</tr>
			 <tr>
				<th bgcolor="yellow">제 목</th>
				<td><input type="text" name="subject" size="50"></td>
			</tr>
			<tr>
				<th bgcolor="yellow">Email</th>
				<td><input type="text" name="email" size="50"></td>
			</tr>
			<tr>
				<th bgcolor="yellow">내 용</th>
				<td><textarea name="content" rows="11" cols="50"></textarea></td>
			</tr>
			<tr>
				<th>파일명</th>
				<td><input type="file" name="filename"></td>
			</tr>
			<tr>
				<th bgcolor="yellow">비밀번호</th>
				<td><input type="password" name="passwd"></td>
			</tr>
			<tr bgcolor="yellow">
				<td colspan="2" align="center">
					<input type="submit" value="글쓰기">
					<input type="reset" value="다시작성">
					<input type="button" value="목록보기" onclick="window.location='board_list.do'">
				</td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>











