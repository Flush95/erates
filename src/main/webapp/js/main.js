$(document).ready(function() {
	$('#chartContainer').hide();
	$('.rateOneBank').hide();
	$('.rateDifferentBanks').hide();
	$('#modalError').hide();
	$('#graph-container').hide();
	$('#charts-container').hide();

	
	$('.itemOne').click(function() {
		$('#chartContainer').slideUp();
		$('#mainContainer').slideDown(800);
	});
	$('.itemThree').click(function() {
		$('#mainContainer').slideUp();
		$('#chartContainer').slideDown(800);
	});
	
	$('#typeOfChart').change(function() {
		if ($('#typeOfChart').val() === 'byRateOneBank') {
			$('.rateDifferentBanks').slideUp();
			$('#charts-container').slideUp();
			$('.rateOneBank').slideDown(800);
			$('#graph-container').slideDown(800);
		}
		else if ($('#typeOfChart').val() === 'byRateDifferentBanks') {
			$('.rateOneBank').slideUp();
			$('#graph-container').slideUp();
			$('.rateDifferentBanks').slideDown(800);
			$('#charts-container').slideDown(800);
		}
		else if ($('#typeOfChart').val() === '') {
			$('.rateOneBank').slideUp();
			$('.rateDifferentBanks').slideUp();
			$('#graph-container').slideUp();
			$('#charts-container').slideUp();
		}
	});
	
	var typeOfChart = "";
	$('#chartType').change(function() {
		if ($('#chartType').val() === 'line') typeOfChart = 'line';
		else if ($('#chartType').val() === 'bar') typeOfChart = 'bar';
	});
	
	function submitForm(formName, path, method, tableNumber) {
		var formData = $('form[name=' + formName + ']').serializeArray();
		var dataSendObj = {};

		formData.forEach(function(item) {
			dataSendObj[item.name] = item.value
		});

		return $.ajax({
			url : 'http://localhost:8080/erates/rest/currencies/' + path,
			method : method,
			data : dataSendObj,
			dataType : 'json'
		}).error(function(result) {
			$.notify("Error Code: " + result.responseJSON.errorCode + " Error Message: " + result.responseJSON.errorMessage);
		}).success(function(result) {
			console.log(result);

			var dataSet = result.map(function(item) {
				return item;
			});
			var tableTemplate = _.template($("#templateRootTable").html());
			var compiledTableTemplate = tableTemplate({
				data : dataSet
			});
			if (tableNumber === 'nbu') {
				$("#table1").html(compiledTableTemplate);
			} else if(tableNumber === 'pb') {
				$("#table2").html(compiledTableTemplate);
			}
		});
	}
	
	function buildOneBankOneRateChart(formName, path) {
		var formData = $('form[name=' + formName + ']').serializeArray();
		$(".chart-loader").removeClass('hidden');
		$('#myChart').hide(); 
		$('.alert').hide();
		var dataSendObj = {};

		formData.forEach(function(item) {
			dataSendObj[item.name] = item.value
		});

		return $.ajax({
			url : 'http://localhost:8080/erates/rest/currencies/' + path,
			method : 'POST',
			data : dataSendObj,
			dataType : 'json'
		}).error(function(result) {
			$.notify("Error Code: " + result.responseJSON.errorCode + " Error Message: " + result.responseJSON.errorMessage);
			$(".chart-loader").addClass('hidden');
			$('#myChart').show(); 
		}).success(function(result) {
			$(".chart-loader").addClass('hidden');
			$('#myChart').show(); 
			
			
			var dataSetSale = result.map(function(item) {
				return item.sale;
			});
			var dataSetBuy = result.map(function(item) {
				return item.buy;
			});
			var dataSetRate = result.map(function(item) {
				return item.date;
			});

			var tendency = parseFloat(dataSetBuy[dataSetBuy.length-1]) - parseFloat(dataSetBuy[0]);
			
			$('#myChart').remove(); 
			$('.alert').remove();
			
			if (tendency < 0) {
			$('#graph-container').append('<canvas id="myChart"></canvas>' + 
					'<div class="alert alert-warning alert-dismissible" role="alert">' +
	  						'<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + 
	 						'<strong>Warning!</strong>' + 'Buy tendency: ' + tendency + '</div>');
			}else {
				$('#graph-container').append('<canvas id="myChart"></canvas>' + 
						'<div class="alert alert-success alert-dismissible" role="alert">' +
		  						'<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + 
		 						'<strong>Eah!</strong>' + 'Buy tendency: ' + tendency + '</div>');
			}
			
			var ctx = document.getElementById("myChart");
			
			Chart.defaults.global.responsive = true;
			Chart.defaults.global.legend.labels.fontColor = "black";

			var myChart = new Chart(ctx, {
				type: typeOfChart != ""? typeOfChart : "line",
				data: {
					labels: dataSetRate || [],
					datasets: [{
						label: $('#rate').val() + ' Buy',
						 fill: false,
				            lineTension: 0.1,
				            backgroundColor: "rgba(75,192,192,0.4)",
				            borderColor: "rgba(75,192,192,1)",
				            borderCapStyle: 'butt',
				            borderDash: [],
				            borderDashOffset: 0.0,
				            borderJoinStyle: 'miter',
				            pointBorderColor: "rgba(75,192,192,1)",
				            pointBackgroundColor: "#fff",
				            pointBorderWidth: 1,
				            pointHoverRadius: 5,
				            pointHoverBackgroundColor: "rgba(75,192,192,1)",
				            pointHoverBorderColor: "rgba(220,220,220,1)",
				            pointHoverBorderWidth: 2,
				            pointRadius: 1,
				            pointHitRadius: 10,
						data: dataSetBuy || []
					}, {
						label: $('#rate').val() + ' Sale',
						borderColor: "red",
						data: dataSetSale || []
					}]
				},
				options: {
					scales: {
						yAxes: [{
							ticks: {
								beginAtZero: false
							}
						}]
					}, 
					title: {
						display: true,
						fontSize: 20,
						padding: 20,
						fontColor: "white",
						text: "Chart for a single Rate"
					},
				}
			});
		});
	}
	
	$('.nbuSingle').on('click', function() {
		var $btn = $(this).button('loading');
		submitForm("oneDateForm", "buildnbutable", "POST", "nbu").success(function() {
			$btn.button('reset');
		}).error(function() {
			$btn.button('reset');
		});
	});	  	
	$('.pbSingle').on('click', function() {
		var $btn = $(this).button('loading');
		submitForm("oneDateForm", "buildpbtable", "POST", "pb").success(function() {
			$btn.button('reset');
		}).error(function() {
			$btn.button('reset');
		});
	});

	
	
	$('.nbuDiapason').on('click', function() {
		var $btn = $(this).button('loading');
		submitForm("diapasonDatesForm", "diapasonnbutable", "POST", "nbu").success(function() {
			$btn.button('reset');
		}).error(function() {
			$btn.button('reset');
		});
	});
	$('.pbDiapason').on('click', function() {
		var $btn = $(this).button('loading');
		submitForm("diapasonDatesForm", "diapasonpbtable", "POST", "pb").success(function() {
			$btn.button('reset');
		}).error(function() {
			$btn.button('reset');
		});
	});

	
	$('.nbuRateOneBank').on('click', function() {
		var $btn = $(this).button('loading');
		buildOneBankOneRateChart("byRateOneBankForm", "nbuonebank").success(function() {
			$btn.button('reset');
		}).error(function() {
			$btn.button('reset');
		});
	});
	$('.pbRateOneBank').on('click', function() {
		var $btn = $(this).button('loading');
		buildOneBankOneRateChart("byRateOneBankForm", "pbonebank").success(function() {
			$btn.button('reset');
		}).error(function() {
			$btn.button('reset');
		});
	});

	
	$('.rateDiffBanks').on('click', function() {
		var $btn = $(this).button('loading');
		buildAllBanksAllRatesCharts("byRateDifferentBanksForm", "differentbanks").success(function() {
			$btn.button('reset');
		}).error(function() {
			$btn.button('reset');
		});
	});
	
});