function removeAllStyles() {
	$(document.body).setStyle('background', 'none');
	
	$(document.body).getElements('a').setStyle('background', 'none');
	$(document.body).getElements('div').setStyle('background', 'none');
	$(document.body).getElements('img').setStyle('display', 'none');
	
	$(document.body).getElements('input[type=button]').setStyle('display', 'none');
	$(document.body).getElements('input[type=submit]').setStyle('display', 'none');
	$(document.body).getElements('input[type=text]').setStyle('border', 'none');
	$(document.body).getElements('textarea').setStyle('border', 'none');
	$(document.body).getElements('div.hideable').setStyle('display', 'none');
		
	$('header').setStyle('display', 'none');
	$('topheader').setStyle('display', 'none');
	$('medheader').setStyle('display', 'none');
	$('medleft').setStyle('display', 'none');
	$('medcenter').setStyle('display', 'none');
	$('medright').setStyle('display', 'none');
	
	
	$('footer').setStyle('display', 'none');
	$('footerleft').setStyle('display', 'none');
	$('footermenu').setStyle('display', 'none');
	$('footerlogo').setStyle('display', 'none');
	//$('wrapper').setStyle('display', 'none');
	
	$$('textarea').each( fixTextAreaHeight(), this); 
}

function fixTextAreaHeight() {
	alert(this);
}