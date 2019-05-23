$("document").ready(function () {
    let states = L.layerGroup();
    let districts = L.layerGroup();
    let precincts = L.layerGroup();
    let stateLayer, districtLayer, precinctLayer;
    let mymap;
    let info;
    let disInfo;
    let maxBounds;
    let colorHashMap = {};
    let initCount = 0;

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

    stateLayer = L.geoJson(statesData, {
        style:style,
        onEachFeature: onEachStateFeature
    }).addTo(states);

    districtLayer = L.geoJson(FL_Dist, {
        style: style,
        onEachFeature: onEachDistrictFeature
    }).addTo(districts);

    precinctLayer = L.geoJson(FL_P, {
        style: style,
        onEachFeature: onEachPrecinctFeature
    }).addTo(precincts);

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

    disInfo = L.control();

    disInfo.onAdd = function () {
        this._div = L.DomUtil.create('div', 'info');
        this.update();
        return this._div;
    };

    disInfo.update = function (democratic, republican, otherParties, all, otherRaces, caucasian, asian, hispanic, african, native) {
        this._div.innerHTML = '<h4>District Information</h4>' + (all ?
            + '<b>Demographics</b><br>'
            + 'Asian/Pacific Islander: ' + asian.toLocaleString("en") + '<br>'
            + 'Caucasian: ' + caucasian.toLocaleString("en") + '<br>'
            + 'Hispanic (of Any Race): ' + hispanic.toLocaleString("en") + '<br>'
            + 'African-American: ' + african.toLocaleString("en") + '<br>'
            + 'Native American: ' + native.toLocaleString("en") + '<br>'
            + 'Other: ' + otherParties.toLocaleString("en") + '<br>'
            + '<br><b>Election</b><br>'
            + 'Democratic: ' + democratic.toLocaleString("en") + '<br>'
            + 'Republican: ' + republican.toLocaleString("en") + '<br>'
            + 'OtherParties: ' + otherParties.toLocaleString("en") + '<br>'
            + '<br><b>Population</b><br>'
            + all.toLocaleString("en")
            : 'No District Selected');
    };
    disInfo.addTo(mymap);


    info.update = function (democratic, republican, otherParties, all, otherRaces, caucasian, asian, hispanic, african, native, name,county, is_border) {
        this._div.innerHTML = '<h4>Precinct Information</h4>' + (all ?
            '<b>' + name + '</b><br>'
            + '<b>Demographics</b><br>'
            + 'Asian/Pacific Islander: ' + asian.toLocaleString("en") + '<br>'
            + 'Caucasian: ' + caucasian.toLocaleString("en") + '<br>'
            + 'Hispanic (of Any Race): ' + hispanic.toLocaleString("en") + '<br>'
            + 'African-American: ' + african.toLocaleString("en") + '<br>'
            + 'Native American: ' + native.toLocaleString("en") + '<br>'
            + 'Other: ' + otherParties.toLocaleString("en") + '<br>'
            + '<br><b>Election</b><br>'
            + 'Democratic: ' + democratic.toLocaleString("en") + '<br>'
            + 'Republican: ' + republican.toLocaleString("en") + '<br>'
            + 'OtherParties: ' + otherParties.toLocaleString("en") + '<br>'
            + 'County: ' + county + '<br>'
            + 'Is Border: ' + is_border + '<br>'
            + '<br><b>Population</b><br>'
            + all.toLocaleString("en")
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
        if(initCount === 0){
            postData(weights, "/init_algorithm", graphpartition);
            initCount += 1;
        }else{
            graphpartition(null);
        }

    });
    function graphpartition(data){
        postData(null, "/graphpartition", colorModifying);
    }

    $("#play-btn-not").on("click", function() {
        console.log("play not skip button");
        let weights = {};
        let count = 0;
        $("#weights input, select").each(function () {
            console.log($(this).attr("id") + " : " + $(this).val());
            weights[$(this).attr("id")] = $(this).val();
        });
        if(initCount === 0){
            postData(weights, "/init_algorithm", graphpartitionOnce);
            initCount += 1;
        }else {
            graphpartitionOnce(null);
        }

    });

    function graphpartitionOnce(data){
        postData(null, "/graphpartition/once", colorModifying);
    }

    $("#phase2Play").on("click", function(){
        postData(null, "/simulating_annealing", saColorModifying);
    })

    function saColorModifying(data){
        let color ;
        let player;
        console.log("sa coloring " + data);
        Object.keys(data).forEach(function(key) {
            if(key === "message") {
                console.log("Objectivefunctionvalue: " + data[key]);
                $("#objvalueMessage").text(data[key]);
                return;
            }
            if (key === "to"){
                color =  colorHashMap[data[key]];
            }else if (key === "p") {
                player = precinctHashmap[data[key]];
            }
        });
        player.setStyle({
            fillColor: color,
            weight: 1,
            color: 'white',
            fillOpacity: 0.7
        });
    }

    let precinctHashmap = {};
    precinctLayer.eachLayer(function(layer){
        precinctHashmap[layer.feature["properties"]["PrecinctID"]] = layer;
    });
    console.log(precinctHashmap);

    function colorModifying(data) {
        // data ={}
        console.log("Coloring: " + data);
        Object.keys(data).forEach(function(key){
            // console.log("This Coloring keys: " + key);
            if(key === "message") {
                console.log("datakey========" + data[key]);
                $("#consoleMessage").text(data[key]);
                return;
            }
            // console.log("value: " + data[key]);
            let color = getColor();
            colorHashMap[key] = color;
            for (let i = 0; i < data[key].length; i++) {
                precinctHashmap[data[key][i]].setStyle({
                    fillColor: color,
                    weight: 1,
                    color: 'white',
                    fillOpacity: 0.7
                });
                onEachPrecinctFeature(precinctHashmap[data[key][i]].feature, precinctHashmap[data[key][i]]);
            }
        });
    }

    $("#sign-in").on("click", function () {
        console.log("clicked");
        let usernameAndPassword = {};
        let count = 0;
        $("#toHome input").each(function () {
            console.log($(this).attr("name") + ":" + $(this).val());
            usernameAndPassword[$(this).attr("name")] = $(this).val();
        });
        $.each(usernameAndPassword, function(key, value){
            if(value !== "")
                count += 1;
            console.log("value: " + value);
        });
        if (count === 2) {
            console.log("count is 2");
            postForm(event, "#toHome", "/login", userlogin);
        }
    });

    $("#signup-button").on("click", function() {
       console.log("Start registing");
       let username_password = {};
       let count = 0;
       $("#signup input").each(function (){
           console.log($(this).attr("name") + ":" + $(this).val());
           username_password[$(this).attr("name")] = $(this).val();
       });
       $.each(username_password, function(key, value){
            if(value !== "")
                count += 1;
            console.log("value: " + value);
       });
       if (count === 2) {
            console.log("count is 2");
            postForm(event, "#signup", "/signup", usersignup);
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

    $("#district-toggle").on("change", function(){
        if($(this).prop('checked')){
            mymap.addLayer(districts);
        }else{
            mymap.removeLayer(districts);
        }
    });


    $("#batchform").on("submit", function(event) {
        event.preventDefault();
    });

    $("#num-district").on("submit", function(event) {
        event.preventDefault();
    });

    $("#num-mmDistrict").on("submit", function(event) {
        event.preventDefault();
    })

    $("#STATE_NAME").on("change", function() {
        console.log("State name: " + $("#STATE_NAME").val());
        let state = $("#STATE_NAME").val();

        if (state === "ALL")
            mymap.fitBounds(maxBounds);
        let stateBounds = {
            "FLORIDA" : FL_Dist,
            "MARYLAND" : MD_Dist,
            "MINNESOTA" : MN_Dist,
        };
        fitStateBounds(stateBounds[state]);
    });

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
        let precinctId = layer.feature["properties"]["PrecinctID"];
        let  url = "/precinct/"+precinctId+"/data";
        getData(url, loadPrecinctPropertiesHelper);
    }

    function loadPrecinctPropertiesHelper(loadedJson) {
        let obj = loadedJson;
        // console.log(obj);
        let democratic = obj['democratic'] ? obj['democratic'] : "N/A";
        let republican = obj['republican'] ? obj['republican'] : "N/A";
        let other_parties = obj['other_parties'] ? obj['other_parties'] : "N/A";
        let all = obj['all'] ? obj['all'] : "N/A";
        let caucasian = obj['caucasian'] ? obj['caucasian'] : "N/A";
        let african_american = obj['african_american'] ? obj['african_american'] : "N/A";
        let asian = obj['asian'] ? obj['asian'] : "N/A";
        let native = obj['native'] ? obj['native'] : "N/A";
        let hispanic = obj['hispanic'] ? obj['hispanic'] : "N/A";
        let other_race = obj['other_race'] ? obj['other_race'] : "N/A";
        let county = obj['county'] ? obj['county'] : "N/A";
        let name = obj['name'] ? obj['name'] : "N/A";
        let disId = obj['district_id'] ? obj['district_id'] : "N/A";
        let is_border = obj['is_border'] ? 'yes' : 'no';

        info.update(democratic, republican, other_parties, all, other_race, caucasian, asian, hispanic, african_american, native, name,county, is_border);
        getData("/district/" + disId + "/data", loadDistrictPropertiesHelper);
    }

    function loadDistrictPropertiesHelper(obj) {
        console.log("dist obj" + obj);
        let democratic = obj['democratic'] ? obj['democratic'] : "N/A";
        let republican = obj['republican'] ? obj['republican'] : "N/A";
        let other_parties = obj['other_parties'] ? obj['other_parties'] : "N/A";
        let all = obj.all ? obj.all: "N/A";
        let caucasian = obj['caucasian'] ? obj['caucasian'] : "N/A";
        let african_american = obj['african_american'] ? obj['african_american'] : "N/A";
        let asian = obj['asian'] ? obj['asian'] : "N/A";
        let native = obj['native'] ? obj['native'] : "N/A";
        let hispanic = obj['hispanic'] ? obj['hispanic'] : "N/A";
        let other_race = obj['other_race'] ? obj['other_race'] : "N/A";
        console.log("ALLT: " + obj.toString());

        disInfo.update(democratic, republican, other_parties, all, other_race, caucasian, asian, hispanic, african_american, native);
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
            mouseover: highlightPrecinctFeature,
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

    // if(sessionStorage.getItem("user") === null) {
    //     console.log("user id: " + sessionStorage.getItem("user"));
    //     $("#controllpane").find("*").prop("disabled", true);
    //     $("#district-toggle").prop("disabled", false);
    // }

});