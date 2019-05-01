function postData(data, url, callback) {
    $.ajax({
        type: "POST",
        url: url,
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        statusCode:{
            200: function (response) {
                console.log("Arrived");
                if (callback != null) {
                    callback(response);
                }
            },
            400: function (response) {
                alert("BAD REQUEST");
            },
            404: function (response) {
                alert("NOT FOUND");
            }
        }
    });
}

function getData(url, callback){
    $.ajax({
        type: "GET",
        url: url,
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

function printData(data) {
    console.log(data);
}
