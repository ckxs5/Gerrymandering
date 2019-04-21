$("document").ready(function () {
    var geojson;

    function initalMap() {
        var mymap = L.map('map').setView([39.8283, -100.5795], 4.5);

        L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
            maxZoom: 18,
            minZoom: 4,
            id: 'mapbox.light'
        }).addTo(mymap);

        // control that shows state info on hover
        var info = L.control();

        info.onAdd = function (mymap) {
            this._div = L.DomUtil.create('div', 'info');
            this.update();
            return this._div;
        };

        info.update = function () {
            this._div.innerHTML = '<h4>US Population Density</h4>';
        };

        info.addTo(mymap);

        var maxBounds = L.latLngBounds(
            L.latLng(53.5300, -131.1267),
            L.latLng(22.2008, -62.3436)
        );

        mymap.setMaxBounds(maxBounds);
        //mymap.fitBounds(maxBounds);

        geojson = L.geoJson(FL_Dist, {
            style: style,
            onEachFeature: onEachDistrictFeature
        }).addTo(mymap);


        geojson = L.geoJson(MD_Dist, {
            style: style,
            onEachFeature: onEachDistrictFeature
        }).addTo(mymap);

        // geojson = L.geoJson(MD_P, {
        //     style: style,
        //     onEachFeature: onEachDistrictFeature
        // }).addTo(mymap);

        // geojson = L.geoJson(MN_Dist, {
        //     style: style,
        //     onEachFeature: onEachDistrictFeature
        // }).addTo(mymap);

        geojson = L.geoJson(MN_P, {
            style: style,
            onEachFeature: onEachDistrictFeature
        }).addTo(mymap);
    }

    initalMap();


    function style(feature) {
        return {
            // fillColor: getColor(feature.properties.density),
            fillColor: '#FD8D3C',
            weight: 2,
            opacity: 1,
            color: 'white',
            dashArray: '3',
            fillOpacity: 0.7
        };
    }

    function getColor(d) {
        return d > 1000 ? '#800026' :
            d > 500 ? '#BD0026' :
                d > 200 ? '#E31A1C' :
                    d > 100 ? '#FC4E2A' :
                        d > 50 ? '#FD8D3C' :
                            d > 20 ? '#FEB24C' :
                                d > 10 ? '#FED976' :
                                    '#FFEDA0';
    }

    function highlightFeature(e) {
        var layer = e.target;

        layer.setStyle({
            weight: 5,
            color: '#666',
            dashArray: '',
            fillOpacity: 0.7
        });

        if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
            layer.bringToFront();
        }
    }

    function resetHighlight(e) {
        geojson.resetStyle(e.target);
    }

    function onEachDistrictFeature(feature, layer) {
        layer.on({
            mouseover: highlightFeature,
            mouseout: resetHighlight
        });
    }

    function disables() {
        if (sessionStorage.getItem("user") == null) {
            $("#controllpane").find("*").prop("disabled", true);
        }

    }

    disables();
});






