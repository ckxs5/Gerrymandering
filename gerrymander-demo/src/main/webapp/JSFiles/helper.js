function postData(data, url, callback){
    $.ajax({
        type: "POST",
        url: url,
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(sucMsg){
            if (callback != null) {
                callback(sucMsg);
            }
        },
        failure: function(errMsg) {
            alert(errMsg);
        }
    });
}

