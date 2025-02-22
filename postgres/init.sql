DROP TABLE IF EXISTS name_basics;
DROP TABLE IF EXISTS title_basics;
DROP TABLE IF EXISTS title_ratings;
DROP TABLE IF EXISTS title_crew;

CREATE TABLE name_basics (
    nconst TEXT PRIMARY KEY,
    primaryName TEXT,
    birthYear INT CHECK (birthYear >= 0 OR birthYear IS NULL),
    deathYear INT CHECK (deathYear >= 0 OR deathYear IS NULL),
    primaryProfession TEXT,
    knownForTitles TEXT
);

CREATE TABLE title_basics (
    tconst TEXT PRIMARY KEY,
    titleType TEXT NOT NULL,
    primaryTitle TEXT NOT NULL,
    originalTitle TEXT NOT NULL,
    isAdult INTEGER NOT NULL CHECK (isAdult IN (0, 1)), -- Ensuring BOOLEAN compatibility
    startYear INT CHECK (startYear >= 0 OR startYear IS NULL),
    endYear INT CHECK (endYear >= 0 OR endYear IS NULL),
    runtimeMinutes INT CHECK (runtimeMinutes >= 0 OR runtimeMinutes IS NULL),
    genres TEXT
);

CREATE TABLE title_ratings (
    tconst TEXT PRIMARY KEY,
    averageRating REAL CHECK (averageRating >= 0 AND averageRating <= 10), -- Using REAL instead of FLOAT
    numVotes INT CHECK (numVotes >= 0)
);

CREATE TABLE title_crew (
    tconst TEXT PRIMARY KEY,
    directors TEXT,
    writers TEXT
);

\copy name_basics FROM '/data/name.basics.tsv' DELIMITER E'\t' NULL '\N' CSV HEADER;
\copy title_basics FROM '/data/title.basics.tsv' DELIMITER E'\t' NULL '\N' CSV HEADER;
\copy title_ratings FROM '/data/title.ratings.tsv' DELIMITER E'\t' NULL '\N' CSV HEADER;
\copy title_crew FROM '/data/title.crew.tsv' DELIMITER E'\t' NULL '\N' CSV HEADER;


ALTER TABLE name_basics
    RENAME COLUMN nconst TO id;

ALTER TABLE title_basics
    RENAME COLUMN tconst TO id;

ALTER TABLE title_crew
    RENAME COLUMN tconst TO id;

ALTER TABLE title_ratings
    RENAME COLUMN tconst TO id;


ALTER TABLE name_basics
    ALTER COLUMN primaryProfession TYPE TEXT[] USING NULLIF(string_to_array(primaryProfession, ','), '{}'),
    ALTER COLUMN knownForTitles TYPE TEXT[] USING NULLIF(string_to_array(knownForTitles, ','), '{}');

ALTER TABLE title_basics
    ALTER COLUMN genres TYPE TEXT[] USING NULLIF(string_to_array(genres, ','), '{}');

ALTER TABLE title_crew
    ALTER COLUMN directors TYPE TEXT[] USING NULLIF(string_to_array(directors, ','), '{}'),
    ALTER COLUMN writers TYPE TEXT[] USING NULLIF(string_to_array(writers, ','), '{}');

COMMIT;
