import csv
import re
import json

dict_list = []
reader = csv.DictReader(open("MN_final_data.csv", "r"))

for line in reader:
    data = dict(line)
    for key in data.keys():
        if key != "id":
            try:
                data[key] = int(data[key])
            except ValueError:
                try:
                    data[key] = float(data[key])
                except ValueError:
                    pass
    data["neighbors"] = str(data["neighbors"]).replace('[', '').replace(']', '').replace(' ', '').replace("'", '').split(',')
    data["boundary"] = re.findall(r"\[.*?\]", data["boundary"])
    for i in range(0, len(data["boundary"])):
        data["boundary"][i] = list(map(float, str(data["boundary"][i]).replace('[', '').replace(']', '').replace(' ', '').split(',')))
    dict_list.append(data)

print(len(dict_list))
with open("precinct_data_pretty.json", 'w') as out_file:
    json.dump(dict_list, out_file, indent=4)

