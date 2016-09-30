$(document).ready(function() {

	$('.wrapp').remove();
	$('.globalWrapper').toggleClass('hidden');

	$('#chartContainer').hide();
	$('#about').hide();
	$('#statisticsContainer').hide();
	
	$('.alert-success').hide();
	$('.alert-warning').hide();
	
	
	$('.itemOne').click(function() {
		$('#chartContainer').slideUp();
		$('#about').slideUp();
		$('#statisticsContainer').slideUp();
		$('#mainContainer').show(800);
	});
	$('.itemTwo').click(function() {
		$('#mainContainer').slideUp();
		$('#about').slideUp();
		$('#chartContainer').slideUp();
		$('#statisticsContainer').show();
		
	});
	$('.itemThree').click(function() {
		$('#mainContainer').slideUp();
		$('#about').slideUp();
		$('#statisticsContainer').slideUp();
		$('#chartContainer').slideDown(800);
	});
	$('.itemFour').click(function() {
		$('#mainContainer').slideUp();
		$('#chartContainer').slideUp()
		$('#statisticsContainer').slideUp();
		$('#about').slideDown(800);
	});
	
	
	var typeOfChart = "";
	$('#chartType').change(function() {
		if ($('#chartType').val() === 'line') typeOfChart = 'line';
		else if ($('#chartType').val() === 'bar') typeOfChart = 'bar';
	});
	
	$("#xls").click(function() {
		$(".table").tableExport();
	});
	
	function submitForm(formName, path, method, tableNumber) {
		var formData = $('form[name=' + formName + ']').serializeArray();
		var dataSendObj = {};

		formData.forEach(function(item) {
			dataSendObj[item.name] = item.value
		});

		return $.ajax({
			url : '/rest/currencies/' + path,
			method : method,
			data : dataSendObj,
			dataType : 'json'
		}).error(function(result) {
			$.notify("Error Code: " + result.responseJSON.errorCode + " Error Message: " + result.responseJSON.errorMessage);
		}).success(function(result) {

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
			
			
			$("#exportButton").click(function () {
	            console.log("I am here");
	            // parse the HTML table element having an id=exportTable
	            var dataSource = shield.DataSource.create({
	                data: "#tableExport",
	                schema: {
	                    type: "table",
	                    fields: {
	                    	Currency: { type: String },
	                    	Date: {type: String },
	                    	Buy: { type: String },
	                    	Sale: { type: String }
	                    }
	                }
	            });

	            // when parsing is done, export the data to Excel
	            dataSource.read().then(function (data) {
	                new shield.exp.OOXMLWorkbook({
	                    author: "Flush95",
	                    worksheets: [
	                        {
	                            name: "CurrencyTable",
	                            rows: [
	                                {
	                                    cells: [
	                                        {
	                                            style: {
	                                                bold: true
	                                            },
	                                            type: String,
	                                            value: "Currency"
	                                        },
	                                        {
	                                            style: {
	                                                bold: true
	                                            },
	                                            type: String,
	                                            value: "Date"
	                                        },
	                                        {
	                                            style: {
	                                                bold: true
	                                            },
	                                            type: String,
	                                            value: "Buy"
	                                        },
	                                        {
	                                            style: {
	                                                bold: true
	                                            },
	                                            type: String,
	                                            value: "Sale"
	                                        }
	                                    ]
	                                }
	                            ].concat($.map(data, function(item) {
	                                return {
	                                    cells: [
	                                        { type: String, value: item.Currency },
	                                        { type: String, value: item.Date },
	                                        { type: String, value: item.Buy },
	                                        { type: String, value: item.Sale }
	                                    ]
	                                };
	                            }))
	                        }
	                    ]
	                }).saveAs({
	                    fileName: "currencies"
	                });
	            });
	        });
			
			
			
		});
	}
	
	function buildOneBankOneRateChart(formName, path) {
		var formData = $('form[name=' + formName + ']').serializeArray();
		$(".chart-loader").removeClass('hidden');
		$('#myChart').hide(); 
		
		var dataSendObj = {};

		formData.forEach(function(item) {
			dataSendObj[item.name] = item.value
		});

		return $.ajax({
			url : '/rest/currencies/' + path,
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
			
			
			var bankName = result.map(function(item) {
				return item.bank;
			});
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
			$('.alert-text').empty().append(tendency);
			
			if (tendency > 0) {
				$('#chart').append('<canvas id="myChart"></canvas>');
				$('.alert-success').hide();
				$('.alert-warning').show();
			}else {
				$('#chart').append('<canvas id="myChart"></canvas>');
				$('.alert-warning').hide();
				$('.alert-success').show();
			}

			
			var ctx = document.getElementById("myChart");
			
			Chart.defaults.global.animation.duration = 2000;
			
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
				        scaleFontColor: "white",
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
								beginAtZero: false,
								fontColor: "white",
								fontSize: 13,
								fontFamily: "'Damion', cursive"
							},
						}], 
						xAxes: [{
							ticks: {
								fontColor: "white",
								fontSize: 13, 
								fontFamily: "'Damion', cursive",
							}
						}]
					}, 
					responsive: true,
					maintainAspectRatio: true,
					title: {
						display: true,
						fontColor: "white",
						text: bankName[0]
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
	
		var dataAssets = {
			    labels: [
			        "PrivatBank", "OschadBank", "UkreximBank", "ProminvestBank",
			        "Russian Sberbank", "Raiffeisen Bank Aval", "Ukrsotsbank",
			        "Alfa-Bank", "UkrSibBank", "Pumb"
			    ],
			    datasets: [
			        {
			        	backgroundColor: "rgba(255,99,132,0.2)",
			            borderColor: "rgba(255,99,132,1)",
			            pointBackgroundColor: "rgba(255,99,132,1)",
			            pointBorderColor: "#fff",
			            pointHoverBackgroundColor: "#fff",
			            pointHoverBorderColor: "rgba(255,99,132,1)",
			            data: [29, 18, 17, 7, 6, 5, 5, 5, 4, 4],
			            
			            label: 'Top banks in terms of assets %'
			        }]
			};
			var assetsChart = document.getElementById("assetsChart");
			Chart.defaults.global.animation.duration = 2000;
			var assetsChart = new Chart(assetsChart, {
			    type: 'radar',
			    data: dataAssets,
			    animation:{
			        animateScale:true,
			        animateRotate: true
			    }
			});
			
			
			
			
			var dataLoans = {
				    labels: [
				        "PrivatBank", "Ukrsotsbank", "UkrSibBank", "Raiffeisen Bank Aval",  
				        "OTP Bank", "Alfa-Bank", "Pumb", "Platinum Bank", 
				        "OschadBank", "UniversalBank",       
				    ],
				    datasets: [
				        {
				        	backgroundColor: "rgba(255,99,132,0.2)",
				            borderColor: "orange",
				            pointBackgroundColor: "orange",
				            pointBorderColor: "#fff",
				            pointHoverBackgroundColor: "#fff",
				            pointHoverBorderColor: "orange",
				            data: [33, 22, 9, 9, 8, 5, 4, 4, 4, 2],
				            label: 'Top banks on loans to individuals in %', 
				        }]
				};
				var assetsChart = document.getElementById("loansToIndividualsChart");
				Chart.defaults.global.animation.duration = 2000;
				var assetsChart = new Chart(assetsChart, {
				    type: 'radar',
				    data: dataLoans,
				    animation:{
				        animateScale: true, 
				        animateRotate: true
				    }
				});
});