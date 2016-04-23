
$(document).ready(function(){
	$("#formDates").hide();
	
	$("#dates").click(function(){
		$(".btn-group").hide("slow", function(){
			$("#formDates").show("slow");
		});
	});	
});