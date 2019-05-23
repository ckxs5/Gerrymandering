function postData(data, url, callback) {
    console.log("Post Data");
    console.log(data);
    $("#loading").show();
    $.ajax({
        type: "POST",
        url: url,
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        statusCode:{
            200: function (response) {
                if (callback != null)
                    callback(response);
                $("#loading").hide();
                return true;
            },
            400: function (response) {
                alert("BAD REQUEST");
                $("#loading").hide();

                return false;
            },
            404: function (response) {
                alert("NOT FOUND");
                $("#loading").hide();
                return false;
            }
        }
    });
}

function postDataOnce(data, url, callback){

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


