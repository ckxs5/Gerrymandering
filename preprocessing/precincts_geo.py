import MN.minnesota_precinct_geo
from shapely.geometry import Polygon, Point
from shapely.strtree import STRtree
import csv



data = MN.minnesota_precinct_geo.minnesota_precint_geo['features']
precincts_geo_data = {}
centroid_precinctId = {}


def flatten_list(l):
    if isinstance(l[0][0], (int, float)):
        return l
    else:
        temp = []
        for i in l:
            temp += flatten_list(i)
        return temp


def create_polygon(l):
    temp = flatten_list(l)
    return Polygon(temp)


def point_key(p):
    if isinstance(p, Point):
        return f"{p.x}_{p.y}"
    else:
        return None


for precinct_data in data:
    polygon = create_polygon(precinct_data['geometry']['coordinates'])

    centroid_precinctId[point_key(polygon.centroid)] = precinct_data['properties']['PrecinctID']

    precincts_geo_data[precinct_data['properties']['PrecinctID']] = {
        'id': precinct_data['properties']['PrecinctID'],
        'name': precinct_data['properties']['Precinct'],
        'county': precinct_data['properties']['County'],
        'type': precinct_data['geometry']['type'],
        'boundary': precinct_data['geometry']['coordinates'],
        'polygon': polygon
    }


def getPIdByBoundary(b):
    return centroid_precinctId[point_key(Polygon(b).centroid)]


def getPrecinctByBoundary(b):
    return precincts_geo_data[getPIdByBoundary(b)]


def getNeigbors():
    coordinates = []
    for key in precincts_geo_data.keys():
        coordinates.append(precincts_geo_data[key].get('polygon').boundary)
    # print(f"Total {len(precincts_geo_data.keys())} precincts")

    tree = STRtree(coordinates)
    for coordinate in coordinates:
        neighboring_coordinates = tree.query(coordinate)
        precinctId = getPIdByBoundary(coordinate)
        precinct = precincts_geo_data[precinctId]
        neighborIds = [getPIdByBoundary(n) for n in neighboring_coordinates]
        neighborIds.remove(precinctId)
        # print(f"{precinct['name']} id: {precinctId} neighbors: {[n for n in neighborIds]}")
        precinct["neighbors"] = neighborIds


getNeigbors()

print(precincts_geo_data)


def write_object_to_csv(file_path):
    temp = {}
    for key in precincts_geo_data.keys():
        temp[key] = precincts_geo_data[key].pop("polygon")

    print(temp.keys())
    print(len(temp.keys()))

    with open(file_path, "w", newline='') as csvfile:
        writer = csv.DictWriter(csvfile, fieldnames=list(precincts_geo_data[next(iter(precincts_geo_data))].keys()))
        print(precincts_geo_data[next(iter(precincts_geo_data))].keys())
        writer.writeheader()
        for precinct_geo_data in precincts_geo_data.values():
            # print(precincts_geo_data)
            writer.writerow(precinct_geo_data)

    for key in temp:
        precincts_geo_data[key]['polygon'] = temp[key]

    print("done!")


write_object_to_csv("mn_precinct_geo.csv")
