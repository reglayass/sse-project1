import * as express from "express";
import * as fs from 'fs';
import * as Papa from 'papaparse';
import { IMDBName, IMDBRating, IMDBTitle } from './interfaces';

const app = express();
app.use(express.json());
const { PORT = 3000 } = process.env;

const ratings: Map<string, IMDBRating> = new Map<string, IMDBRating>();
const names: Map<string, IMDBName> = new Map<string, IMDBName>();
const titles: Map<string, IMDBTitle> = new Map<string, IMDBTitle>();

const ratingsFile = fs.createReadStream("./data/title.ratings.tsv");
const namesFile = fs.createReadStream("./data/name.basics.tsv");
const titlesFile = fs.createReadStream("./data/title.basics.tsv");

const loadRatings = new Promise<void>((resolve, reject) => Papa.parse(ratingsFile, {
    delimiter: '\t',
    header: true,
    dynamicTyping: true, // Automatically convert numeric values
    skipEmptyLines: true,

    step: (result) => {
        const row = result.data as Record<string, any>;

        if (row.tconst && row.averageRating !== undefined && row.numVotes !== undefined) {
            const rating: IMDBRating = {
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

const loadNames = new Promise<void>((resolve, reject) => Papa.parse(namesFile, {
    delimiter: '\t',
    header: true,
    dynamicTyping: true, // Automatically convert numeric values
    skipEmptyLines: true,

    step: (result) => {
        const person = result.data as Record<string, any>;
        const processedData = {
            nconst: person.nconst,
            primaryName: person.primaryName,
            birthYear: person.birthYear !== '\\N' ? parseInt(person.birthYear) : null,
            deathYear: person.deathYear !== '\\N' ? parseInt(person.deathYear) : null,
            primaryProfession: person.primaryProfession !== '\\N' ? person.primaryProfession.split(',') : [],
            knownForTitles: person.knownForTitles !== '\\N' ? person.knownForTitles.split(',') : []
        }
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

const loadTitles = new Promise<void>((resolve, reject) => Papa.parse(titlesFile, {
    delimiter: '\t',
    header: true,
    dynamicTyping: true, // Automatically convert numeric values
    skipEmptyLines: true,

    step: (result) => {
        // Type assertion with safeguards
        const title = result.data as Record<string, any>;
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
        }
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
        const genre = req.query.genre as string; 
        const rating = parseInt(req.query.rating as string);
        const votes = parseInt(req.query.votes as string); 
        
        const resTitles = Array.from(titles.values())
            .filter((t) => t.genres.includes(genre) 
                && ratings.get(t.tconst)?.averageRating >= rating 
                && ratings.get(t.tconst)?.numVotes >= votes
            ).map((t) => t.tconst)

        const resName = Array.from(names.values()).filter(n => n.knownForTitles.some((t) => resTitles.includes(t)))
        res.send(resName);
    })

    app.get('/names', (req, res) => {
        res.send(Array.from(names.values()))
    })

    app.get('/titles', (req,res) => {
        res.send(Array.from(titles.values()))
    })
    return app.listen(PORT, () => {
        console.log(`Server running on http://localhost:${PORT}`);
    })
})
