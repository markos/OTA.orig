
function katartisis_searchInit(str){

	$(str).addEvent('keyup', function() {		
		if( $(str).getProperty('value').length <= 3 ) return;
	
		var req = new Request.HTML({
			method: 'post',
			url: '/OTA/servlet/searchServe',
			encoding: 'utf-8',
			data: $('searchForm').toQueryString(),
			onRequest: function() {},
			update: $('searchAJResults'),
			onComplete: function(response) {} 
		}).send();
	});
}