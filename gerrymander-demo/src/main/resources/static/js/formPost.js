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
}

function userlogin(logindata) {
    window.location.href = '/index';
}

function userlogout(logoutdata) {
    window.location.href = '/login';
}

function usersignup(signupdata) {
    window.location.href = '/';
}