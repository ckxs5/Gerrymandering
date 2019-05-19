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
    event.preventDefault();
    postData(objectizeFormArray(form.serializeArray()), postUrl, callback);
}

function userlogin(logindata) {
    console.log("right here");
    console.log(logindata);

    document.getElementById('signInForm').innerHTML = document.getElementById('logOutButton').innerHTML;
    document.getElementById('signInForm').style.display="initial";
    document.getElementById('logOutButton').remove();
    window.location.href = '/';
}

function userlogout(logoutdata) {
    window.location.href = '/';
}

function usersignup(signupdata) {
    console.log("sign up data");
    window.location.href = '/';
}

function getDeleteId(){
    // alert($("#inputGroupSelect").val());
    let id = $("#inputGroupSelect").val();
    let url = "/user/" + id + "/delete";
    getData(url, null);
    window.location.href = '/';

}

// function getDeleteEmail(){
//     alert($("#inputGroupSelect").val());
//     var email = $("#inputGroupSelect").val();
//     var url = "/user/" + email + "/delete";
//     getData(url, null);
//
// }

function getIdandType(id){
    // var id = $("#userId").children(':first-child').text();
    const type_id = "#userType" + id;
    let usertype = $(type_id).val();
    console.log("ID: " + id);
    console.log("TYPE:" + usertype);
    let url =  "/user/" + id + "/" + usertype;
    getData(url, null);
    window.location.href = '/';

}