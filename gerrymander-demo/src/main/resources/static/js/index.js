$("document").ready(function () {
    var states = L.layerGroup();
    var districts = L.layerGroup();
    var precincts = L.layerGroup();
    var stateLayer, districtLayer, precinctLayer;
    var mymap;
    var info;
    var maxBounds;
    var districtZoomLevel = 6;
    var DefaultZoomLevel = 4;
    var precinctZoomLevel = 8;

        mymap = L.map('map', {layers: states}).setView([39.8283, -100.5795], DefaultZoomLevel);
        L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
            maxZoom: 18,
            minZoom: 4,
            id: 'mapbox.light'
        }).addTo(mymap);

        maxBounds = L.latLngBounds(
            L.latLng(53.5300, -131.1267),
            L.latLng(22.2008, -62.3436)
        );

        mymap.setMaxBounds(maxBounds);
        mymap.fitBounds(maxBounds);

        stateLayer = L.geoJson(statesData, {
            style:style,
            onEachFeature: onEachStateFeature
        }).addTo(states);

        districtLayer = L.geoJson(FL_Dist, {
            style: style,
            onEachFeature: onEachDistrictFeature
        }).addTo(districts);

        districtLayer = L.geoJson(MD_Dist, {
            style: style,
            onEachFeature: onEachDistrictFeature
        }).addTo(districts);

        precinctLayer = L.geoJson(MD_P, {
            style: style,
            onEachFeature: onEachPrecinctFeature
        }).addTo(precincts);

        districtLayer = L.geoJson(MN_Dist, {
            style: style,
            onEachFeature: onEachDistrictFeature
        }).addTo(districts);

        precinctLayer = L.geoJson(MN_P, {
            style: style,
            onEachFeature: onEachPrecinctFeature
        }).addTo(precincts);


        info = L.control();

        info.onAdd = function () {
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

        mymap.on("zoomend", function () {
            console.log(mymap.getZoom());
            if (mymap.getZoom() >= districtZoomLevel && mymap.hasLayer(stateLayer)){
                mymap.removeLayer(states);
                mymap.addLayer(districts);
            }
            if (mymap.getZoom() > precinctZoomLevel && mymap.hasLayer(districtLayer)){
                mymap.removeLayer(districts);
                mymap.addLayer(precincts);
            }
            if (mymap.getZoom() <= precinctZoomLevel && mymap.hasLayer(precinctLayer)){
                mymap.removeLayer(precincts);
                mymap.addLayer(districts);
            }
            if (mymap.getZoom() < districtZoomLevel && mymap.hasLayer(districtLayer)){
                mymap.removeLayer(districts);
                mymap.addLayer(states);
            }
        });

        mymap.on("click", function(e){
            if(L.geoJson(MN_Dist).getBounds().contains(e.latlng)){
                fitStateBounds(MN_Dist);
            }else if(L.geoJson(MD_Dist).getBounds().contains(e.latlng)){
                fitStateBounds(MD_Dist);
            }else if(L.geoJson(FL_Dist).getBounds().contains(e.latlng)){
                fitStateBounds(FL_Dist);
            }
        });

    $("#play-btn").on("click", function () {
        console.log("play button");
        weights = {};
        $("#weights input").each(function () {
            console.log($(this).attr("id") + " : " + $(this).val());
            weights[$(this).attr("id")] = $(this).val();
        });
        postData(weights, "/setweights", printData);
    });

/**
 * @todo Revise the function.
 */
$("#states").on("change", function() {
    const state = $("#states").val();
    if (state === "ALL")
        mymap.fitBounds(maxBounds);
    let stateBounds = {
        "FLORIDA" : FL_Dist,
        "MARYLAND" : MD_Dist,
        "MINNESOTA" : MN_Dist,
    };
    fitStateBounds(stateBounds[state]);
});

    function fitStateBounds(statebounds){
        var statebounds = L.geoJson(statebounds);
        mymap.fitBounds(statebounds.getBounds());
    }

        $("#majorMinor").change(function(){
            return document.getElementById("majorMinor").value;
        });

    function style() {
        console.log("style");
        return {
            fillColor: getColor(),
            weight: 1,
            color: 'white',
            fillOpacity: 0.7,
        };
    }

    function getColor() {
        return randomColor();
    }

    function highlightFeature(e) {
        var layer = e.target;

        layer.setStyle({
            weight: 4,
            color: 'darkgrey',
            fillOpacity: 0.7
        });

        if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
            layer.bringToFront();
        }
    }
    function dehighlightFeature(e){
        var layer = e.target;

        layer.setStyle({
            weight: 2,
            color: '#fff',
            fillOpacity: 0.7
        });

        if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
            layer.bringToFront();
        }
    }

    function highlightPrecinctFeature(e) {
        highlightFeature(e);
        loadPrecinctProperties(e.target);

        if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
            (e.target).bringToFront();
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
        info.update(democratic, republican, otherParties, all, otherRaces, caucasian, asian, hispanic, african, native, name);
    }

    function resetPrecinctHighlight(e) {
        info.update();
        dehighlightFeature(e);
    }

    function resetDistrictHighlight(e) {
        dehighlightFeature(e);
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

    function onEachStateFeature(feature, layer) {
        layer.on({
            mouseover: highlightFeature,
            mouseout: resetStateHighlight
        });
    }

    function resetStateHighlight(e) {
        dehighlightFeature(e);
    }

    // function disables() {
    //     if (sessionStorage.getItem("user") == null) {
    //         $("#controllpane").find("*").prop("disabled", true);
    //     }
    //
    // }
    // disables();
});






