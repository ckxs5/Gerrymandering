from shapely.geometry import Polygon, MultiPolygon, Point
from shapely.strtree import STRtree
import json

obj = []

with open('MN/FINAL_MN_NEW.json', 'r') as file:
    obj = json.load(file)

# print(obj[0]['boundary'])
# print(obj[0]['id'])

precinct_polygon = {}
centroid_precinctId = {}


def flatten_boundary(b):
    temp = b
    while not isinstance(temp[0][0], float):
        list = []
        for i in temp:
            if isinstance(i[0], float):
                list.append(i)
            else:
                for j in i:
                    list.append(j)
        temp = list
    return temp


def centroid_id(c):
    return f"{c.x}_{c.y}"

coordinates = []

for i in obj:
    p = Polygon(flatten_boundary(i['boundary']))
    coordinates.append(p.boundary)
    centroid_precinctId[centroid_id(p.centroid)] = i['id']

tree = STRtree(coordinates)
for coordinate in coordinates:
    neighbours = tree.query(coordinate)
    precinctId = centroid_precinctId[centroid_id(coordinate.centroid)]
    neighbour_Ids = [centroid_precinctId[centroid_id(n.centroid)] for n in neighbours]
    print(f"{precinctId} neighbours: {[n for n in neighbour_Ids]}")




