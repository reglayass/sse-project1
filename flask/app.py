from flask import Flask, jsonify
import csv

# Initialize an empty dictionary
names_list = []
titles_list = []

# Open the TSV file and read it
with open("name.basics.tsv", mode="r", encoding="utf-8") as file:
    reader = csv.DictReader(file, delimiter="\t")  # Read TSV file
    for row in reader:
        names_list.append({
            "id": row["nconst"],
            "primaryName": row["primaryName"],
            "birthYear": row["birthYear"],
            "deathYear": row["deathYear"],
            "primaryProfession": row["primaryProfession"].split(","),
            "knownForTitles": row["knownForTitles"].split(","),
        })
        
# Open the TSV file and read it
with open("title.basics.tsv", mode="r", encoding="utf-8") as file:
    reader = csv.DictReader(file, delimiter="\t")  # Read TSV file
    for row in reader:
        titles_list.append({
            "id": row["tconst"],
            "titleType": row["titleType"],
            "primaryTitle": row["primaryTitle"],
            "originalTitle": row["originalTitle"],
            "isAdult": row["isAdult"],
            "startYear": row["startYear"],
            "endYear": row["endYear"],
            "runtimeMinutes": row["runtimeMinutes"],
            "genres": row["genres"].split(",")
        })

app = Flask(__name__)

@app.route('/names')
def get_names():
    return jsonify(names_list)

@app.route('/titles')
def get_titles():
    return jsonify(titles_list)

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=True)