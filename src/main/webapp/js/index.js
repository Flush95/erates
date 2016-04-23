
$(document).ready(function(){
	
	
	
	$( "#openNBU" ).click(function() {
		$( "#blackoutNBU" ).addClass( "visable" );
		$( "#popupNBU" ).addClass( "visable" );
	});
	$( "#blackoutNBU, .close" ).click(function() {
		$( "#blackoutNBU" ).removeClass( "visable" );
		$( "#popupNBU" ).removeClass( "visable" );
	});
	
	
	$( "#openPB" ).click(function() {
		$( "#blackoutPB" ).addClass( "visable" );
		$( "#popupPB" ).addClass( "visable" );
	});
	$( "#blackoutPB, .close" ).click(function() {
		$( "#blackoutPB" ).removeClass( "visable" );
		$( "#popupPB" ).removeClass( "visable" );
	});
	
	
	$( '.datepicker' ).pickadate({
		monthSelector: true,
		yearSelector: true,
		yearSelector: 100,
		dateMin: [1960,1,1],
		dateMax: true,
		onStart: function() {
			this.show(1,1960)
		}
	});
	
	$("button").click(function(){
   
    alert("The paragraph is now hidden");
	});
	
});


