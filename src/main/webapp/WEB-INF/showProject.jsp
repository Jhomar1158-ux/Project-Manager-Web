<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Insert title here</title>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
</head>
<body>
	<div class="container">
		<h1>${project.getTitle()}</h1>
		<p>
			<b>Descripción:</b> ${project.getDescription() }
		</p>
		<p>
			<b>Fecha Límite:</b> ${project.getDue_date() }
		</p>
		<p>
			<b>Líder de Proyecto (Creador):</b> ${project.getUser().getFirstname() }
		</p>
		<a href="/dashboard" class="btn btn-primary">Regresar</a>
	</div>
</body>
</html>