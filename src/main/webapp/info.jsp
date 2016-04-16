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
		<h2>${bank}</h2>
		<table class="table table-condensed">
			<thead>
				<tr>
					<th>Currency</th>
					<th>Date</th>
					<th>Buy</th>
					<th>Sale</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>Eur</td>
					<td>
						${EURDate}
					</td>
					<td>
						${EURBuy}
					</td>
					<td>
						${EURSale}
					</td>
				</tr>
				<tr>
					<td>Usd</td>
					<td>
						${USDDate}
					</td>
					<td>
						${USDBuy}
					</td>
					<td>
						${USDSale}
					</td>
				</tr>
				<tr>
					<td>Rub</td>
					<td>
						${RUBDate}
					</td>
					<td>
						${RUBBuy}
					</td>
					<td>
						${RUBSale}
					</td>
				</tr>
			</tbody>
		</table>
	</div>

</body>

</body>
</html>