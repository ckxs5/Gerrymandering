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
    if (logindata["user"]) {
        sessionStorage.setItem("user", logindata["user"]);
        window.location.href = '/';
        console.log(sessionStorage.getItem("user"));

    }
    else {
        console.log("Error when login");
        const message = $(".message");
        message.css("color", "green");
        message.html(logindata["error"]);
    }
}

function userlogout(logoutdata) {
    sessionStorage.clear();
    window.location.href = '/';
}

function usersignup(signupdata) {
    console.log(signupdata);
    console.log(sessionStorage);
    if (signupdata["status"] && signupdata["status"] != "OK"){
        document.getElementById('userExist').innerText = "User Exists";
    }else{
        window.location.href = '/';
    }
}