from flask import Flask, jsonify, request
import csv

# Initialize an empty dictionary
names_list = []
titles_list = []
ratings_list = []

# Open the TSV file and read it
with open("data/name.basics.tsv", mode="r", encoding="utf-8") as file:
    reader = csv.DictReader(file, delimiter="\t")  # Read TSV file
    for row in reader:
        names_list.append({
            "nconst": row["nconst"],
            "primaryName": row["primaryName"],
            "birthYear": int(row["birthYear"]) if row['birthYear'] != "\\N" else None,
            "deathYear": int(row["deathYear"]) if row["deathYear"] != "\\N" else None,
            "primaryProfession": row["primaryProfession"].split(","),
            "knownForTitles": row["knownForTitles"].split(","),
        })
        
# Open the TSV file and read it
with open("data/title.basics.tsv", mode="r", encoding="utf-8") as file:
    reader = csv.DictReader(file, delimiter="\t")  # Read TSV file
    for row in reader:
        titles_list.append({
            "tconst": row["tconst"],
            "titleType": row["titleType"],
            "primaryTitle": row["primaryTitle"],
            "originalTitle": row["originalTitle"],
            "isAdult": row["isAdult"],
            "startYear": int(row["startYear"]) if row["startYear"] != "\\N" else None,
            "endYear": int(row["endYear"]) if row["endYear"] != "\\N" else None,
            "runtimeMinutes": float(row["runtimeMinutes"]) if row["runtimeMinutes"] != "\\N" else None,
            "genres": row["genres"].split(",")
        })
        
with open("data/title.ratings.tsv", mode="r", encoding="utf-8") as file:
    reader = csv.DictReader(file, delimiter="\t")
    for row in reader:
        ratings_list.append({
            "tconst": row["tconst"],
            "averageRating": float(row["averageRating"]),
            "numVotes": int(row["numVotes"])
        })

app = Flask(__name__)

@app.route('/names')
def get_names():
    return jsonify(names_list)

@app.route('/titles')
def get_titles():
    return jsonify(titles_list)

@app.route('/filter')
def get_filtered_names():
    genre = request.args.get('genre')
    rating = float(request.args.get('rating'))
    votes = int(request.args.get('votes'))

    ratings_dict = {r["tconst"]: r for r in ratings_list}

    res_titles = [
        t["tconst"] for t in titles_list
        if genre in t["genres"]
        and float(ratings_dict.get(t["tconst"], {}).get("averageRating") or 0) >= rating
        and int(ratings_dict.get(t["tconst"], {}).get("numVotes") or 0) >= votes
    ]
    
    res_name = [
        n for n in names_list
        if any(t in res_titles for t in n.get("knownForTitles", []))
    ]

    return jsonify(res_name)  # Return the filtered names as JSON

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=True)