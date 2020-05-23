<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@page
import = "java.io.BufferedReader"
import = "java.io.InputStreamReader"
import = "java.net.HttpURLConnection"
import = "java.net.URL"
import = "java.net.URLEncoder"
%>

<%!
StringBuffer sb;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>DATA</title>
</head>
<body>
<p id="data">
<%
request.setCharacterEncoding("UTF-8");
String apiURL = request.getParameter("url");
try {
	URL url = new URL(apiURL);
	HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	connection.setRequestMethod("GET");
	int responseCode = connection.getResponseCode();
	BufferedReader br;
	if (responseCode == 200) {
		br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
	} else {
		br = new BufferedReader(new InputStreamReader(connection.getErrorStream(),"UTF-8"));
	}
	sb = new StringBuffer();
	String line;
	while ((line = br.readLine()) != null) {
	%><%=line%><%
		sb.append(line);
	}
	br.close();
	connection.disconnect();
} catch(Exception e){
	System.err.println(e);
}
%>
</p>
</body>
</html>