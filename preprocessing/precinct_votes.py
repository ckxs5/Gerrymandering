import csv
precincts = []
precincts_data = []
with open("2016-minnesota-precinct-president_cleaned.csv",
          newline="") as csvfile:
    reader = csv.DictReader(csvfile)
    for row in reader:
        precinct = row['precinct']
        party = row['party']
        votes = row['votes']
        if precinct not in precincts:
            precincts.append(precinct)
            precincts_data.append({
                "precinct": precinct,
                "country": row['county_name'],
                "county_lat": row['county_lat'],
                "county_long": row['county_long'],
                row['party']: row['votes']
            })
        else:
            precincts_data[precincts.index(precinct)][row['party']] = row['votes']

with open("2016-minnesota-precinct-president-preprocessed.csv", "w", newline='') as csvfile:
    writer = csv.DictWriter(csvfile, fieldnames=list(precincts_data[0].keys()))
    writer.writeheader()
    for precinct_data in precincts_data:
        writer.writerow(precinct_data)

print("done!")
