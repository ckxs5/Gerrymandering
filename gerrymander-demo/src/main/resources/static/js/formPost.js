function objectizeFormArray(formArray) {
    console.log("formArray");
    console.log(formArray);
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
    // console.log(form);
    // console.log(objectizeFormArray(form.serializeArray()));
    event.preventDefault();
    postData(objectizeFormArray(form.serializeArray()), postUrl, callback);
}

function userlogin(logindata) {
    console.log("right here");
    console.log(logindata);

    document.getElementById('signInForm').innerHTML = document.getElementById('logOutButton').innerHTML;
    document.getElementById('logOutButton').style.display="initial";
}

function userlogout(logoutdata) {
    window.location.href = '/';
}

function usersignup(signupdata) {
    console.log("sign up data");
    window.location.href = '/';
}