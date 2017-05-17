function respondWithPostMessage(/* string */response) {
	if (window.parent != null) {
		window.parent.postMessage(response, "*");
	} else {
		window.postMessage(response, "*");
	}
}
	
$( document ).ready(function() {
	
	var scale = $('body').width() / ($('table').width() + 1);
 
	if (scale > 1) {
		scale = 1;
	}
  
	$('body').css('transform', 'scale(' + scale + ')' );
	$('body').css('-webkit-transform', 'scale(' + scale + ')' );
	$('body').css('-moz-transform', 'scale(' + scale + ')' );
	$('bodye').css('-o-transform', 'scale(' + scale + ')' );

	respondWithPostMessage("oslc-preview-height:" + $(this).height());  
}); 