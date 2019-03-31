function postData(data, url, callback){
    $.ajax({
        type: "POST",
        url: url,
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(sucMsg){
            callback(sucMsg);
        },
        failure: function(errMsg) {
            alert(errMsg);
        }
    });
}

function objectizeFormArray(formArray) {
    var obj={};
    for(var i in formArray){
        obj[formArray[i]['name']] = formArray[i]['value'];
    }
    return obj;
}