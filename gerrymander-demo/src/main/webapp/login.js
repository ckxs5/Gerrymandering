$(function () {
    $("#signupbtn").click(function () {
        $(".lightbox").delay(500).fadeIn(500);
    });
    $("#cancel-button").click(function () {
        $(".lightbox").stop().delay(500).fadeOut(500);
    });

    $(".loginmap").ready(function () {
        var mymap = L.map('loginmap').setView([44.985667, -93.267763], 13);

        L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
            maxZoom: 18,
            attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
                '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
                'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
            id: 'mapbox.streets'
        }).addTo(mymap);
    });

});