$(document).ready(function() {
	var dataChartSource = [];
	
	$.ajax({
		url: "http://localhost:8080/erates/pbgraph",
		method: "POST",
		data: { startDate: "2016-04-04", finishDate: "2016-04-07" }
	})
	.success(function(response) {
		var dataSet = response.PrivatBank.map(function(item) {
			return item.sale;
		});
		
		var myChart = new Chart(ctx, {
			type: 'line',
			data: {
				labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange"],
				datasets: [{
					label: '# of Votes',
					data: dataSet || [],
					
				}]
			},
			options: {
				scales: {
					yAxes: [{
						ticks: {
							beginAtZero:true
						}
					}]
				}
			}
		});
	})
	.error(function(error) {
		console.log(error)
	});
	
	var ctx = document.getElementById("myChart");
	
})
