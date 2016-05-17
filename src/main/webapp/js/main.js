$(document).ready(function() {
	/* Hide Elements */
	$('#chartContainer').hide();
	$('.rateOneBank').hide();
	$('.rateDifferentBanks').hide();
	$('.rateOverDiapason').hide();
	/* ************* */
	
	/* Menu Items Behavior */
	$('.itemOne').click(function() {
		$('#chartContainer').hide();
		$('#mainContainer').show();
	});
	$('.itemThree').click(function() {
		$('#mainContainer').hide();
		$('#chartContainer').show();
		submitFormForCharts("oneDateForm", "buildpbtable", "POST");
	});
	/* ******************* */
	
	$('#typeOfChart').change(function() {
		if ($('#typeOfChart').val() === 'byRateOneBank') {
			$('.rateDifferentBanks').hide();
			$('.rateOverDiapason').hide();
			$('.rateOneBank').show();
		}
		else if ($('#typeOfChart').val() === 'byRateDifferentBanks') {
			$('.rateOneBank').hide();
			$('.rateOverDiapason').hide();
			$('.rateDifferentBanks').show();
		}
		else if ($('#typeOfChart').val() === 'byRateOverDiapason') {
			$('.rateOneBank').hide();
			$('.rateDifferentBanks').hide();
			$('.rateOverDiapason').show();
		}
		else if ($('#typeOfChart').val() === '') {
			$('.rateOneBank').hide();
			$('.rateDifferentBanks').hide();
			$('.rateOverDiapason').hide();
		}
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
			console.log(result);
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

	
	function submitFormForCharts(formName, path, method) {
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
			console.log(result);
		}).success(function(result) {
			console.log(result);
			
			var dataSet = result.map(function(item) {
				return item.sale;
			});
			var dataSetRate = result.map(function(item) {
				return item.rate;
			});
			
			var myChart = new Chart(ctx, {
				type: 'bar',
				data: {
					labels: dataSetRate || [],
					datasets: [{
						label: 'Currencies',
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
		});
	}
	var ctx = document.getElementById("myChart");
	
	/*Single date Buttons*/
	$('.nbuSingle').on('click', function() {
		var $btn = $(this).button('loading');
		submitForm("oneDateForm", "buildnbutable", "POST", "nbu").success(function() {
			$btn.button('reset');
		});
	});	  	
	$('.pbSingle').on('click', function() {
		var $btn = $(this).button('loading');
		submitForm("oneDateForm", "buildpbtable", "POST", "pb").success(function() {
			$btn.button('reset');
		});
	});
	/* */
	/*Diapason dates Buttons*/
	$('.nbuDiapason').on('click', function() {
		var $btn = $(this).button('loading');
		submitForm("diapasonDatesForm", "diapasonnbutable", "POST", "nbu").success(function() {
			$btn.button('reset');
		});
	});
	$('.pbDiapason').on('click', function() {
		var $btn = $(this).button('loading');
		submitForm("diapasonDatesForm", "diapasonpbtable", "POST", "pb").success(function() {
			$btn.button('reset');
		});
	});
	/*  */
	
	
});