/**
 * à 
 */

function greekselection(){
	
	var element = document.getElementById("form:greektextarea");
	var value = element.value;
	var start = element.selectionStart;
	var end = element.selectionEnd;
	var select = value.substring(start,end);
	var targetElement = document.getElementById("form:greektext");
	targetElement.value= select + " [" + start + "$" + end + ").";
	targetElement.innerHTML = select + " [" + start + "$" + end + ").";
	//alert(select);
}

/**
 * 
 */

function arabicselection(){
	
	var element = document.getElementById("form:arabictextarea");
	var value = element.value;
	var start = element.selectionStart;
	var end = element.selectionEnd;
	var select = value.substring(start,end);
	var targetElement = document.getElementById("form:arabictext");
	targetElement.value= select + " [" + start + "$" + end + ")٠";
	targetElement.innerHTML = select + " [" + start + "$" + end + ")&#x0660;";
	//alert(select);
	
}