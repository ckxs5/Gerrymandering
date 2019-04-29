$("document").ready(function () {
    var districtGeojson;
    var precinctGeojson;
    var mymap;
    var info;
    var states = ['MARYLAND','MINNESOTA', 'FLORIDA'];

    function initalMap() {
        mymap = L.map('map').setView([39.8283, -100.5795], 4.5);
        L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
            maxZoom: 18,
            minZoom: 4,
            id: 'mapbox.light'
        }).addTo(mymap);

        var maxBounds = L.latLngBounds(
            L.latLng(53.5300, -131.1267),
            L.latLng(22.2008, -62.3436)
        );

        mymap.setMaxBounds(maxBounds);
        //mymap.fitBounds(maxBounds);

        // districtGeojson = L.geoJson(FL_Dist, {
        //     style: style,
        //     onEachFeature: onEachDistrictFeature
        // }).addTo(mymap);

        // districtGeojson = L.geoJson(MD_Dist, {
        //     style: style,
        //     onEachFeature: onEachDistrictFeature
        // }).addTo(mymap);

        districtGeojson = L.geoJson(MN_Dist, {
            style: style,
            onEachFeature: onEachDistrictFeature
        }).addTo(mymap);

        // geojson = L.geoJson(MD_P, {
        //     style: style,
        //     onEachFeature: onEachDistrictFeature
        // }).addTo(mymap);

        mymap.on("zoomend", function () {
            if (mymap.getZoom() > 6 && mymap.hasLayer(districtGeojson)) {
                districtGeojson.remove();
                precinctGeojson = L.geoJson(MN_P, {
                    style: style,
                    onEachFeature: onEachPrecinctFeature
                }).addTo(mymap);
            }
            //console.log(mymap.getZoom() <= 8 && mymap.hasLayer(precinctGeojson));
            if (mymap.getZoom() <= 6 && mymap.hasLayer(precinctGeojson)) {
                info.update();
                precinctGeojson.remove();
                districtGeojson = L.geoJson(MN_Dist, {
                    style: style,
                    onEachFeature: onEachDistrictFeature
                }).addTo(mymap);
            }

        });

        $("#play-btn").click(function () {
            console.log("play button");
            const weights = [
                "compactness", "politicalFairness", "populationEquality", "communityInterest",
                "efficiencyGap", "partisanFairness", "ethnicMinority", "partisanFairness",
                "ethnicMinority", "graphTheoretical"];
            let playBtnJson = {};
            for (let i in weights)
                playBtnJson[weights[i]] = $("#" + weights[i]).val();
            console.log(playBtnJson);

            postData(playBtnJson, "/setweights", printData);
        });

        // for(let i in states){
        //     console.log("asdads");
        //     $("#states[i]").click(function() {
        //         document.getElementById("dropdownMenuButton2").innerText = "MINNESOTA";
        //     });
        // }
        // $("#MINNESOTA").click(function() {
        //     document.getElementById("dropdownMenuButton2").innerText = "MINNESOTA";
        // });
        //
        // $("#MARYLAND").click(function() {
        //     document.getElementById("dropdownMenuButton2").innerText = "MARYLAND";
        // });
        //
        // $("#FLORIDA").click(function() {
        //     document.getElementById("dropdownMenuButton2").innerText = "FLORIDA";
        // });
    }

    initalMap();

    function infoWindow() {
        // control that shows state info on hover
        info = L.control();

        info.onAdd = function (mymap) {
            this._div = L.DomUtil.create('div', 'info');
            this.update();
            return this._div;
        };

        info.update = function (democratic, republican, otherParties, all, otherRaces, caucasian, asian, hispanic, african, native, name) {
            this._div.innerHTML = '<h4>Precinct Information</h4>' + (all ?
                '<b>' + name + '</b><br>'
                + '<b>Demographics</b><br>'
                + 'Asian/Pacific Islander: ' + asian + '<br>'
                + 'Caucasian: ' + caucasian + '<br>'
                + 'Hispanic (of Any Race): ' + hispanic + '<br>'
                + 'African-American: ' + african + '<br>'
                + 'Native American: ' + native + '<br>'
                + 'Other: ' + otherParties + '<br>'
                + '<br><b>Election</b><br>'
                + 'Democratic: ' + democratic + '<br>'
                + 'Republican: ' + republican + '<br>'
                + 'OtherParties: ' + otherParties + '<br>'
                + '<br><b>Population</b><br>'
                + all
                : 'No Precinct Selected');
        };

        info.addTo(mymap);
    }

    infoWindow();


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


    function highlightPrecinctFeature(e) {
        var layer = e.target;

        layer.setStyle({
            weight: 5,
            color: '#666',
            dashArray: '',
            fillOpacity: 0.7
        });

        loadPrecinctProperties(layer);

        if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
            layer.bringToFront();
        }

    }

    function loadPrecinctProperties(layer) {
        getData("/precinct/1/data", loadPrecinctPropertiesHelper)
    }


    function loadPrecinctPropertiesHelper(loadedJson) {
        obj = loadedJson;
        if (obj['data']) {
            if (obj['data']['votingData']) {
                var democratic = obj['data']['votingData']['DEMOCRATIC'];
                var republican = obj['data']['votingData']['REPUBLICAN'];
                var otherParties = obj['data']['votingData']['OTHERS']
            } else {
                var democratic = "N/A";
                var republican = "N/A";
                var others = "N/A"
            }
            if (obj['data']['demographic']) {
                var all = obj['data']['demographic']['ALL'];
                var otherRaces = obj['data']['demographic']['OTHERS'];
                var caucasian = obj['data']['demographic']['CAUCASIAN'];
                var asian = obj['data']['demographic']['ASIAN_PACIFIC_AMERICAN'];
                //console.log('aaaa');
                var hispanic = obj['data']['demographic']['HISPANIC_LATINO_AMERICAN'];
                var african = obj['data']['demographic']['AFRICAN_AMERICAN'];
                var native = obj['data']['demographic']['NATIVE_AMERICAN']
            } else {
                var all = "N/A";
                var others = "N/A";
                var caucasian = "N/A";
                var asian = "N/A";
                var hispanic = "N/A";
                var african = "N/A";
                var native = "N/A"
            }
        }
        if (obj['name']) {
            var name = obj['name']
        }
        info.update(democratic, republican, otherParties, all, otherRaces, caucasian, asian, hispanic, african, native, name)
    }

    function resetPrecinctHighlight(e) {
        //info.update(null,null,null,null,null,null,null,null,null,null,null);
        info.update();
        precinctGeojson.resetStyle(e.target);
    }

    function resetDistrictHighlight(e) {
        districtGeojson.resetStyle(e.target);
    }

    function onEachDistrictFeature(feature, layer) {
        layer.on({
            mouseover: highlightFeature,
            mouseout: resetDistrictHighlight
        });
    }

    function onEachPrecinctFeature(feature, layer) {
        layer.on({
            mouseover: highlightPrecinctFeature,
            mouseout: resetPrecinctHighlight
        });
    }

    // function disables() {
    //     if (sessionStorage.getItem("user") == null) {
    //         $("#controllpane").find("*").prop("disabled", true);
    //     }
    //
    // }
    // disables();
});






