function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 44.985308, lng: -93.267210},
        zoom: 6
    });
}

function addpoly(){
    polygon = map.drawPolygon({
        paths: paths,
        useGeoJSON: true,
        strokeOpacity: 1,
        strokeWeight: 3,
        fillColor: '#BBD8E9',
        fillOpacity: 0.6
    });
}