// import maryland_precinct_2016.js
// var districtJson; //District handler added to the map
// var precinctJson;
//
// var mymap = L.map('mapid').setView([37.0902, -95.7129], 4);
// var mapAccessToken = 'pk.eyJ1Ijoib3ZlcnRoZWNsb3VkcyIsImEiOiJjam1hdWwxc2I1aGhrM3FwNGZ1cXd1c2c5In0.ixJrpwux_Hmz8kuRU-da-w';
// L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1Ijoib3ZlcnRoZWNsb3VkcyIsImEiOiJjam1hdWwxc2I1aGhrM3FwNGZ1cXd1c2c5In0.ixJrpwux_Hmz8kuRU-da-w', {
//     attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
//     maxZoom: 18,
//     id: 'mapbox.light'
// }).addTo(mymap);
//
// mymap.fitBounds(targetState.getBounds());
// loadStateJson();
// function loadStateJson(){
//             precinctData = maryland_precincts
//             districtData = maryland_districts
//             addDistrictsLayer();
// }
// function addDistrictsLayer() {
//     L.geoJson(districtData, {
//         style: function(){
//             return {
//                 fillOpacity: 0.4,
//                 color: "grey"
//             }
//         }
//     }).addTo(mymap);
// }
// function loadPrecincts(e) {
//     mymap.fitBounds(e.target.getBounds());
//         addPrecinctsLayer();
// }
// function addPrecinctsLayer() {
//     L.geoJson(precinctData, {
//         style: function(){
//             return {
//                 fillOpacity: 0.4,
//                 color: "grey"
//             }
//         }
//     }).addTo(mymap);
// }
