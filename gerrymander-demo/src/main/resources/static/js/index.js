$("document").ready(function () {
    let states = L.layerGroup();
    let districts = L.layerGroup();
    let precincts = L.layerGroup();
    let stateLayer, districtLayer, precinctLayer;
    let mymap;
    let info;
    let maxBounds;

    mymap = L.map('map', {layers: states}).setView(USCENTER, defaultZoomLevel);
    L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
        maxZoom: MAXZOOM,
        minZoom: MINZOOM,
        id: 'mapbox.light'
    }).addTo(mymap);

    maxBounds = L.latLngBounds(
        L.latLng(NORTHWEST),
        L.latLng(SOUTHEAST)
    );

    mymap.setMaxBounds(maxBounds);
    mymap.fitBounds(maxBounds);

    // stateLayer = L.geoJson(statesData, {
    //     style:style,
    //     onEachFeature: onEachStateFeature
    // }).addTo(states);

    // districtLayer = L.geoJson(FL_Dist, {
    //     style: style,
    //     onEachFeature: onEachDistrictFeature
    // }).addTo(districts);

    // precinctLayer = L.geoJson(FL_P, {
    //     style: style,
    //     onEachFeature: onEachPrecinctFeature
    // }).addTo(precincts);

    // districtLayer = L.geoJson(MD_Dist, {
    //     style: style,
    //     onEachFeature: onEachDistrictFeature
    // }).addTo(districts);

    // precinctLayer = L.geoJson(MD_P, {
    //     style: style,
    //     onEachFeature: onEachPrecinctFeature
    // }).addTo(precincts);

    districtLayer = L.geoJson(MN_Dist, {
        style: style,
        onEachFeature: onEachDistrictFeature
    }).addTo(districts);

    precinctLayer = L.geoJson(MN_P, {
        style: style,
        onEachFeature: onEachPrecinctFeature
    }).addTo(precincts);

    mymap.on("zoomend", function () {
        console.log(mymap.getZoom());
        if (mymap.getZoom() >= districtZoomLevel && mymap.hasLayer(stateLayer)){
            mymap.removeLayer(states);
            mymap.addLayer(districts);
        }
        if (mymap.getZoom() > precinctZoomLevel || mymap.hasLayer(districtLayer)){
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

    function fitStateBounds(statebounds){
        statebounds = L.geoJson(statebounds);
        mymap.fitBounds(statebounds.getBounds());
    }

    info = L.control();

    info.onAdd = function () {
        this._div = L.DomUtil.create('div', 'info');
        this.update();
        return this._div;
    };

    info.update = function (democratic, republican, otherParties, all, otherRaces, caucasian, asian, hispanic, african, native, name,county) {
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
            + 'County: ' + county + '<br>'
            + '<br><b>Population</b><br>'
            + all
            : 'No Precinct Selected');
    };
    info.addTo(mymap);

    $("#play-btn").on("click", function () {
        console.log("play skip button");
        let weights = {};
        $("#weights input, select").each(function () {
            console.log($(this).attr("id") + " : " + $(this).val());
            weights[$(this).attr("id")] = $(this).val();
        });
        postData(weights, "/graphpartition", colorModifying);
    });

    $("#play-btn-not").on("click", function() {
        console.log("play not skip button");
        let weights = {};
        $("#weights input, select").each(function () {
            console.log($(this).attr("id") + " : " + $(this).val());
            weights[$(this).attr("id")] = $(this).val();
        });
        // while()
        postData(weights, "/graphpartition/once", colorModifying);
    })

    let precinctHashmap = {};

    precinctLayer.eachLayer(function(layer){
        precinctHashmap[layer.feature["properties"]["PrecinctID"]] = layer;
    });
    console.log(precinctHashmap);

    function colorModifying(data) {
        for (let i = 0; i < data.length; i++){
            let color = getColor();
            // console.log(color);
            for(let j = 0; j < data[i].length; j++){
                // console.log(precinctHashmap[data[i][j]].feature);
                precinctHashmap[data[i][j]].setStyle({
                    fillColor: color,
                    weight: 1,
                    color: 'white',
                    fillOpacity: 0.7
                });
                onEachPrecinctFeature(precinctHashmap[data[i][j]].feature, precinctHashmap[data[i][j]]);
            }
        }
    }

    $("#sign-in").on("click", function () {
        let usernameAndPassword = {};
        let count = 0;
        $("#toHome input").each(function () {
            console.log($(this).attr("name") + " : " + $(this).val());
            usernameAndPassword[$(this).attr("name")] = $(this).val();
        });
        console.log(usernameAndPassword);
        $.each(usernameAndPassword, function(key, value){
            if(value !== "")
                count += 1;
        });
        if (count === 2) {
            postForm(event, "#toHome", "/login", userlogin);
        }
    });

    $("#signupbtn").on("click", function () {
        $(".lightbox").delay(500).fadeIn(500);
    });
    $("#cancel-button").on("click", function () {
        $(".lightbox").stop().delay(500).fadeOut(500);
    });

    $(".ranges").each(function (){
        let weights = $(this).parent().children(':first-child').text();
        $(this).parent().children(':first-child').text(weights +": 50");
    });

    $(".custom-range").on("change", function() {
        let weights = $(this).parent().children(':last-child').attr('id')+": "+$(this).val();
        $(this).parent().children(':first-child').text(weights);
    });

    $("#btn-batch").on("change", function() {
        if($(this).prop('checked')){
            $("#batch-runs").show();
        }else{
            $("#batch-runs").hide();
        }
    });

    $("#batchform").on("submit", function(event) {
        event.preventDefault();
    })
    $("#num-district").on("submit", function(event) {
        event.preventDefault();
    })

    /**
     * @todo Revise the function.
     * @todo Revise stateBounds.
     */
    $("#states").on("change", function() {
        console.log($("#states").val());
        let state = $("#states").val();
        if (state === "ALL")
            mymap.fitBounds(maxBounds);
        let stateBounds = {
            // "FLORIDA" : FL_Dist,
            // "MARYLAND" : MD_Dist,
            "MINNESOTA" : MN_Dist,
        };
        fitStateBounds(stateBounds[state]);
    });

    // $("#communities").on("change", function() {
    //     console.log($("#communities").val());
    //     return $("#communities").val();
    // });

    // $(th: id${weight})

    /**
     * @todo Generate Color based on measurements in future
     */
    function style(features) {
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
        let layer = e.target;

        layer.setStyle({
            weight: 2,
            color: 'black',
            fillOpacity: 0.7
        });
        if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
            layer.bringToFront();
        }
    }
    function dehighlightFeature(e){
        let layer = e.target;
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
        let precinctId = layer.feature["properties"]["PrecinctID"]
        let  url = "/precinct/"+precinctId+"/data"
        getData(url, loadPrecinctPropertiesHelper)
    }

    function loadPrecinctPropertiesHelper(loadedJson) {
        let obj = loadedJson;
        let democratic = obj['democratic'] ? obj['democratic'] : "N/A"
        let republican = obj['republican'] ? obj['republican'] : "N/A"
        let other_parties = obj['other_parties'] ? obj['other_parties'] : "N/A"
        let all = obj['all'] ? obj['all'] : "N/A";
        let caucasian = obj['caucasian'] ? obj['caucasian'] : "N/A"
        let african_american = obj['african_american'] ? obj['african_american'] : "N/A"
        let asian = obj['asian'] ? obj['asian'] : "N/A"
        let native = obj['native'] ? obj['native'] : "N/A"
        let hispanic = obj['hispanic'] ? obj['hispanic'] : "N/A"
        let other_race = obj['other_race'] ? obj['other_race'] : "N/A"
        let county = obj['county'] ? obj['county'] : "N/A"
        let name = obj['name'] ? obj['name'] : "N/A"

        info.update(democratic, republican, other_parties, all, other_race, caucasian, asian, hispanic, african_american, native, name,county);
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