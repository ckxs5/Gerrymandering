mymap.fitBounds(targetState.getBounds());
loadStateJson();
function loadStateJson(){
            precinctData = maryland_precincts
            districtData = maryland_districts
            addDistrictsLayer();
}
function addDistrictsLayer() {
    L.geoJson(districtData, {
        style: function(){
            return {
                fillOpacity: 0.4,
                color: "grey"
            }
        }
    }).addTo(mymap);
}
function loadPrecincts(e) {
    mymap.fitBounds(e.target.getBounds());
    districtJson.remove();//wzh: remove select history
        addPrecinctsLayer();
}
function addPrecinctsLayer() {
    L.geoJson(precinctData, {
        style: function(){
            return {
                fillOpacity: 0.4,
                color: "grey"
            }
        },
    }).addTo(mymap);
}
