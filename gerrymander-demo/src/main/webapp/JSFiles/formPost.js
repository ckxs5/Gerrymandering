function objectizeFormArray(formArray) {
    var obj={};
    for(var i in formArray){
        obj[formArray[i]['name']] = formArray[i]['value'];
    }
    return obj;
}

function postForm(event, formId, postUrl, callback) {
    var form;
    if (formId != null)
        form = $(formId);
    else
        form = $("form");
    event.preventDefault();
    postData(objectizeFormArray(form.serializeArray()), postUrl, callback);
    // if (formId != null)
    //     window.location.href = redirectUrl;
}

function userlogin(logindata) {
    console.log(logindata);
    sessionStorage.setItem("user", logindata["user"]);
    console.log(sessionStorage.getItem("user"));
    window.location.href = '/';
}

function userlogout(logoutdata) {
    sessionStorage.clear();
    window.location.href = '/';
}

