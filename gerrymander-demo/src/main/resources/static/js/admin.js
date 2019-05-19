$("document").ready(function () {
    $("#inputGroupSelect01").on("", function () {
        let userType = $("#inputGroupSelect01").val();
        console.log(userType);
    });

    $("#updateButton").on("click", function(){
       $("#inputGroupSelect01").each(function() {
            console.log($("#inputGroupSelect01").val());
       })
    });
});