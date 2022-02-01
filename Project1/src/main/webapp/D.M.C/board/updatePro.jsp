<%@page import="board.BoardDTO"%>
<%@page import="board.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<%

//한글처리
request.setCharacterEncoding("utf-8");

//request에 저장된 요청정보 num name pass subject content 가져오기
int num = Integer.parseInt(request.getParameter("num"));
String name = request.getParameter("name");
String pass = request.getParameter("pass");
String subject = request.getParameter("subject");
String content = request.getParameter("content");

//글번호, 비밀번호 체크하는 메서드 numCheck 정의
BoardDAO bDAO = new BoardDAO();

//메서드 호출
BoardDTO bDTO = bDAO.numCheck(num, pass);

if(bDTO != null){
	//bDTO가 null 이 아니면=번호와 비밀번호가 일치하면
	//BoardDTO updateDTO 객체생성
	BoardDTO updateDTO = new BoardDTO();
	
	//set 메서드 호출해서 파라미터값 저장
	updateDTO.setNum(num);
	updateDTO.setName(name);
	updateDTO.setPass(pass);
	updateDTO.setSubject(subject);
	updateDTO.setContent(content);
	
	//리턴형 없음 updateBoard(BoardDTO updateDTO) 메서드 정의
	//sql = "update board set subject=?, content=? where num=?";
	//updateBoard(updateDTO) 메서드 호출
	bDAO.updateBoard(updateDTO);

	response.sendRedirect("list.jsp");
	} else {
	//데이터가 없으면 : "입력하신 정보 틀림" 뒤로 이동
		%>
		<script type="text/javascript">
		alert("입력하신 정보 틀림");
		history.back();
		</script>
	<%
	}

%>
</body>
</html>