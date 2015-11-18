<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>AMPT: Error Page</title>
<style>
.errorDisplay{width:420px;margin:12% auto;border-radius:7px;padding:10px;box-shadow:3px 3px 3px 1px #ccc;text-align:center;background:#f2e6e6;}
.errorDisplay strong{vertical-align:7px;margin-left:7px;}
</style>
<link rel="stylesheet" href="./css/jquery.ui.base.css" type="text/css" />
	<script>
		function showHeader(){
			if(!document.getElementById("header")){
				document.getElementById("errorheader").style.display = 'block';
			}
		}
	</script>
</head>
<body onload="showHeader();">
	<div id="errorheader" style="display:none;">		
		<dl>
			<dt class="hdr-lt logo-nyt">&nbsp;&nbsp;&nbsp;&nbsp;
				<div class="logo"></div>
				<div class="logo-text"></div>
			</dt>
			<dt class="hdr-rt">						
				<span class="close" style="margin-top:15px;">
					<a href="javascript:void(0)" onclick="window.close();" title="Close"></a>
				</span>
				<span class="feedback"></span> 
			</dt>
		</dl>
	</div>
	<div class="errorDisplay">
		<img src="./images/error.png"></img>
		<strong>Exceptional Behaviour occurred please consult your system administrator.</strong>
	</div>
</body>
</html>