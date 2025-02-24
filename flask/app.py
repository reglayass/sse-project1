from flask import Flask, jsonify
import psycopg2
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.dialects.postgresql import ARRAY

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

@app.route('/names')
def get_names():
    names = Names.query.all()
    return jsonify([name.to_dict() for name in names])

@app.route('/titles')
def get_titles():
    titles = Titles.query.all()
    return jsonify([title.to_dict() for title in titles])

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=True)