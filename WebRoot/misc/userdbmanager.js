// Some javascript stuff taken from
// http://balusc.blogspot.com/2006/06/using-datatables.html
// Thanks for a GREAT howto on JSF datatables!

/*function addOnclickToDatatableRows() {
	var trs = $("dynamicList").getElementsByTagName("tbody")[0].getElementsByTagName("tr");
	for (var i = 0; i < trs.length; i++) {
		trs[i].onclick = new Function("highlightAndSelectRow(this)");
        trs[i].onmouseover = new Function("this.bgColor='#9096FF'");
        trs[i].onmouseout = new Function("this.bgColor='#ffffff'");
	}
}

function highlightAndSelectRow(tr) {
	var trs = $("dynamicList").getElementsByTagName("tbody")[0].getElementsByTagName("tr");
	for (var i = 0; i < trs.length; i++) {
		if (trs[i] == tr) {
			trs[i].bgColor = "#4D5189";
			$('rowIndex').set("value", trs[i].rowIndex);
			$("form:submit").click();
		} else {
			trs[i].bgColor = "#ffffff";
		}
	}
}*/

/* window.addEvent("domready", function () {
	$("form:dropMunicipality").addEvent("change", function () {
		$("form:selectMunicipality").set("value", $("form:dropMunicipality").getSelected().getProperty("value"));
	});
}); */

// get value of checked radio button
function getRadio(name) {
	return $$('input[name='+name+']').filter(function(item) { return item.checked })[0].get('value');
}