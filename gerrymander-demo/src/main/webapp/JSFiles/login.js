$(function () {
    $("#signupbtn").click(function () {
        $(".lightbox").delay(500).fadeIn(500);
    });
    $("#cancel-button").click(function () {
        $(".lightbox").stop().delay(500).fadeOut(500);
    });

    $(".loginmap").ready(function () {
        var mymap = L.map('loginmap').setView([39.8283, -100.5795], 9);

        L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
            maxZoom: 18,
            minZoom: 5,
            // attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
            // '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
            // 'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
            id: 'mapbox.light'
        }).addTo(mymap);

        var maxBounds = L.latLngBounds(
            L.latLng(52.1912, -131.4395),
            L.latLng(22.6069, -63.1037)
        )

        mymap.setMaxBounds(maxBounds);
        mymap.fitBounds(maxBounds);

        var geojson = L.geoJson(statesData).addTo(mymap);
    });
});