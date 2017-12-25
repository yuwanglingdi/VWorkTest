<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src="js/jquery-3.2.1.min.js"></script>
<script src="js/jquery.form.js"></script>
<title>Insert title here</title>
</head>
<body>

	<form action="/test" method="post" id="form1" >
		<input type='text' name="username" id="username"/><br/>
		<input type='text' name="password" id="password"/><br/>
		<input type="submit"/><br/>
		<input type="button" id="ensureBtn" value="json post"/>
	</form>


<script>
	$(document).ready(function(){
		$("#ensureBtn").bind("click", function(){
			/* $.post("/test", {
				"username" : $("#username").val(),
				"password" : $("#password").val()
			}, function(obj){
				alert(obj);
			}, "json"); */
			$("#form1").ajaxSubmit({
				success: function(obj){
					alert(obj);
				}, 
				dataType: 'json',
				contentType: 'application/json;charset=utf-8'
			});
		});
		
		
		
	});
</script>

</body>
</html>