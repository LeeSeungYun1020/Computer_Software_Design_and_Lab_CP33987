<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    

<%
	request.setCharacterEncoding("UTF-8");
	int second = 0, minute = 0, hour = 0;
	try{
		int number = Integer.parseInt(request.getParameter("number"));

		second = number % 60;
		minute = number / 60 % 60;	
		hour = number / 3600;
	}catch(Exception e){}
%>

<br> <%=hour%> hours <%=minute%> minutes <%=second%> seconds