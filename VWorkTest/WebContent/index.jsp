<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script src="js/jquery-3.2.1.min.js"></script>
</head>
<body>
<button id="getUserInfo">用户信息</button>


<script src="js/vrv-jssdk.js"></script>
<script>
	$(document).ready(function(){
		vrv.init({
		    debug:true//开启调试模式,调用的所有api的返回值会在客户端alert出来
		});
		vrv.ready(function(){
			vrv.jssdk.getAccountInfo({
		       isEntUser: false,// 是否是企业用户 true 企业用户 false 普通用户
		       success: function(res){
		    	   alert(res.mUserId);
		       }
			})
	         
	    });
		
		$("#getUserInfo").bind("click", function(){
			vrv.jssdk.getAccountInfo({
		       isEntUser: false,// 是否是企业用户 true 企业用户 false 普通用户
		       success: function(res){
		    	   alert(res.mUserId);
		       }
			})
		});
		
	});
</script>

</body>
</html>