$(document).ready(function() {
	$('.sDate').attr('disabled', true);
	$('.dDate').attr('disabled', true);
	
	$('#inlineRadio1').click(function() {
		$('.sDate').attr('disabled', false);
		$('.dDate').attr('disabled', true);
	});
	$('#inlineRadio2').click(function() {
		$('.dDate').attr('disabled', false);
		$('.sDate').attr('disabled', true);
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
			}
			else if(tableNumber === 'pb') {
				$("#table2").html(compiledTableTemplate);
			}
			
		});
	}
	

	$('#typeForm input').on('change', function() {
		if ($('input[name="inlineRadioOptions"]:checked', '#typeForm').val() === 'single') {
			$('.nbu').on('click', function() {
				var $btn = $(this).button('loading');
				submitForm("oneDateForm", "buildnbutable", "POST", "nbu").success(function() {
					$btn.button('reset');
				});
			   
			});
			$('.pb').on('click', function() {
				var $btn = $(this).button('loading');
				submitForm("oneDateForm", "buildpbtable", "POST", "pb").success(function() {
					$btn.button('reset');
				});
			});
		}
		else if ($('input[name="inlineRadioOptions"]:checked', '#typeForm').val() === 'diapason') {
			$('.nbu').on('click', function() {
				var $btn = $(this).button('loading');
				submitForm("diapasonDatesForm", "diapasonnbutable", "POST", "nbu").success(function() {
					$btn.button('reset');
				});
			});
			
			$('.pb').on('click', function() {
				var $btn = $(this).button('loading');
				submitForm("diapasonDatesForm", "diapasonpbtable", "POST", "pb").success(function() {
					$btn.button('reset');
				});
			});
		}
	});
	
	

		
});