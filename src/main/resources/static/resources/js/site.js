var moneyConversions = document.querySelectorAll(".moneyConversion");
for(let element of moneyConversions){
	let convertedValue = Number(element.value).toFixed(2);
	element.value = convertedValue;
}
var numberInputs = document.querySelectorAll("[type=number]");
for(let element of numberInputs){
	if(element.value == ""){
		element.value = 0;
	}
}
var dateInputs = document.querySelectorAll("[type=date]");
for(let element of dateInputs){
	if(element.getAttribute("value") != ""){
		let dateValue = new Date(element.getAttribute("value"));
		let formatted = dateValue.toISOString().split('T')[0];
		element.value = formatted;
	}
}
let alternateAction = document.querySelector(".alternate-action table");
if(alternateAction != null){
	let actionType = alternateAction.getAttribute("data-action");
	document.querySelector(".alternate-action").setAttribute("action", actionType);
	if(actionType === "update"){
		document.querySelector(".readonly-id").setAttribute("readonly", "readonly");
	}
}
let role = document.querySelector(".role");
if(role != null){
	let roleName = role.textContent;
	let formatted = roleName.replace('[', '').replace(']', '').replace('ROLE_', '');
	role.textContent = formatted;
}
else{
    let mainBody = document.querySelector(".main-body");
    let roleName = mainBody.getAttribute("data-role");
    let formatted = roleName.replace('[', '').replace(']', '').replace('ROLE_', '');
    mainBody.setAttribute("data-role", formatted);
}

let appTahun = document.querySelector(".app-tahun");
if(appTahun != null){
	let today = new Date();
	let tahun = today.getFullYear();
	appTahun.textContent = tahun;
}

document.addEventListener("DOMContentLoaded", function(event) {
    let pageArray = document.querySelectorAll('.pagination a');
    let current = document.querySelector('[data-currentpage]');
    if(pageArray != null){
    pageArray.forEach(function(element){
        if(element.textContent == current.dataset.currentpage){
            element.style.backgroundColor = '#14acf8';
            element.style.color = '#202a45';
        }
    });
    }

    var inputPage = document.querySelector(".pagination [type='number']");
    if(inputPage != null){
        inputPage.addEventListener("change", function(event){
            var url = this.dataset["href"];
            var nameSearch = document.querySelector("[name = 'name']");
            window.location.href = url + "?page="+ this.value +"&name=" + nameSearch.value;
        });
    }


    var buttonClearSearchForm = document.querySelector('.button-clear-all-form');
    if(buttonClearSearchForm != null){
        buttonClearSearchForm.addEventListener("click", clearFormSearch);
    }


});

var dropdown = document.getElementsByClassName("menuloan");
var i;

for (i = 0; i < dropdown.length; i++) {
  dropdown[i].addEventListener("click", function() {
    this.classList.toggle("active");
    var dropdownContent = this.nextElementSibling;
    if (dropdownContent.style.display === "block") {
      dropdownContent.style.display = "none";
    } else {
      dropdownContent.style.display = "block";
    }
  });
}
