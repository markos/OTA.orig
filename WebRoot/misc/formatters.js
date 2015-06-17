
function IsNumeric(str) {
   var strValidChars = "0123456789.-";
   var strChar;
   var result = true;

   if (str.length == 0) return false;

   for (i = 0; i < str.length && result == true; i++) {
      strChar = str.charAt(i);
      if (strValidChars.indexOf(strChar) == -1) {
         result = false;
      }
   }
   return result;
}
function formatMoney(n, c, d, t) {
	var c = isNaN(c = Math.abs(c)) ? 2 : c, d = d == undefined ? "," : d, t = t == undefined ? "." : t, s = n < 0 ? "-" : "", i = parseInt(n = Math.abs(+n || 0).toFixed(c)) + "", j = (j = i.length) > 3 ? j % 3 : 0;
	return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
}

function currencyFormatter(field) {
	var fieldValue = $(field).get('value');
	fieldValue = fieldValue.replace(".","").replace(",",".");
 	var floatValue = new Number(fieldValue);
 	var newFieldValue = formatMoney(floatValue);
	$(field).set('value', newFieldValue);
	
	return true;
}

function postalCodeFormatter(field) {
  var fieldValue = $(field).get('value');
  if (IsNumeric(fieldValue) == true) {
  	var num = parseInt(fieldValue) % 100000;
  	var res = sprintf("%5d", num);
  	$(field).set('value', res);
  	$(field).setStyle('color', 'black');
  	return true;
  } else {
  	$(field).setStyle('color', 'red');  
  	return false;
  }
}

function numberFormatter(field) {
  var fieldValue = $(field).get('value');
  if (IsNumeric(fieldValue) == true) {
  	return true;
  } else {
  	$(field).setStyle('color', 'red');
  	return false;
  }
}