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

function dealdata(logindata){
    console.log(logindata);
    sessionStorage.setItem("user", logindata["user"]);
    console.log(sessionStorage.getItem("user"));
    window.location.href = '/';
}