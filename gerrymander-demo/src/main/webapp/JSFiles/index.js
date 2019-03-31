$("document").ready(function(){
    function initalMap() {
        var mymap = L.map('map').setView([39.8283, -100.5795], 4.5);

        L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
            maxZoom: 18,
            minZoom: 4,
            // attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
            //     '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
            //     'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
            id: 'mapbox.light'
        }).addTo(mymap);

        var maxBounds = L.latLngBounds(
            L.latLng(53.5300, -131.1267),
            L.latLng(22.2008, -62.3436)
        )

        mymap.setMaxBounds(maxBounds);
        mymap.fitBounds(maxBounds);

        var geojson = L.geoJson(statesData).addTo(mymap);
        var geojson = L.geoJson(maryland_precinct).addTo(mymap);
    }
    initalMap();

    // function disable(){
    //     console.log(document.referrers.);
    // }
    // disable();
});




