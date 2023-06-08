const removeChilds = (parent) => {
    while (parent.lastChild) {
        parent.removeChild(parent.lastChild);
    }
};

function showModal(name) {
    //show modal
    let modalName = "." + name;
    $(modalName).css("display", "flex");
    $(".upsert-dialog").css("display", "block");

    var selector = `.${name} .upsert-dialog`;
    var btn = document.querySelector(selector).querySelector(".close-button");
    btn.dataset.tipe = name;
}

function showModalMessageDialog(name, result) {
    //show modal
    debugger;
    let modalName = "." + name;
    $(modalName).css("display", "flex");
    $(".message-dialog").css("display", "block");

    const el = document.querySelector(".message-dialog").querySelector(".close-button");
    el.dataset.tipe = name;

    var msgNode = document.querySelector('.message-body');
    if (msgNode != null) {
        var p = document.createElement('p');
        p.textContent = result.message;
        msgNode.appendChild(p);
    }
    if (result != null) {
        document.querySelector('.actionName').textContent = "to " + result.accessDenied.action;
    }
}

function showModalConfirmDialog(name, actionUrl) {
    //show modal
    debugger;
    let modalName = "." + name;
    $(modalName).css("display", "flex");
    $(".message-dialog").css("display", "block");

    const el = document.querySelector(".message-dialog").querySelector(".close-button");
    el.dataset.tipe = name;

    document.querySelector('.confirm-button').setAttribute('href', actionUrl);

}




function hideModal(name) {
    debugger;
    let modalName = "." + name;
    $(modalName).removeAttr("style");
    $(".popup-dialog").removeAttr("style");
    setValidationGroup();
}

function showValidationMessageKedua(validations) {
    $('.color-error').removeAttr('class');
    $(".validation-message").attr("class", "validation-message");
    $(".validation-message").text("");

    document.querySelectorAll(".field-validation-error").forEach(el => el.remove());

    result = validations.reduce(function (r, a) {
                r[a.propertyName] = r[a.propertyName] || [];
                r[a.propertyName].push({'errorMessage' : a.errorMessage});
                return r;
            }, Object.create(null));

    Object.entries(result).forEach(function(item){
        var div = document.createElement('div');
        div.setAttribute("class", "field-validation-error");
        var input = document.querySelector(`input.${item[0]}`).parentElement;

        item[1].forEach(function(obj){
            let span = document.createElement('span');
            span.textContent = obj.errorMessage;
            div.appendChild(span);
        });

        input.appendChild(div);
    });
}



function showValidationMessage(validations) {
    $('.color-error').removeAttr('class');
    $(".validation-message").attr("class", "validation-message");
    $(".validation-message").text("");

    for (let validation of validations) {
        //let propertyName = validation.propertyName;

        //jika modal popup
        var input = document.querySelector(`[data-for= ${validation.propertyName}]`);
        if (input != null) {
            $(`form [data-for=${validation.propertyName}]`).text(validation.errorMessage);
            document.querySelector(`[data-for= ${validation.propertyName}]`).setAttribute("class", "field-validation-error color-error");
        }
        //jika BUKAN modal popup
        else {
            //$(".field-validation-error").text("");
            $(`form [data-valmsg-for=${validation.propertyName}]`).text(validation.errorMessage);
            document.querySelector(`[data-valmsg-for= ${validation.propertyName}]`).setAttribute("class", "validation-message field-validation-error");
        }
    }
}


function setValidationGroup() {
    $(".field-validation-error").text("");
    $(".fa-exclamation-circle").css("display", "none");
}





