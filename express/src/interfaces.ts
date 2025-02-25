export interface IMDBTitle {
  tconst: string;              // Unique identifier (e.g., tt0000001)
  titleType: string;           // Type: movie, short, tvseries, tvepisode, etc.
  primaryTitle: string;        // Title in primary language
  originalTitle: string;       // Original title (if different)
  isAdult: boolean;            // Whether it's an adult title
  startYear: number | null;    // Release year or start year for series
  endYear: number | null;      // End year for series, null for other types
  runtimeMinutes: number | null; // Runtime in minutes
  genres: string[];            // Array of genres
}

export interface IMDBRating {
  tconst: string;          // Unique identifier matching title.basics.tsv
  averageRating: number;   // Average rating (1-10 scale)
  numVotes: number;        // Number of votes
}

export interface IMDBName {
  nconst: string;           // alphanumeric unique identifier
  primaryName: string;      // name by which the person is most often credited
  birthYear: number | null;        // in YYYY format
  deathYear: number | null;        // in YYYY format or '\N' if still alive
  primaryProfession: string[]; // the top-3 professions of the person
  knownForTitles: string[];   // titles the person is known for
}