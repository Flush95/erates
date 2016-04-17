<!--<.%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%> -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Exchange Rates</title>

<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

<link rel="stylesheet" href="css/style.css">
<link rel='stylesheet prefetch'
	href='http://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css'>
<link rel='stylesheet prefetch'
	href='http://fonts.googleapis.com/css?family=Open+Sans:400italic,600italic,700italic,400,600,700'>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://www.w3schools.com/lib/w3.css">
<script src="js/prefixfree.min.js"></script>
<script src="js/index.js"></script>
<link href='http://fonts.googleapis.com/css?family=Michroma'
	rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Damion'
	rel='stylesheet' type='text/css'>
</head>

<body>


	<div class="menu">
		<div class="menuItem mi1">
			H<span>Home</span>
		</div>
		<div class="menuItem mi2">
			S<span>Settings</span>
		</div>
		<div class="menuItem mi3">
			I<span>Info</span>
		</div>
		<div class="menuItem mi4">
			A<span>About</span>
		</div>
		<div class="menuItem mi5">
			C<span>Contact me</span>
		</div>
	</div>

	<div class="container">
		<table class="table table-condensed">
			<thead>
				<tr>
					<th>Bank</th>
					<th>Currency</th>
					<th>Date</th>
					<th>Buy</th>
					<th>Sale</th>
				</tr>
			</thead>
			<tbody>
			<%@ page import="java.util.List" %>
			<%@ page import="org.flush.erates.controllers.All"%>
			<%
			List<All> list = (List<All>)session.getAttribute("allObjectsList");
			for (int i = 0; i < list.size(); i++) {
			%>
			
				<tr>
					<td>
						<% list.get(i).getBankName(); %>
					</td>
					<td>
						<% list.get(i).getCodeAlpha(); %>
					</td>
					<td>
						<% list.get(i).getDate(); %>
					</td>
					<td>
						<% list.get(i).getRateBuy(); %>
					</td>
					<td>
						<% list.get(i).getRateSale(); %>
					</td>
				</tr>
				<%} %>
			</tbody>
		</table>
	</div>

</body>

</body>
</html>