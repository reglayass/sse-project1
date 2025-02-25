from flask import Flask, jsonify
import psycopg2
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.dialects.postgresql import ARRAY
from functools import reduce
import time
import math
import random

app = Flask(__name__)

# PostgreSQL Database Configuration
app.config['SQLALCHEMY_DATABASE_URI'] = 'postgresql://root:password@db:5432/root'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False  # Disable modification tracking

# Initialize SQLAlchemy
db = SQLAlchemy(app)

class Names(db.Model):
    __tablename__ = "name_basics"  # Match the existing table name

    id = db.Column(db.Integer, primary_key=True)
    primaryname = db.Column(db.String, nullable=False)
    birthyear = db.Column(db.Integer)
    deathyear = db.Column(db.Integer)
    primaryprofession = db.Column(ARRAY(db.String))
    knownfortitles = db.Column(ARRAY(db.String))

    def to_dict(self):
        return {
            "id": self.id, 
            "primaryName": self.primaryname, 
            "birthYear": self.birthyear,
            "deathYear": self.deathyear,
            "primaryProfession": self.primaryprofession if self.primaryprofession is not None else [],
            "knownForTitles": self.knownfortitles if self.knownfortitles is not None else []
        }

class Titles(db.Model):
    __tablename__ = "title_basics"
    
    id = db.Column(db.Integer, primary_key=True)
    titletype = db.Column(db.String, nullable=False)
    primarytitle = db.Column(db.String, nullable=False)
    originaltitle = db.Column(db.String, nullable=False)
    isadult = db.Column(db.Boolean, nullable=False)
    startyear = db.Column(db.Integer)
    endyear = db.Column(db.Integer)
    runtimeminutes = db.Column(db.Integer)
    genres = db.Column(ARRAY(db.String))
    
    def to_dict(self):
        return {
            "id": self.id,
            "titleType": self.titletype,
            "primaryTitle": self.primarytitle,
            "originalTitle": self.originaltitle,
            "isAdult": self.isadult,
            "startYear": self.startyear,
            "endYear": self.endyear,
            "runtimeMinutes": self.runtimeminutes,
            "genres": self.genres if self.genres is not None else []
        }


class TitleRatings(db.Model):
    __tablename__ = "title_ratings"

    id = db.Column(db.String, primary_key=True)
    averagerating = db.Column(db.Float)
    numvotes = db.Column(db.Integer)

    def to_dict(self):
        return {
            "id": self.id,
            "averageRating": self.averagerating,
            "numVotes": self.numvotes
        }


class TitleCrew(db.Model):
    __tablename__ = "title_crew"

    id = db.Column(db.String, primary_key=True)
    directors = db.Column(ARRAY(db.String))
    writers = db.Column(ARRAY(db.String))

    def to_dict(self):
        return {
            "id": self.id,
            "directors": self.directors if self.directors is not None else [],
            "writers": self.writers if self.writers is not None else []
        }

@app.route('/names')
def get_names():
    names = Names.query.all()
    return jsonify([name.to_dict() for name in names])

@app.route('/titles')
def get_titles():
    titles = Titles.query.all()
    return jsonify([title.to_dict() for title in titles])

@app.route('/ratings')
def get_ratings():
    ratings = TitleRatings.query.all()
    return jsonify([rating.to_dict() for rating in ratings])

@app.route('/crew')
def get_crew():
    crew = TitleCrew.query.all()
    return jsonify([c.to_dict() for c in crew])


@app.route('/stress_route')
def stress_route():
    titles = Titles.query.limit(50000).all()
    title_data = [title.to_dict() for title in titles]

    start_time = time.time()

    scores = list(map(lambda x: {
        'id': x['id'],
        'score': len(x['primaryTitle']) * (0.5 if 'Drama' in (x['genres'] or []) else 1.2),
        'ord': sum(ord(c) for c in x['primaryTitle']) % 100,
        'title_vectors': [ord(c) * math.sin(ord(c)) for c in x['primaryTitle'][:10]]
    }, title_data))

    filtered_scores = list(filter(lambda x: x['score'] > 20 and x['ord'] > 40, scores))

    stats = reduce(lambda acc, item: {
        'total_score': acc['total_score'] + item['score'],
        'avg_ord': (acc['avg_ord'] * acc['count'] + item['ord']) / (acc['count'] + 1),
        'max_score': max(acc['max_score'], item['score']),
        'min_score': min(acc['min_score'], item['score']),
        'count': acc['count'] + 1,
        'sum': [a + b for a, b in zip(acc['sum'],
                                             item['title_vectors'] + [0] * (10 - len(item['title_vectors'])))]
    }, filtered_scores, {
                       'total_score': 0,
                       'avg_ord': 0,
                       'max_score': 0,
                       'min_score': 99999999,
                       'count': 0,
                       'sum': [0] * 10
                   })

    end_time = time.time() - start_time

    return jsonify({
        'stats': stats,
        'time': end_time,
    })


if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=True)