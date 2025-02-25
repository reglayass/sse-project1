"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const express = require("express");
const fs = require("fs");
const Papa = require("papaparse");
const app = express();
app.use(express.json());
const { PORT = 3000 } = process.env;
const ratings = new Map();
const names = new Map();
const titles = new Map();
const ratingsFile = fs.createReadStream("./data/title.ratings.tsv");
const namesFile = fs.createReadStream("./data/name.basics.tsv");
const titlesFile = fs.createReadStream("./data/title.basics.tsv");
const loadRatings = new Promise((resolve, reject) => Papa.parse(ratingsFile, {
    delimiter: '\t',
    header: true,
    dynamicTyping: true,
    skipEmptyLines: true,
    step: (result) => {
        const row = result.data;
        if (row.tconst && row.averageRating !== undefined && row.numVotes !== undefined) {
            const rating = {
                tconst: String(row.tconst),
                averageRating: Number(row.averageRating),
                numVotes: Number(row.numVotes)
            };
            ratings.set(rating.tconst, rating);
        }
    },
    complete: () => {
        resolve();
    },
    error: (error) => {
        ratingsFile.close();
        reject(new Error(`Failed to parse CSV: ${error.message}`));
    }
}));
const loadNames = new Promise((resolve, reject) => Papa.parse(namesFile, {
    delimiter: '\t',
    header: true,
    dynamicTyping: true,
    skipEmptyLines: true,
    step: (result) => {
        const person = result.data;
        const processedData = {
            nconst: person.nconst,
            primaryName: person.primaryName,
            birthYear: person.birthYear !== '\\N' ? parseInt(person.birthYear) : null,
            deathYear: person.deathYear !== '\\N' ? parseInt(person.deathYear) : null,
            primaryProfession: person.primaryProfession !== '\\N' ? person.primaryProfession.split(',') : [],
            knownForTitles: person.knownForTitles !== '\\N' ? person.knownForTitles.split(',') : []
        };
        names.set(processedData.nconst, processedData);
    },
    complete: () => {
        resolve();
    },
    error: (error) => {
        namesFile.close();
        reject(new Error(`Failed to parse CSV: ${error.message}`));
    }
}));
const loadTitles = new Promise((resolve, reject) => Papa.parse(titlesFile, {
    delimiter: '\t',
    header: true,
    dynamicTyping: true,
    skipEmptyLines: true,
    step: (result) => {
        // Type assertion with safeguards
        const title = result.data;
        const processedData = {
            tconst: title.tconst,
            titleType: title.titleType,
            primaryTitle: title.primaryTitle,
            originalTitle: title.originalTitle,
            isAdult: title.isAdult === '1',
            startYear: title.startYear !== '\\N' ? parseInt(title.startYear) : null,
            endYear: title.endYear !== '\\N' ? parseInt(title.endYear) : null,
            runtimeMinutes: title.runtimeMinutes !== '\\N' ? parseInt(title.runtimeMinutes) : null,
            genres: title.genres !== '\\N' ? title.genres.split(',') : []
        };
        titles.set(processedData.tconst, processedData);
    },
    complete: () => {
        resolve();
    },
    error: (error) => {
        titlesFile.close();
        reject(new Error(`Failed to parse CSV: ${error.message}`));
    }
}));
Promise.all([loadRatings, loadNames, loadTitles]).then(() => {
    app.get('/filter', (req, res) => {
        // Drama, 8 rating, 50 votes
        // get people known
        const genre = req.query.genre;
        const rating = parseInt(req.query.rating);
        const votes = parseInt(req.query.votes);
        const resTitles = Array.from(titles.values())
            .filter((t) => {
            var _a, _b;
            return t.genres.includes(genre)
                && ((_a = ratings.get(t.tconst)) === null || _a === void 0 ? void 0 : _a.averageRating) >= rating
                && ((_b = ratings.get(t.tconst)) === null || _b === void 0 ? void 0 : _b.numVotes) >= votes;
        }).map((t) => t.tconst);
        const resName = Array.from(names.values()).filter(n => n.knownForTitles.some((t) => resTitles.includes(t)));
        res.send(resName);
    });
    app.get('/names', (req, res) => {
        res.send(Array.from(names.values()));
    });
    app.get('/titles', (req, res) => {
        res.send(Array.from(titles.values()));
    });
    return app.listen(PORT, () => {
        console.log(`Server running on http://localhost:${PORT}`);
        console.log(titles);
    });
});
// }).catch(error => console.log(error))
//# sourceMappingURL=index.js.map